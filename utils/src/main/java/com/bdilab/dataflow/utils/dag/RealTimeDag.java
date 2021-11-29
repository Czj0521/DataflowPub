package com.bdilab.dataflow.utils.dag;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.consts.CommonConstants;
import com.bdilab.dataflow.common.enums.OperatorOutputTypeEnum;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseManager;
import com.bdilab.dataflow.utils.dag.dto.DagNodeInputDto;
import com.bdilab.dataflow.utils.redis.RedisUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  ClickHouseManager clickhouseManager;

  /**
   * Add a node to the dag.
   *
   * @param workspaceId workspace ID
   * @param dagNodeInputDto the nodeDto being added
   */
  public void addNode(String workspaceId, DagNodeInputDto dagNodeInputDto) {
    redisUtils.hset(workspaceId, dagNodeInputDto.getNodeId(), new DagNode(dagNodeInputDto));
    log.info("Add node [{}] to [{}].", dagNodeInputDto.getNodeId(), workspaceId);
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
    Map<Object, Object> dagMap = redisUtils.hmget(workspaceId);
    DagNode preNode = (DagNode) dagMap.get(preNodeId);
    DagNode nextNode = (DagNode) dagMap.get(nextNodeId);

    if (!StringUtils.isEmpty(nextNode.getPreNodeId(slotIndex))) {
      //本数据槽已经被连了，需要替换
      DagNode oldPreNode = (DagNode) dagMap.get(nextNode.getPreNodeId(slotIndex));
      oldPreNode.removeOutputSlot(new OutputDataSlot(nextNodeId, slotIndex));
    }

    preNode.getOutputDataSlots().add(new OutputDataSlot(nextNodeId, slotIndex));
    String deleteInputTableName = "";
    if (OperatorOutputTypeEnum.isFilterOutput(preNode.getNodeType())) {
      nextNode.getFilterId(slotIndex).add(preNodeId);
      //自动填充数据集
      String fillDataSource = preNode.getInputDataSource(0);
      if (StringUtils.isEmpty(nextNode.getInputDataSource(slotIndex))
          && preNode.getInputSlotSize() == 1
          && !StringUtils.isEmpty(fillDataSource)) {
        nextNode.setDataSource(slotIndex, fillDataSource);
      }
    } else {
      deleteInputTableName = nextNode.getInputDataSource(slotIndex);
      nextNode.setPreNodeId(slotIndex, preNodeId);
      nextNode.setDataSource(slotIndex, CommonConstants.CPL_TEMP_TABLE_PREFIX + preNodeId);
    }
    Map<String, Object> map = new HashMap<String, Object>(2) {
      {
        this.put(preNodeId, preNode);
        this.put(nextNodeId, nextNode);
      }
    };
    redisUtils.hmset(workspaceId, map);
    log.info("Add edge between [{}] to slot [{}] of [{}] in [{}].", preNodeId, slotIndex, nextNodeId, workspaceId);

    if (!StringUtils.isEmpty(deleteInputTableName)) {
      clickhouseManager.deleteInputTable(deleteInputTableName);
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
    for (int i = 0;  i < deletedNode.getInputDataSlots().length; i++) {
      //删除前节点的next信息
      InputDataSlot inputDataSlot = deletedNode.getInputDataSlots()[i];
      String preNodeId = inputDataSlot.getPreNodeId();
      List<String> filterIds = inputDataSlot.getFilterId();
      OutputDataSlot deletedSlot = new OutputDataSlot(deletedNodeId, i);
      if (!StringUtils.isEmpty(preNodeId)) {
        ((DagNode) dagMap.get(preNodeId)).getOutputDataSlots().remove(deletedSlot);
      }
      for (String filterId : filterIds) {
        ((DagNode) dagMap.get(filterId)).getOutputDataSlots().remove(deletedSlot);
      }
    }

    List<String> deleteInputTableName = new ArrayList<>();
    String newTableName = "";
    String deleteTableName = "";
    if (OperatorOutputTypeEnum.isFilterOutput(deletedNode.getNodeType())) {
      //本节点为filter
      for (OutputDataSlot outputDataSlot : deletedNode.getOutputDataSlots()) {
        //删除后节点的filter信息
        DagNode nextNode = (DagNode) dagMap.get(outputDataSlot.getNextNodeId());
        nextNode.getFilterId(outputDataSlot.getNextSlotIndex()).remove(deletedNodeId);
      }
    } else {
      //本节点为table
      deleteTableName = CommonConstants.CPL_TEMP_TABLE_PREFIX + deletedNodeId;
      if (deletedNode.getOutputDataSlots().size() > 0) {
        newTableName = CommonConstants.CPL_TEMP_INPUT_TABLE_PREFIX + deletedNodeId;
        for (OutputDataSlot outputDataSlot : deletedNode.getOutputDataSlots()) {
          //删除后节点的table信息
          DagNode nextNode = (DagNode) dagMap.get(outputDataSlot.getNextNodeId());
          nextNode.setPreNodeId(outputDataSlot.getNextSlotIndex(), null);
          nextNode.setDataSource(outputDataSlot.getNextSlotIndex(), newTableName);
        }
      }
    }

    //删除输入
    for (InputDataSlot inputDataSlot : deletedNode.getInputDataSlots()) {
      if (StringUtils.isEmpty(inputDataSlot.getPreNodeId())) {
        deleteInputTableName.add(inputDataSlot.getDataSource());
      }
    }

    dagMap.remove(deletedNodeId);
    redisUtils.hdel(workspaceId, deletedNodeId);
    redisUtils.hmset(workspaceId, dagMap);
    log.info("Remove node [{}] in [{}].", deletedNodeId, workspaceId);

    if (!deleteInputTableName.isEmpty()) {
      deleteInputTableName.forEach((name) -> {
        clickhouseManager.deleteInputTable(name);
      });
    }
    if (!StringUtils.isEmpty(newTableName)) {
      clickhouseManager.copyToTable(CommonConstants.CPL_TEMP_TABLE_PREFIX + deletedNodeId,
          newTableName);
    }
    if (!StringUtils.isEmpty(deleteTableName)) {
      clickhouseManager.deleteTable(CommonConstants.CPL_TEMP_TABLE_PREFIX + deletedNodeId);
    }
  }

  /**
   * Remove edge from the dag.
   *
   * @param workspaceId workspace ID
   * @param preNodeId the ID of preceding node
   * @param nextNodeId the ID of subsequent node
   */
  public void removeEdge(String workspaceId,
                         String preNodeId,
                         String nextNodeId,
                         Integer slotIndex) {
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
      nextNode.setPreNodeId(slotIndex, null);
      nextNode.setDataSource(slotIndex, newTableName);
    }
    Map<String, Object> map = new HashMap<String, Object>(2) {
      {
        this.put(preNodeId, preNode);
        this.put(nextNodeId, nextNode);
      }
    };
    redisUtils.hmset(workspaceId, map);
    log.info("Add edge between [{}] to slot [{}] of [{}] in [{}].", preNodeId, slotIndex, nextNodeId, workspaceId);

    if (!StringUtils.isEmpty(newTableName)) {
      clickhouseManager.copyToTable(CommonConstants.CPL_TEMP_TABLE_PREFIX + preNodeId, newTableName);
    }
  }

  /**
   * Clear dag.
   *
   * @param workspaceId workspace ID
   */
  public void clearDag(String workspaceId) {
    Map<Object, Object> dagMap = redisUtils.hmget(workspaceId);
    redisUtils.del(workspaceId);
    log.info("Clear dag with workspace ID {}.", workspaceId);

    List<String> inputDataSources = new ArrayList<>();
    List<String> outputDataSources = new ArrayList<>();
    for (Map.Entry<Object, Object> node : dagMap.entrySet()) {
      DagNode value = (DagNode) node.getValue();
      inputDataSources.addAll(value.getInputDataSources());
      if (!OperatorOutputTypeEnum.isFilterOutput(value.getNodeType())) {
        outputDataSources.add(CommonConstants.CPL_TEMP_TABLE_PREFIX + value.getNodeId());
      }
    }
    for (String inputDataSource : inputDataSources) {
      clickhouseManager.deleteInputTable(inputDataSource);
    }
    for (String outputDataSource : outputDataSources) {
      clickhouseManager.deleteTable(outputDataSource);
    }
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
    JSONArray newDataSources = ((JSONObject) nodeDescription).getJSONArray("dataSource");
    JSONArray oldDataSources = ((JSONObject) node.getNodeDescription()).getJSONArray("dataSource");
    if (newDataSources.size()  != oldDataSources.size()) {
      log.error("Input [dataSource] size error !");
      throw new RuntimeException("Input [dataSource] size error !");
    }
    List<String> deleteInputTableName = new ArrayList<>();
    if (!newDataSources.equals(oldDataSources)) {
      for (int i = 0; i < newDataSources.size(); i++) {
        String oldDataSource = oldDataSources.getString(i);
        String newDataSource = newDataSources.getString(i);
        if (!oldDataSource.equals(newDataSource)) {
          deleteInputTableName.add(oldDataSource);
          node.setDataSource(i, newDataSource);
        }
      }
    }
    redisUtils.hset(workspaceId, nodeId, node);
    log.info("Update node [{}] in [{}]", nodeId, workspaceId);

    deleteInputTableName.forEach((name) -> {
      clickhouseManager.deleteInputTable(name);
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
      if (!StringUtils.isEmpty(inputDataSlot.getPreNodeId())
          || !inputDataSlot.getFilterId().isEmpty()) {
        return false;
      }
    }
    return true;
  }
}
