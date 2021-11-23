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
    String[] dataSources = desc.getJSONArray("dataSource").toArray(new String[]{});

    DagNodeInputDto dagNodeInputDto = new DagNodeInputDto(operatorId,dataSources,operatorType,desc);


    switch (dagType) {
      case "addNode":
        realTimeDag.addNode(workspaceId,dagNodeInputDto);
//        realTimeDag.addNode(workspaceId, new DagNode(operatorId, operatorType, 1, desc));//todo
        scheduleService.executeTask(workspaceId, operatorId);
        break;
      case "updateNode":
        realTimeDag.updateNode(workspaceId, operatorId, desc);
        scheduleService.executeTask(workspaceId, operatorId);
        break;
      case "removeNode":
        realTimeDag.removeNode(workspaceId, operatorId);
//        List<String> nextNodesId = realTimeDag.getNode(workspaceId, operatorId).getNextNodesId();//todo
//        for (String s : nextNodesId) {
//          scheduleService.executeTask(workspaceId, s);
//        }
        break;
      case "addEdge":
        String addPreNodeId = desc.getString("preNodeId");
        String addNextNodeId = desc.getString("nextNodeId");
        realTimeDag.addEdge(workspaceId, addPreNodeId, addNextNodeId, 0);//todo
        scheduleService.executeTask(workspaceId, addNextNodeId);
        break;
      case "removeEdge":
        String rmPreNodeId = desc.getString("preNodeId");
        String rmNextNodeId = desc.getString("nextNodeId");
//        realTimeDag.removeEdge(workspaceId, rmPreNodeId, rmNextNodeId);//todo
        scheduleService.executeTask(workspaceId, rmNextNodeId);
        break;
      default:
        throw new RuntimeException("not exist this dagType");
    }


  }
}
