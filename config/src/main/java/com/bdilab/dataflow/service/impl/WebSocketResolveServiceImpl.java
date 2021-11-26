package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.service.WebSocketResolveService;
import com.bdilab.dataflow.utils.dag.DagNode;
import com.bdilab.dataflow.utils.dag.RealTimeDag;
import java.util.List;
import javax.annotation.Resource;

import com.bdilab.dataflow.utils.dag.dto.DagNodeInputDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


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

  @Override
  public void resolve(String jsonString) {
    JSONObject jsonObject = JSONObject.parseObject(jsonString);
    String operatorType = jsonObject.getString("operatorType");
    String dagType = jsonObject.getString("dagType");
    String workspaceId = jsonObject.getString("workspaceId");
    String operatorId = (String) jsonObject.getOrDefault("operatorId", "");
    JSONObject desc = jsonObject.getJSONObject(operatorType + "Description");



    switch (dagType) {
      case "addNode":
        DagNodeInputDto dagNodeInputDto = new DagNodeInputDto(operatorId,operatorType,desc);
        realTimeDag.addNode(workspaceId,dagNodeInputDto);
        scheduleService.executeTask(workspaceId, operatorId);
        break;
      case "updateNode":
        realTimeDag.updateNode(workspaceId, operatorId, desc);
        scheduleService.executeTask(workspaceId, operatorId);
        break;
      case "removeNode":
        List<DagNode> nextNodes = realTimeDag.getNextNodes(workspaceId, operatorId);
        realTimeDag.removeNode(workspaceId, operatorId);

        for (DagNode dagNode : nextNodes) {
          scheduleService.executeTask(workspaceId, dagNode.getNodeId());
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
        realTimeDag.removeEdge(workspaceId,rmPreNodeId,rmNextNodeId,Integer.valueOf(rmSlotIndex));
        scheduleService.executeTask(workspaceId, rmNextNodeId);
        break;
      default:
        throw new RuntimeException("not exist this dagType");
    }


  }
}
