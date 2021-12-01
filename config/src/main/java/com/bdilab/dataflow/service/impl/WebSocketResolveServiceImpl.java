package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.enums.OperatorOutputTypeEnum;
import com.bdilab.dataflow.service.WebSocketResolveService;
import com.bdilab.dataflow.utils.dag.DagFilterManager;
import com.bdilab.dataflow.utils.dag.DagNode;
import com.bdilab.dataflow.utils.dag.RealTimeDag;
import java.util.List;
import javax.annotation.Resource;

import com.bdilab.dataflow.utils.dag.dto.DagNodeInputDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


/**
 * Websocket parser implementation class.
 *
 * @author wjh
 */
@Service
@Slf4j
public class WebSocketResolveServiceImpl implements WebSocketResolveService {

  @Resource
  RealTimeDag realTimeDag;
  @Resource
  ScheduleServiceImpl scheduleService;
  @Resource
  DagFilterManager dagFilterManager;

  @Override
  public void resolve(String jsonString) {
    JSONObject jsonObject = JSONObject.parseObject(jsonString);
    String operatorType = jsonObject.getString("operatorType");
    String dagType = jsonObject.getString("dagType");
    String workspaceId = jsonObject.getString("workspaceId");
    String operatorId = (String) jsonObject.getOrDefault("operatorId", "");
    JSONObject desc = jsonObject.getJSONObject(operatorType + "Description");
    JSONArray dataSources = desc.getJSONArray("dataSource");
    String nodeType = "";

    switch (dagType) {
      case "addNode":
        DagNodeInputDto dagNodeInputDto = new DagNodeInputDto(operatorId,operatorType,desc);
        realTimeDag.addNode(workspaceId,dagNodeInputDto);
        if(isDataSourceReady(dataSources)){
          scheduleService.executeTask(workspaceId, operatorId);
        }
        break;
      case "updateNode":
        realTimeDag.updateNode(workspaceId, operatorId, desc);
        if(isDataSourceReady(dataSources)){
          scheduleService.executeTask(workspaceId, operatorId);
        }
        break;
      case "removeNode":
        List<DagNode> nextNodes = realTimeDag.getNextNodes(workspaceId, operatorId);
        nodeType = realTimeDag.getNode(workspaceId, operatorId).getNodeType();
        realTimeDag.removeNode(workspaceId, operatorId);
        if(OperatorOutputTypeEnum.isFilterOutput(nodeType)) {
          for (DagNode dagNode : nextNodes) {
            scheduleService.executeTask(workspaceId, dagNode.getNodeId());
          }
          dagFilterManager.deleteFilter(workspaceId, operatorId);
        }
        break;
      case "addEdge":
        String addPreNodeId = desc.getString("preNodeId");
        String addNextNodeId = desc.getString("nextNodeId");
        String addSlotIndex = desc.getString("slotIndex");
        realTimeDag.addEdge(workspaceId, addPreNodeId, addNextNodeId, Integer.valueOf(addSlotIndex));
        scheduleService.executeTask(workspaceId, addNextNodeId);
        break;
      case "removeEdge":
        String rmPreNodeId = desc.getString("preNodeId");
        String rmNextNodeId = desc.getString("nextNodeId");
        String rmSlotIndex = desc.getString("slotIndex");
        nodeType = realTimeDag.getNode(workspaceId, rmPreNodeId).getNodeType();
        realTimeDag.removeEdge(workspaceId,rmPreNodeId,rmNextNodeId,Integer.valueOf(rmSlotIndex));
        if(OperatorOutputTypeEnum.isFilterOutput(nodeType)) {
          scheduleService.executeTask(workspaceId, rmNextNodeId);
        }
        break;
      default:
        throw new RuntimeException("not exist this dagType");
    }

  }

  private boolean isDataSourceReady(JSONArray dataSources) {
    for (Object dataSource : dataSources) {
      if(StringUtils.isEmpty(dataSource)) {
        return false;
      }
    }
    return true;
  }
}
