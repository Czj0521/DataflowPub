package com.bdilab.dataflow.utils.dag;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.consts.CommonConstants;
import com.bdilab.dataflow.common.enums.OperatorOutputTypeEnum;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseUtils;
import com.bdilab.dataflow.utils.redis.RedisUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Real time dag for dataflow.
 * You can use it to modify redis DAG graphs in real time.
 *
 * @author wh
 * @date 2021/11/14
 */
@Slf4j
@Component
public class RealTimeDag {
  @Resource
  RedisUtils redisUtils;
  @Resource
  ClickHouseUtils clickhouseUtils;

  /**
   * Add a node to the dag.
   *
   * @param workspaceId workspace ID
   * @param dagNode the node being added
   */
  public void addNode(String workspaceId, DagNode dagNode) {
    redisUtils.hset(workspaceId, dagNode.getNodeId(), dagNode);
  }

  /**
   * Add an edge to the dag.
   * Insert together. To prevent a insertion is successful, the other is a failure。
   *
   * @param workspaceId workspace ID
   * @param preNodeId ID of preceding node of the edge
   * @param nextNodeId ID of subsequent node of the edge
   */
  public void addEdge(String workspaceId, String preNodeId, String nextNodeId) {
    DagNode preNode = (DagNode) redisUtils.hget(workspaceId, preNodeId);
    DagNode nextNode = (DagNode) redisUtils.hget(workspaceId, nextNodeId);
    preNode.getNextNodesId().add(nextNodeId);
    JSONObject nodeDescription = (JSONObject) nextNode.getNodeDescription();
    String deleteTableName = "";
    if (OperatorOutputTypeEnum.isFilterOutput(preNode.getNodeType())) {
      //filter连table 或 filter连filter 不更改数据源，会有bug einblick也有bug。
      nextNode.getFilterId().add(preNodeId);
    } else {
      //table连table
      nextNode.getPreNodesId().add(preNodeId);
      deleteTableName = nodeDescription.getString("dataSource");
      nodeDescription.put("dataSource", preNodeId);
    }
    nextNode.setNodeDescription(nodeDescription);
    Map<String, Object> map = new HashMap<String, Object>(2) {
      {
        this.put(preNodeId, preNode);
        this.put(nextNodeId, nextNode);
      }
    };
    redisUtils.hmset(workspaceId, map);

    if(!"".equals(deleteTableName)){
      clickhouseUtils.deleteTable(deleteTableName);
    }
  }

  /**
   * Remove node from the dag.
   *
   * @param workspaceId workspace ID
   * @param deletedNodeId the ID of node that will be deleted
   */
  public void removeNode(String workspaceId, String deletedNodeId) {
    Map<Object, Object> dagMap = redisUtils.hmget(workspaceId);
    DagNode deletedNode = (DagNode) dagMap.get(deletedNodeId);
    JSONObject nodeDescription = (JSONObject) deletedNode.getNodeDescription();
    String deleteInputTableName = "";
    String newTableName = "";
    if (deletedNode.getPreNodesId().isEmpty() && deletedNode.getFilterId().isEmpty()) {
      //头节点
      deleteInputTableName = nodeDescription.getString("dataSource");
    }
    if (!deletedNode.getNextNodesId().isEmpty()) {
      if (OperatorOutputTypeEnum.isFilterOutput(deletedNode.getNodeType())) {
        // filter 没表，不删除
        deletedNode.getNextNodesId().forEach((nodeId) -> {
          ((DagNode) dagMap.get(nodeId)).getFilterId().remove(deletedNodeId);
        });
      } else {
        // table
        newTableName = CommonConstants.DATABASE + ".tempInput_" + deletedNodeId;
        for (String nodeId : deletedNode.getNextNodesId()) {
          DagNode nextNode = (DagNode) dagMap.get(nodeId);
          nextNode.getPreNodesId().remove(deletedNodeId);
          ((JSONObject) nextNode.getNodeDescription()).put("dataSource", newTableName);
          dagMap.put(nodeId, nextNode);
        }
      }
    }
    deletedNode.getPreNodesId().forEach((nodeId) -> {
      ((DagNode) dagMap.get(nodeId)).getNextNodesId().remove(deletedNodeId);
    });
    deletedNode.getFilterId().forEach((nodeId) -> {
      ((DagNode) dagMap.get(nodeId)).getNextNodesId().remove(deletedNodeId);
    });
    dagMap.remove(deletedNodeId);
    redisUtils.hdel(workspaceId, deletedNodeId);
    redisUtils.hmset(workspaceId, dagMap);

    if (!"".equals(deleteInputTableName)) {
      clickhouseUtils.deleteInputTable(deleteInputTableName);
    }
    if (!OperatorOutputTypeEnum.isFilterOutput(deletedNode.getNodeType())) {
      //table
      clickhouseUtils.deleteTable(deletedNodeId);
    }
    if (!"".equals(newTableName)) {
      clickhouseUtils.copyToTable(deletedNodeId, newTableName);
    }
  }

  /**
   * Remove edge from the dag.
   *
   * @param workspaceId workspace ID
   * @param preNodeId the ID of preceding node
   * @param nextNodeId the ID of subsequent node
   */
  public void removeEdge(String workspaceId, String preNodeId, String nextNodeId) {
    DagNode preNode = (DagNode) redisUtils.hget(workspaceId, preNodeId);
    DagNode nextNode = (DagNode) redisUtils.hget(workspaceId, nextNodeId);
    preNode.getNextNodesId().remove(nextNodeId);
    String newTableName = "";
    if (OperatorOutputTypeEnum.isFilterOutput(preNode.getNodeType())) {
      //filter
      nextNode.getFilterId().remove(preNodeId);
    } else {
      //table
      newTableName = CommonConstants.DATABASE + ".tempInput_" + preNodeId;
      ((JSONObject) nextNode.getNodeDescription()).put("dataSource", newTableName);
      nextNode.getPreNodesId().remove(preNodeId);
    }
    Map<String, Object> map = new HashMap<String, Object>(2) {
      {
        this.put(preNodeId, preNode);
        this.put(nextNodeId, nextNode);
      }
    };
    redisUtils.hmset(workspaceId, map);

    if (!"".equals(newTableName)) {
      //table
      clickhouseUtils.copyToTable(preNodeId, newTableName);
    }
  }

  /**
   * Clear dag.
   */
  public void clearDag(String workspaceId) {
    redisUtils.del(workspaceId);
  }

  /**
   * Updating node information.
   *
   * @param workspaceId workspace ID
   * @param nodeId new node
   * @param nodeDescription node description
   */
  public void updateNode(String workspaceId, String nodeId, Object nodeDescription) {
    DagNode node = (DagNode) redisUtils.hget(workspaceId, nodeId);
    node.setNodeDescription(nodeDescription);
    redisUtils.hset(workspaceId, nodeId, node);

    String newDataSource = ((JSONObject) nodeDescription).getString("dataSource");
    String oldDataSource = ((JSONObject) node.getNodeDescription()).getString("dataSource");
    if (!newDataSource.equals(oldDataSource)) {
      clickhouseUtils.deleteInputTable(oldDataSource);
    }
  }

  /**
   * Get node.
   *
   * @param workspaceId workspace ID
   * @param nodeId node ID
   * @return DagNode
   */
  public DagNode getNode(String workspaceId, String nodeId) {
    return (DagNode) redisUtils.hget(workspaceId, nodeId);
  }

  /**
   * Gets the subsequent nodes of this node.
   *
   * @param workspaceId workspace ID
   * @param nodeId node ID
   * @return list of dag node
   */
  public List<DagNode> getNextNodes(String workspaceId, String nodeId) {
    Map<Object, Object> dagMap = redisUtils.hmget(workspaceId);
    DagNode node = (DagNode) dagMap.get(nodeId);
    List<DagNode> nextNodes = new ArrayList<>();
    node.getNextNodesId().forEach((id) -> {
      nextNodes.add((DagNode) dagMap.get(id));
    });
    return nextNodes;
  }

  /**
   * Gets the preceding node of this node.
   *
   * @param workspaceId workspace ID
   * @param nodeId node ID
   * @return list of dag node
   */
  public List<DagNode> getPreNodes(String workspaceId, String nodeId) {
    Map<Object, Object> dagMap = redisUtils.hmget(workspaceId);
    DagNode node = (DagNode) dagMap.get(nodeId);
    List<DagNode> preNodes = new ArrayList<>();
    node.getPreNodesId().forEach((id) -> {
      preNodes.add((DagNode) dagMap.get(id));
    });
    return preNodes;
  }

  /**
   * Gets the preceding filter node of this node.
   *
   * @param workspaceId workspace ID
   * @param nodeId node ID
   * @return list of dag node
   */
  public List<DagNode> getFilterNodes(String workspaceId, String nodeId) {
    Map<Object, Object> dagMap = redisUtils.hmget(workspaceId);
    DagNode node = (DagNode) dagMap.get(nodeId);
    List<DagNode> preNodes = new ArrayList<>();
    node.getFilterId().forEach((id) -> {
      preNodes.add((DagNode) dagMap.get(id));
    });
    return preNodes;
  }

  /**
   * Get this dag list.
   *
   * @param workspaceId workspace ID
   * @return all nodes
   */
  public List<DagNode> getDag(String workspaceId) {
    List<DagNode> dag = new ArrayList<>();
    redisUtils.hmget(workspaceId).forEach((k, v) -> {
      dag.add((DagNode) v);
    });
    return dag;
  }

}
