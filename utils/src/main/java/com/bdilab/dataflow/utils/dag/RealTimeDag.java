package com.bdilab.dataflow.utils.dag;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.consts.CommonConstants;
import com.bdilab.dataflow.common.enums.OperatorOutputTypeEnum;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseUtils;
import com.bdilab.dataflow.utils.dag.dto.DagNodeInputDto;
import com.bdilab.dataflow.utils.redis.RedisUtils;

import java.util.*;
import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
   * @param dagNodeInputDto the nodeDto being added
   */
  public void addNode(String workspaceId, DagNodeInputDto dagNodeInputDto) {
    redisUtils.hset(workspaceId, dagNodeInputDto.getNodeId(), new DagNode(dagNodeInputDto));
  }

  /**
   * Add an edge to the dag.
   * Insert together. To prevent a insertion is successful, the other is a failure。
   *
   * @param workspaceId workspace ID
   * @param preNodeId ID of preceding node of the edge
   * @param nextNodeId ID of subsequent node of the edge
   */
  public void addEdge(String workspaceId, String preNodeId, String nextNodeId, Integer slotIndex) {
    DagNode preNode = (DagNode) redisUtils.hget(workspaceId, preNodeId);
    DagNode nextNode = (DagNode) redisUtils.hget(workspaceId, nextNodeId);
    preNode.getOutputDataSlots().add(new OutputDataSlot(nextNodeId, slotIndex));
//    JSONObject nodeDescription = (JSONObject) nextNode.getNodeDescription();
//    String deleteInputTableName = "";
    if (OperatorOutputTypeEnum.isFilterOutput(preNode.getNodeType())) {
      //前节点filter
      nextNode.getInputDataSlots()[slotIndex].getFilterId().add(preNodeId);
    } else {
      //前节点table
      nextNode.getInputDataSlots()[slotIndex].setPreNodeId(preNodeId);
//      deleteInputTableName = nodeDescription.getString("dataSource");
//      nodeDescription.put("dataSource", CommonConstants.CPL_TEMP_TABLE_PREFIX + preNodeId);
    }
//    nextNode.setNodeDescription(nodeDescription);
    Map<String, Object> map = new HashMap<String, Object>(2) {
      {
        this.put(preNodeId, preNode);
        this.put(nextNodeId, nextNode);
      }
    };
    redisUtils.hmset(workspaceId, map);

//    if(!StringUtils.isEmpty(deleteInputTableName)){
//      clickhouseUtils.deleteInputTable(deleteInputTableName);
//    }

//    DagNode preNode = (DagNode) redisUtils.hget(workspaceId, preNodeId);
//    DagNode nextNode = (DagNode) redisUtils.hget(workspaceId, nextNodeId);
//    preNode.getNextNodesId().add(nextNodeId);
//    JSONObject nodeDescription = (JSONObject) nextNode.getNodeDescription();
//    String deleteInputTableName = "";
//    if (OperatorOutputTypeEnum.isFilterOutput(preNode.getNodeType())) {
//      //filter连table 或 filter连filter 不更改数据源，会有bug einblick也有bug。
//      nextNode.getFilterId().add(preNodeId);
//    } else {
//      //table连table
//      nextNode.getPreNodesId().add(preNodeId);
//      deleteInputTableName = nodeDescription.getString("dataSource");
//      nodeDescription.put("dataSource", CommonConstants.CPL_TEMP_TABLE_PREFIX + preNodeId);
//    }
//    nextNode.setNodeDescription(nodeDescription);
//    Map<String, Object> map = new HashMap<String, Object>(2) {
//      {
//        this.put(preNodeId, preNode);
//        this.put(nextNodeId, nextNode);
//      }
//    };
//    redisUtils.hmset(workspaceId, map);
//
//    if(!"".equals(deleteInputTableName)){
//      clickhouseUtils.deleteInputTable(deleteInputTableName);
//    }
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
    for (int i=0;  i<deletedNode.getInputDataSlots().length; i++) {
      //删除前节点的next信息
      InputDataSlot inputDataSlot = deletedNode.getInputDataSlots()[i];
      String preNodeId = inputDataSlot.getPreNodeId();
      List<String> filterIds = inputDataSlot.getFilterId();
      OutputDataSlot deletedSlot = new OutputDataSlot(deletedNodeId, i);
      if(!StringUtils.isEmpty(preNodeId)){
        ((DagNode) dagMap.get(preNodeId)).getOutputDataSlots().remove(deletedSlot);
      }
      for (String filterId : filterIds) {
        ((DagNode) dagMap.get(filterId)).getOutputDataSlots().remove(deletedSlot);
      }
    }

    JSONObject nodeDescription = (JSONObject) deletedNode.getNodeDescription();
    List<String> deleteInputTableName = new ArrayList<>();
    String newTableName = "";
    String deleteTableName = "";
    if (OperatorOutputTypeEnum.isFilterOutput(deletedNode.getNodeType())) {
      //本节点为filter
      for (OutputDataSlot outputDataSlot : deletedNode.getOutputDataSlots()) {
        //删除后节点的filter信息
        DagNode nextNode = (DagNode) dagMap.get(outputDataSlot.getNextNodeId());
        nextNode.getInputDataSlots()[outputDataSlot.getNextSlotIndex()].getFilterId().remove(deletedNodeId);
        //todo 表维护
      }
    } else {
      //本节点为table
      deleteTableName = CommonConstants.CPL_TEMP_TABLE_PREFIX + deletedNodeId;
      newTableName = CommonConstants.CPL_TEMP_INPUT_TABLE_PREFIX + deletedNodeId;
      for (OutputDataSlot outputDataSlot : deletedNode.getOutputDataSlots()) {
        //删除后节点的table信息
        DagNode nextNode = (DagNode) dagMap.get(outputDataSlot.getNextNodeId());
        InputDataSlot inputSlot = nextNode.getInputDataSlots()[outputDataSlot.getNextSlotIndex()];
        inputSlot.setPreNodeId(null);
        inputSlot.setDataSource(newTableName);
        JSONObject nextNodeDescription = (JSONObject) nextNode.getNodeDescription();
        JSONArray dataSource = nextNodeDescription.getJSONArray("dataSource");
        dataSource.set(outputDataSlot.getNextSlotIndex(), newTableName);
        nextNodeDescription.put("dataSource", dataSource);
      }
    }
    if (isHeadNode(deletedNode.getInputDataSlots())) {
      //头节点
      for (Object dataSource : nodeDescription.getJSONArray("dataSource")) {
        deleteInputTableName.add((String) dataSource);
      }
    }
    dagMap.remove(deletedNodeId);
    redisUtils.hdel(workspaceId, deletedNodeId);
    redisUtils.hmset(workspaceId, dagMap);

    if(!deleteInputTableName.isEmpty()) {
      deleteInputTableName.forEach((name) -> {
        clickhouseUtils.deleteInputTable(name);
      });
    }
    if(!StringUtils.isEmpty(newTableName)){
      clickhouseUtils.copyToTable(CommonConstants.CPL_TEMP_TABLE_PREFIX + deletedNodeId, newTableName);
    }
    if(!StringUtils.isEmpty(deleteTableName)){
      clickhouseUtils.deleteTable(CommonConstants.CPL_TEMP_TABLE_PREFIX + deletedNodeId);
    }
  }

  /**
   * Remove edge from the dag.
   *
   * @param workspaceId workspace ID
   * @param preNodeId the ID of preceding node
   * @param nextNodeId the ID of subsequent node
   */
  public void removeEdge(String workspaceId, String preNodeId, String nextNodeId, Integer slotIndex) {
    DagNode preNode = (DagNode) redisUtils.hget(workspaceId, preNodeId);
    DagNode nextNode = (DagNode) redisUtils.hget(workspaceId, nextNodeId);
    preNode.getOutputDataSlots().remove(new OutputDataSlot(nextNodeId, slotIndex));
    String newTableName = "";
    if (OperatorOutputTypeEnum.isFilterOutput(preNode.getNodeType())) {
      //filter边
      nextNode.getInputDataSlots()[slotIndex].getFilterId().remove(preNodeId);
    } else {
      //table边
      nextNode.getInputDataSlots()[slotIndex].setPreNodeId(null);
      newTableName = CommonConstants.CPL_TEMP_INPUT_TABLE_PREFIX + preNodeId;
      JSONObject nextNodeDescription = (JSONObject) nextNode.getNodeDescription();
      JSONArray dataSource = nextNodeDescription.getJSONArray("dataSource");
      dataSource.set(slotIndex, "");
      nextNodeDescription.put("dataSource", dataSource);
    }
    Map<String, Object> map = new HashMap<String, Object>(2) {
      {
        this.put(preNodeId, preNode);
        this.put(nextNodeId, nextNode);
      }
    };
    redisUtils.hmset(workspaceId, map);

    if (!StringUtils.isEmpty(newTableName)) {
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
    JSONArray newDataSources = ((JSONObject) nodeDescription).getJSONArray("dataSource");
    JSONArray oldDataSources = ((JSONObject) node.getNodeDescription()).getJSONArray("dataSource");
    if(newDataSources.size()  != oldDataSources.size()){
      throw new RuntimeException("Input [dataSource] size error !");
    }
    List<String> deleteInputTableName = new ArrayList<>();
    if (!newDataSources.equals(oldDataSources)) {
      for (int i = 0; i < newDataSources.size(); i++) {
        String oldDataSource = oldDataSources.getString(i);
        String newDataSource = newDataSources.getString(i);
        if(!oldDataSource.equals(newDataSource)){
          deleteInputTableName.add(oldDataSource);
          node.getInputDataSlots()[i].setDataSource(newDataSource);
        }
      }
    }
    redisUtils.hset(workspaceId, nodeId, node);

    deleteInputTableName.forEach((name) -> {
      clickhouseUtils.deleteInputTable(name);
    });
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
    node.getOutputDataSlots().forEach((outputDataSlot) -> {
      nextNodes.add((DagNode) dagMap.get(outputDataSlot.getNextNodeId()));
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
    for (InputDataSlot inputDataSlot : node.getInputDataSlots()) {
      preNodes.add((DagNode) dagMap.get(inputDataSlot.getPreNodeId()));
    }
    return preNodes;
  }

  /**
   * Gets the preceding filter node of this node.
   *
   * @param workspaceId workspace ID
   * @param nodeId node ID
   * @return list of dag node
   */
  public List<DagNode> getFilterNodes(String workspaceId, String nodeId, Integer slotIndex) {
    Map<Object, Object> dagMap = redisUtils.hmget(workspaceId);
    DagNode node = (DagNode) dagMap.get(nodeId);
    List<DagNode> preNodes = new ArrayList<>();
    InputDataSlot inputDataSlot = node.getInputDataSlots()[slotIndex];
    inputDataSlot.getFilterId().forEach((filterId) -> {
      preNodes.add((DagNode) dagMap.get(filterId));
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

  private boolean isHeadNode(InputDataSlot[] inputDataSlots) {
    for (InputDataSlot inputDataSlot : inputDataSlots) {
      if(!StringUtils.isEmpty(inputDataSlot.getPreNodeId()) || !inputDataSlot.getFilterId().isEmpty()) {
        return false;
      }
    }
    return true;
  }
}
