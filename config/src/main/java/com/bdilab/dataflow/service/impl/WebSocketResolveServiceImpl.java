package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.service.WebSocketResolveService;
import com.bdilab.dataflow.utils.dag.DagNode;
import com.bdilab.dataflow.utils.dag.RealTimeDag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wjh
 */
@Service
@Slf4j
public class WebSocketResolveServiceImpl implements WebSocketResolveService {

  @Resource
  RealTimeDag realTimeDag;

  @Override
  public void resolve(String jsonString) {
    JSONObject jsonObject = JSONObject.parseObject(jsonString);
    String operatorType = jsonObject.getString("operatorType");
    String dagType = jsonObject.getString("dagType");
    String workspaceId = jsonObject.getString("workspaceId");
    String operatorId = jsonObject.getString("operatorId");
    JSONObject desc = jsonObject.getJSONObject(operatorType + "Description");




    switch (dagType){
      case "addNode":
        realTimeDag.addNode(workspaceId,new DagNode(operatorId,operatorType,desc));
        break;
      case "updateNode":
        realTimeDag.updateNode(operatorId,operatorId,desc);
        break;
      default:
        throw new RuntimeException("not exist this dagType");

    }



  }
}
