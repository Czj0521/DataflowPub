package com.bdilab.dataflow;

import com.bdilab.dataflow.service.WebSocketResolveService;
import com.bdilab.dataflow.utils.dag.DagManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: Guo Yongqiang
 * @date: 2021/12/24 15:48
 * @version:
 */

@SpringBootTest(classes = DataFlowApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MutualInformationLinkageTest {
  @Autowired
  private WebSocketResolveService webSocketResolveService;
  @Autowired
  private DagManager dagManager;

  @Test
  public void linkTest() {
    dagManager.deleteDag("mutual_information_test");
    String addTable001 = "{\n" +
        "  \"job\": \"start_job\",\n" +
        "  \"tableDescription\": {\n" +
        "    \"jobType\": \"table\",\n" +
        "    \"dataSource\": [\"operator_mi.pointwise_mutual_information\"],\n" +
        "    \"filter\": \"\",\n" +
        "    \"group\": [\"\"],\n" +
        "    \"limit\": 2000,\n" +
        "    \"project\": [\"*\"]\n" +
        "  },\n" +
        "  \"operatorType\": \"table\",\n" +
        "  \"dagType\": \"addNode\",\n" +
        "  \"operatorId\": \"mutual_information_test_001\",\n" +
        "  \"workspaceId\": \"mutual_information_test\"\n" +
        "}";

    String addMutualInformation002 = "{\n" +
        "  \"job\": \"start_job\",\n" +
        "  \"mutualInformationDescription\": {\n" +
        "    \"jobType\": \"mutualInformation\",\n" +
        "    \"dataSource\": [\"\"],\n" +
        "    \"target\": \"\",\n" +
        "    \"features\": [\"\"]\n" +
        "  },\n" +
        "  \"operatorType\": \"mutualInformation\",\n" +
        "  \"dagType\": \"addNode\",\n" +
        "  \"operatorId\": \"mutual_information_test_002\",\n" +
        "  \"workspaceId\": \"mutual_information_test\"\n" +
        "}";

    String linkFrom001To002 = "{\n" +
        "  \"job\": \"start_job\",\n" +
        "  \"dagDescription\": {\n" +
        "    \"jobType\": \"addEdge\",\n" +
        "    \"preNodeId\": \"mutual_information_test_001\",\n" +
        "    \"nextNodeId\": \"mutual_information_test_002\",\n" +
        "    \"slotIndex\": \"0\"\n" +
        "  },\n" +
        "  \"operatorType\": \"dag\",\n" +
        "  \"dagType\": \"addEdge\",\n" +
        "  \"workspaceId\": \"mutual_information_test\"\n" +
        "}";

    String updateMutualInformation002 = "{\n" +
        "  \"job\": \"start_job\",\n" +
        "  \"mutualInformationDescription\": {\n" +
        "    \"jobType\": \"mutualInformation\",\n" +
        "    \"dataSource\": [\"\"],\n" +
        "    \"target\": \"x\",\n" +
        "    \"features\": [\"y\"]\n" +
        "  },\n" +
        "  \"operatorType\": \"mutualInformation\",\n" +
        "  \"dagType\": \"updateNode\",\n" +
        "  \"operatorId\": \"mutual_information_test_002\",\n" +
        "  \"workspaceId\": \"mutual_information_test\"\n" +
        "}";


    Assertions.assertDoesNotThrow(
        () -> {
          webSocketResolveService.resolve(addTable001);
          webSocketResolveService.resolve(addMutualInformation002);
          webSocketResolveService.resolve(linkFrom001To002);
          webSocketResolveService.resolve(updateMutualInformation002);
        }
    );
  }
}
