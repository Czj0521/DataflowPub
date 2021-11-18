package com.bdilab.dataflow.utils.dag;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.enums.OperatorOutputTypeEnum;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseUtils;
import com.bdilab.dataflow.utils.redis.RedisUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Real time dag for dataflow.
 * You can use it to modify redis DAG graphs in real time.
 *
 * @author wh
 * @date 2021/11/14
 */
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
    //todo 设置读写锁
    DagNode preNode = (DagNode) redisUtils.hget(workspaceId, preNodeId);
    DagNode nextNode = (DagNode) redisUtils.hget(workspaceId, nextNodeId);
    preNode.getNextNodesId().add(nextNodeId);
    JSONObject nodeDescription = (JSONObject) nextNode.getNodeDescription();
    if (OperatorOutputTypeEnum.isFilterOutput(preNode.getNodeType())) {
      nextNode.getFilterId().add(preNodeId);
    } else {
      nextNode.getPreNodesId().add(preNodeId);
      nodeDescription.put("datasource", preNodeId);
    }
    nextNode.setNodeDescription(nodeDescription);
    Map<String, Object> map = new HashMap<String, Object>(2) {
      {
        this.put(preNodeId, preNode);
        this.put(nextNodeId, nextNode);
      }
    };
    redisUtils.hmset(workspaceId, map);
  }

  /**
   * Remove node from the dag.
   *
   * @param workspaceId workspace ID
   * @param deletedNodeId the ID of node that will be deleted
   */
  public void removeNode(String workspaceId, String deletedNodeId) {
    //todo 设置读写锁
    Map<Object, Object> dagMap = redisUtils.hmget(workspaceId);
    DagNode deletedNode = (DagNode) dagMap.get(deletedNodeId);
    JSONObject nodeDescription = (JSONObject) deletedNode.getNodeDescription();

    deletedNode.getPreNodesId().forEach((nodeId) -> {
      ((DagNode) dagMap.get(nodeId)).getNextNodesId().remove(deletedNodeId);
    });
    if (deletedNode.getNextNodesId().isEmpty()) {
      clickhouseUtils.deleteTable(nodeDescription.getString("datasource"));
    } else {
      String newTableName = deletedNodeId + "_input";
      clickhouseUtils.renameTable(nodeDescription.getString("datasource"), newTableName);
      deletedNode.getNextNodesId().forEach((nodeId) -> {
        DagNode nextNode = (DagNode) dagMap.get(nodeId);
        nextNode.getPreNodesId().remove(deletedNodeId);
        ((JSONObject) nextNode.getNodeDescription()).put("datasource", newTableName);
        dagMap.put(nodeId, nextNode);
      });
    }

    dagMap.remove(deletedNodeId);
    redisUtils.del(workspaceId);
    redisUtils.hmset(workspaceId, dagMap);
  }

  public void removeEdge(String workspaceId, String preNodeId, String nextNodeId) {
    //todo 设置读写锁
    DagNode preNode = (DagNode) redisUtils.hget(workspaceId, preNodeId);
    DagNode nextNode = (DagNode) redisUtils.hget(workspaceId, nextNodeId);
    preNode.getNextNodesId().remove(nextNodeId);
    nextNode.getPreNodesId().remove(preNodeId);


    Map<String, Object> map = new HashMap<String, Object>(2) {
      {
        this.put(preNodeId, preNode);
        this.put(nextNodeId, nextNode);
      }
    };
    redisUtils.hmset(workspaceId, map);
  }

  public void clearDag(String workspaceId) {
    redisUtils.del(workspaceId);
  }

  public void updateNode(String workspaceId, String nodeId, Object nodeDescription) {
    DagNode node = (DagNode) redisUtils.hget(workspaceId, nodeId);
    node.setNodeDescription(nodeDescription);
    redisUtils.hset(workspaceId, nodeId, node);
  }

  public DagNode getNode(String workspaceId, String nodeId) {
    return (DagNode) redisUtils.hget(workspaceId, nodeId);
  }

  public List<DagNode> getNextNodes(String workspaceId, String nodeId) {
    //todo 设置读写锁
    Map<Object, Object> dagMap = redisUtils.hmget(workspaceId);
    DagNode node = (DagNode) dagMap.get(nodeId);
    List<DagNode> nextNodes = new ArrayList<>();
    node.getNextNodesId().forEach((id) -> {
      nextNodes.add((DagNode) dagMap.get(id));
    });
    return nextNodes;
  }

  public List<DagNode> getPreNodes(String workspaceId, String nodeId) {
    //todo 设置读写锁
    Map<Object, Object> dagMap = redisUtils.hmget(workspaceId);
    DagNode node = (DagNode) dagMap.get(nodeId);
    List<DagNode> preNodes = new ArrayList<>();
    node.getPreNodesId().forEach((id) -> {
      preNodes.add((DagNode) dagMap.get(id));
    });
    return preNodes;
  }

  public List<DagNode> getDag(String workspaceId) {
    List<DagNode> dag = new ArrayList<>();
    redisUtils.hmget(workspaceId).forEach((k, v) -> {
      dag.add((DagNode) v);
    });
    return dag;
  }

}
