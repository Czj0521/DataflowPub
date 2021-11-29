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
  void linkageTest(){
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
    dagManager.deleteDag("testWorkSpace");
  }
}
