package com.bdilab.dataflow;

import com.bdilab.dataflow.service.impl.ScheduleServiceImpl;
import com.bdilab.dataflow.service.impl.WebSocketResolveServiceImpl;
import com.bdilab.dataflow.utils.dag.DagManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LinkageTest {
  @Resource
  WebSocketResolveServiceImpl webSocketResolveServiceImpl;
  @Resource
  DagManager dagManager;

  @Test
  void linkageTest1(){
    dagManager.deleteDag("testWorkSpace");
    String addNode01 = "{\n" +
        "  \"job\": \"start_job\",\n" +
        "  \"tableDescription\": {\n" +
        "    \"jobType\": \"table\",\n" +
        "    \"dataSource\": [\"dataflow.airuuid\"],\n" +
        "    \"filter\": \"(startsWith(city,'新'))\",\n" +
        "    \"group\": [],\n" +
        "    \"limit\": 2000,\n" +
        "    \"project\": [\"city\"]\n" +
        "  },\n" +
        "  \"operatorType\": \"table\",\n" +
        "  \"dagType\": \"addNode\",\n" +
        "  \"operatorId\": \"testNode01\",\n" +
        "  \"workspaceId\": \"testWorkSpace\"\n" +
        "}";
    webSocketResolveServiceImpl.resolve(addNode01);
    String addNode02 = "{\n" +
        "  \"job\": \"start_job\",\n" +
        "  \"tableDescription\": {\n" +
        "    \"jobType\": \"table\",\n" +
        "    \"dataSource\": [\"\"],\n" +
        "    \"filter\": \"\",\n" +
        "    \"group\": [],\n" +
        "    \"limit\": 0,\n" +
        "    \"project\": []\n" +
        "  },\n" +
        "  \"operatorType\": \"table\",\n" +
        "  \"dagType\": \"addNode\",\n" +
        "  \"operatorId\": \"testNode02\",\n" +
        "  \"workspaceId\": \"testWorkSpace\"\n" +
        "}";
    webSocketResolveServiceImpl.resolve(addNode02);
    String addEdge01 = "{\n" +
        "  \"job\": \"start_job\",\n" +
        "  \"dagDescription\": {\n" +
        "    \"jobType\": \"addEdge\",\n" +
        "    \"preNodeId\": \"testNode01\",\n" +
        "    \"nextNodeId\": \"testNode02\",\n" +
        "    \"slotIndex\": \"0\"\n" +
        "  },\n" +
        "  \"operatorType\": \"dag\",\n" +
        "  \"dagType\": \"addEdge\",\n" +
        "  \"workspaceId\": \"testWorkSpace\"\n" +
        "}";
    webSocketResolveServiceImpl.resolve(addEdge01);
    String removeEdge01 = "{\n" +
        "  \"job\": \"start_job\",\n" +
        "  \"dagDescription\": {\n" +
        "    \"jobType\": \"removeEdge\",\n" +
        "    \"preNodeId\": \"testNode01\",\n" +
        "    \"nextNodeId\": \"testNode02\",\n" +
        "    \"slotIndex\": \"0\"\n" +
        "  },\n" +
        "  \"operatorType\": \"dag\",\n" +
        "  \"dagType\": \"removeEdge\",\n" +
        "  \"workspaceId\": \"testWorkSpace\"\n" +
        "}";
    webSocketResolveServiceImpl.resolve(removeEdge01);
    String removeNode01 = "{\n" +
        "  \"job\": \"start_job\",\n" +
        "  \"dagDescription\": {\n" +
        "    \"jobType\": \"removeNode\",\n" +
        "  },\n" +
        "  \"operatorType\": \"dag\",\n" +
        "  \"dagType\": \"removeNode\",\n" +
        "  \"operatorId\": \"testNode02\",\n" +
        "  \"workspaceId\": \"testWorkSpace\"\n" +
        "}";
    webSocketResolveServiceImpl.resolve(removeNode01);


    String addNode03 = "{\n" +
        "  \"job\": \"start_job\",\n" +
        "  \"filterDescription\": {\n" +
        "           \"dataSource\": [\"dataflow.airuuid\"],\n" +
        "           \"filter\": \"AQI > 30 or AQI <10\",\n" +
        "           \"jobType\": \"filter\",\n" +
        "           \"limit\": -1\n" +
        "  },\n" +
        "  \"operatorType\": \"filter\",\n" +
        "  \"dagType\": \"addNode\",\n" +
        "  \"operatorId\": \"testNode03\",\n" +
        "  \"workspaceId\": \"testWorkSpace\"\n" +
        "}";
    webSocketResolveServiceImpl.resolve(addNode03);
    String addEdge02 = "{\n" +
        "  \"job\": \"start_job\",\n" +
        "  \"dagDescription\": {\n" +
        "    \"jobType\": \"addEdge\",\n" +
        "    \"preNodeId\": \"testNode03\",\n" +
        "    \"nextNodeId\": \"testNode01\",\n" +
        "    \"slotIndex\": \"0\"\n" +
        "  },\n" +
        "  \"operatorType\": \"dag\",\n" +
        "  \"dagType\": \"addEdge\",\n" +
        "  \"workspaceId\": \"testWorkSpace\"\n" +
        "}";
    webSocketResolveServiceImpl.resolve(addEdge02);
    String removeNode02 = "{\n" +
        "  \"job\": \"start_job\",\n" +
        "  \"dagDescription\": {\n" +
        "    \"jobType\": \"removeNode\",\n" +
        "  },\n" +
        "  \"operatorType\": \"dag\",\n" +
        "  \"dagType\": \"removeNode\",\n" +
        "  \"operatorId\": \"testNode03\",\n" +
        "  \"workspaceId\": \"testWorkSpace\"\n" +
        "}";
    webSocketResolveServiceImpl.resolve(removeNode02);
    dagManager.deleteDag("testWorkSpace");
  }

  @Test
  void linkageTest2(){
    dagManager.deleteDag("testWorkSpace");
    String addNode01 = "{\n" +
        "  \"job\": \"start_job\",\n" +
        "  \"tableDescription\": {\n" +
        "    \"jobType\": \"table\",\n" +
        "    \"dataSource\": [\"\"],\n" +
        "    \"filter\": \"\",\n" +
        "    \"group\": [],\n" +
        "    \"limit\": 0,\n" +
        "    \"project\": []\n" +
        "  },\n" +
        "  \"operatorType\": \"table\",\n" +
        "  \"dagType\": \"addNode\",\n" +
        "  \"operatorId\": \"testNode01\",\n" +
        "  \"workspaceId\": \"testWorkSpace\"\n" +
        "}";
    webSocketResolveServiceImpl.resolve(addNode01);
    String updateNode01 = "{\n" +
        "  \"job\": \"start_job\",\n" +
        "  \"tableDescription\": {\n" +
        "    \"jobType\": \"table\",\n" +
        "    \"dataSource\": [\"dataflow.airuuid\"],\n" +
        "    \"filter\": \"(startsWith(city,'新'))\",\n" +
        "    \"group\": [],\n" +
        "    \"limit\": 2000,\n" +
        "    \"project\": [\"city\"]\n" +
        "  },\n" +
        "  \"operatorType\": \"table\",\n" +
        "  \"dagType\": \"updateNode\",\n" +
        "  \"operatorId\": \"testNode01\",\n" +
        "  \"workspaceId\": \"testWorkSpace\"\n" +
        "}";
    webSocketResolveServiceImpl.resolve(updateNode01);
    dagManager.deleteDag("testWorkSpace");
  }


  @Test
  void linkageTest3(){
    //自动填充数据集
    dagManager.deleteDag("testWorkSpace");
    String addNode01 = "{\n" +
        "  \"job\": \"start_job\",\n" +
        "  \"tableDescription\": {\n" +
        "    \"jobType\": \"table\",\n" +
        "    \"dataSource\": [\"\"],\n" +
        "    \"filter\": \"\",\n" +
        "    \"group\": [],\n" +
        "    \"limit\": 0,\n" +
        "    \"project\": []\n" +
        "  },\n" +
        "  \"operatorType\": \"table\",\n" +
        "  \"dagType\": \"addNode\",\n" +
        "  \"operatorId\": \"testNode01\",\n" +
        "  \"workspaceId\": \"testWorkSpace\"\n" +
        "}";
    webSocketResolveServiceImpl.resolve(addNode01);
    String addNode02 = "{\n" +
        "  \"job\": \"start_job\",\n" +
        "  \"filterDescription\": {\n" +
        "           \"dataSource\": [\"dataflow.airuuid\"],\n" +
        "           \"filter\": \"AQI > 30 or AQI <10\",\n" +
        "           \"jobType\": \"filter\",\n" +
        "           \"limit\": -1\n" +
        "  },\n" +
        "  \"operatorType\": \"filter\",\n" +
        "  \"dagType\": \"addNode\",\n" +
        "  \"operatorId\": \"testNode02\",\n" +
        "  \"workspaceId\": \"testWorkSpace\"\n" +
        "}";
    webSocketResolveServiceImpl.resolve(addNode02);
    String addEdge02 = "{\n" +
        "  \"job\": \"start_job\",\n" +
        "  \"dagDescription\": {\n" +
        "    \"jobType\": \"addEdge\",\n" +
        "    \"preNodeId\": \"testNode02\",\n" +
        "    \"nextNodeId\": \"testNode01\",\n" +
        "    \"slotIndex\": \"0\"\n" +
        "  },\n" +
        "  \"operatorType\": \"dag\",\n" +
        "  \"dagType\": \"addEdge\",\n" +
        "  \"workspaceId\": \"testWorkSpace\"\n" +
        "}";
    webSocketResolveServiceImpl.resolve(addEdge02);
    dagManager.deleteDag("testWorkSpace");
  }
}
