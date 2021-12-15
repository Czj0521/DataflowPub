package com.bdilab.dataflow.utils.dag;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.annotation.LogMethodTime;
import com.bdilab.dataflow.common.consts.CommonConstants;
import com.bdilab.dataflow.common.enums.OperatorOutputTypeEnum;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseManager;
import com.bdilab.dataflow.utils.dag.consts.DagConstants;
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
  @LogMethodTime
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
  @LogMethodTime
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
    String[] copyTableNames = new String[2];
    if (OperatorOutputTypeEnum.isFilterOutput(preNode.getNodeType())) {
      nextNode.getFilterId(slotIndex).add(preNodeId);
      //自动填充数据集
      String fillDataSource = preNode.getInputDataSource(0);
      if (StringUtils.isEmpty(nextNode.getInputDataSource(slotIndex))
          && preNode.getInputSlotSize() == 1
          && !StringUtils.isEmpty(fillDataSource)) {
        copyTableNames[0] = fillDataSource;
        copyTableNames[1] = CommonConstants.CPL_TEMP_INPUT_TABLE_PREFIX + nextNodeId;
        nextNode.setDataSource(slotIndex, copyTableNames[1]);
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
    log.info("Add edge between [{}] to slot [{}] of [{}] in [{}].",
        preNodeId, slotIndex, nextNodeId, workspaceId);

    if (!StringUtils.isEmpty(deleteInputTableName)) {
      clickhouseManager.deleteInputTable(deleteInputTableName);
    }
    if (!StringUtils.isEmpty(copyTableNames[1])) {
      clickhouseManager.copyToTable(copyTableNames[0],
          copyTableNames[1]);
    }
  }

  /**
   * Remove node from the dag.
   *
   * @param workspaceId workspace ID
   * @param deletedNodeId the ID of node that will be deleted
   */
  @LogMethodTime
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
    String deleteTableName = "";
    String[] copyTableNames = new String[2];
    if (OperatorOutputTypeEnum.isFilterOutput(deletedNode.getNodeType())) {
      //本节点为filter
      for (OutputDataSlot outputDataSlot : deletedNode.getOutputDataSlots()) {
        //删除后节点的filter信息
        DagNode nextNode = (DagNode) dagMap.get(outputDataSlot.getNextNodeId());
        nextNode.getFilterId(outputDataSlot.getNextSlotIndex()).remove(deletedNodeId);
        nextNode.getEdgeTypeMap(outputDataSlot.getNextSlotIndex()).remove(deletedNodeId);
      }
    } else {
      //本节点为table
      deleteTableName = CommonConstants.CPL_TEMP_TABLE_PREFIX + deletedNodeId;
      if (deletedNode.getOutputDataSlots().size() > 0) {
        copyTableNames[0] = CommonConstants.CPL_TEMP_TABLE_PREFIX + deletedNodeId;
        copyTableNames[1] = CommonConstants.CPL_TEMP_INPUT_TABLE_PREFIX + deletedNodeId;
        for (OutputDataSlot outputDataSlot : deletedNode.getOutputDataSlots()) {
          //删除后节点的table信息
          DagNode nextNode = (DagNode) dagMap.get(outputDataSlot.getNextNodeId());
          nextNode.setPreNodeId(outputDataSlot.getNextSlotIndex(), null);
          nextNode.setDataSource(outputDataSlot.getNextSlotIndex(), copyTableNames[1]);
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
    if (!StringUtils.isEmpty(copyTableNames[1])) {
      clickhouseManager.copyToTable(copyTableNames[0],
          copyTableNames[1]);
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
  @LogMethodTime
  public void removeEdge(String workspaceId,
                         String preNodeId,
                         String nextNodeId,
                         Integer slotIndex) {
    DagNode preNode = (DagNode) redisUtils.hget(workspaceId, preNodeId);
    DagNode nextNode = (DagNode) redisUtils.hget(workspaceId, nextNodeId);
    preNode.getOutputDataSlots().remove(new OutputDataSlot(nextNodeId, slotIndex));
    String[] copyTableNames = new String[2];
    if (OperatorOutputTypeEnum.isFilterOutput(preNode.getNodeType())) {
      //filter边
      nextNode.getFilterId(slotIndex).remove(preNodeId);
      nextNode.getEdgeTypeMap(slotIndex).remove(preNodeId);
    } else {
      //table边
      nextNode.getInputDataSlots()[slotIndex].setPreNodeId(null);
      copyTableNames[0] = CommonConstants.CPL_TEMP_TABLE_PREFIX + preNodeId;
      copyTableNames[1] = CommonConstants.CPL_TEMP_INPUT_TABLE_PREFIX + preNodeId;
      nextNode.setPreNodeId(slotIndex, null);
      nextNode.setDataSource(slotIndex, copyTableNames[1]);
    }
    Map<String, Object> map = new HashMap<String, Object>(2) {
      {
        this.put(preNodeId, preNode);
        this.put(nextNodeId, nextNode);
      }
    };
    redisUtils.hmset(workspaceId, map);
    log.info("Add edge between [{}] to slot [{}] of [{}] in [{}].",
        preNodeId, slotIndex, nextNodeId, workspaceId);

    if (!StringUtils.isEmpty(copyTableNames[1])) {
      clickhouseManager.copyToTable(copyTableNames[0], copyTableNames[1]);
    }
  }

  /**
   * Clear dag.
   *
   * @param workspaceId workspace ID
   */
  @LogMethodTime
  public void clearDag(String workspaceId) {
    Map<Object, Object> dagMap = redisUtils.hmget(workspaceId);
    redisUtils.del(workspaceId);
    log.info("Clear dag with workspace ID [{}].", workspaceId);

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
  @LogMethodTime
  public void updateNode(String workspaceId, String nodeId, Object nodeDescription) {
    DagNode node = (DagNode) redisUtils.hget(workspaceId, nodeId);
    JSONObject newNodeDescription = (JSONObject) nodeDescription;
    JSONArray newDataSources = newNodeDescription.getJSONArray("dataSource");
    JSONArray oldDataSources = ((JSONObject) node.getNodeDescription()).getJSONArray("dataSource");
    if (newDataSources.size()  != oldDataSources.size()) {
      log.error("Input [dataSource] size error !");
      throw new RuntimeException("Input [dataSource] size error !");
    }
    newNodeDescription.put("dataSource", oldDataSources);
    node.setNodeDescription(nodeDescription);
    //    List<String> deleteInputTableName = new ArrayList<>();
    //    if (!newDataSources.equals(oldDataSources)) {
    //      for (int i = 0; i < newDataSources.size(); i++) {
    //        String oldDataSource = oldDataSources.getString(i);
    //        String newDataSource = newDataSources.getString(i);
    //        if (!oldDataSource.equals(newDataSource)) {
    //          deleteInputTableName.add(oldDataSource);
    //          node.setDataSource(i, newDataSource);
    //        }
    //      }
    //    }
    redisUtils.hset(workspaceId, nodeId, node);
    log.info("Update node [{}] in [{}]", nodeId, workspaceId);

    //    deleteInputTableName.forEach((name) -> {
    //      clickhouseManager.deleteInputTable(name);
    //    });
  }

  /**
   * Update edge.
   *
   * @param workspaceId workspace Id
   * @param preNodeId preNode Id
   * @param nextNodeId nextNode Id
   * @param slotIndex slot index
   * @param edgeType edge type
   */
  @LogMethodTime
  public void updateEdge(String workspaceId,
                         String preNodeId,
                         String nextNodeId,
                         Integer slotIndex,
                         String edgeType) {
    DagNode nextNode = (DagNode) redisUtils.hget(workspaceId, nextNodeId);
    DagNode preNode = (DagNode) redisUtils.hget(workspaceId, preNodeId);
    switch (edgeType) {
      case DagConstants.DEFAULT_LINE:
        break;
      case DagConstants.DASHED_LINE:
        if (!OperatorOutputTypeEnum.isFilterOutput(preNode.getNodeType())) {
          throw new RuntimeException("For dashed edge, the output node must be of type Filter !");
        }
        break;
      case DagConstants.BRUSH_LINE:
        if (!OperatorOutputTypeEnum.isChart(preNode.getNodeType())
            || !OperatorOutputTypeEnum.isChart(nextNode.getNodeType())) {
          throw new RuntimeException("For brush edge, both of output and input node must be Chart !");
        }
        break;
      default:
        throw new RuntimeException("Error edge type !");
    }
    nextNode.getEdgeTypeMap(slotIndex).put(preNodeId, edgeType);
    redisUtils.hset(workspaceId, nextNodeId, nextNode);

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
   * Get edge type.
   *
   * @param workspaceId workspace ID
   * @param nodeId node ID
   * @param slotIndex slot index
   * @param preNodeId preNode ID
   * @return edge type
   */
  public String getEdgeType(String workspaceId,
                            String nodeId,
                            Integer slotIndex,
                            String preNodeId) {
    DagNode node = (DagNode) redisUtils.hget(workspaceId, nodeId);
    return node.getEdgeType(slotIndex, preNodeId);
  }

  /**
   * Is default edge or not.
   *
   * @param workspaceId workspace ID
   * @param nodeId node ID
   * @param slotIndex slot index
   * @param preNodeId preNode ID
   * @return edge type
   */
  public boolean isDefaultEdge(String workspaceId,
                              String nodeId,
                              Integer slotIndex,
                              String preNodeId) {
    DagNode node = (DagNode) redisUtils.hget(workspaceId, nodeId);
    return node.isDefaultEdge(slotIndex, preNodeId);
  }

  /**
   * Is dashed edge or not.
   *
   * @param workspaceId workspace ID
   * @param nodeId node ID
   * @param slotIndex slot index
   * @param preNodeId preNode ID
   * @return edge type
   */
  public boolean isDashedEdge(String workspaceId,
                             String nodeId,
                             Integer slotIndex,
                             String preNodeId) {
    DagNode node = (DagNode) redisUtils.hget(workspaceId, nodeId);
    return node.isDashedEdge(slotIndex, preNodeId);
  }

  /**
   * Is brush edge or not.
   *
   * @param workspaceId workspace ID
   * @param nodeId node ID
   * @param slotIndex slot index
   * @param preNodeId preNode ID
   * @return edge type
   */
  public boolean isBrushEdge(String workspaceId,
                             String nodeId,
                             Integer slotIndex,
                             String preNodeId) {
    DagNode node = (DagNode) redisUtils.hget(workspaceId, nodeId);
    return node.isBrushEdge(slotIndex, preNodeId);
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
