package com.bdilab.dataflow;

import com.bdilab.dataflow.service.impl.WebSocketResolveServiceImpl;
import com.bdilab.dataflow.utils.dag.DagManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author : [zhangpeiliang]
 * @description : [Test chart linkage]
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChartLinkageTest {

    @Resource
    WebSocketResolveServiceImpl webSocketResolveServiceImpl;
    @Resource
    DagManager dagManager;

    @Test
    void linkageTest() {
        dagManager.deleteDag("testWorkSpace");
        String addNode01 = "{\n" +
                "  \"job\": \"start_job\",\n" +
                "  \"chartDescription\": {\n" +
                "  \"dataSource\": [\"dataflow.car\"],\n" +
                "  \"jobType\": \"chart\",\n" +
                "  \"limit\": -1,\n" +
                "  \"filter\": \"\",\n" +
                "  \"onlyUpdateFilter\": false,\n" +
                "  \"menus\": [\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"Model\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"x-axis\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"Type\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"y-axis\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"color\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"size\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"row\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"column\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "   ]\n" +
                "  },\n" +
                "  \"operatorType\": \"chart\",\n" +
                "  \"dagType\": \"addNode\",\n" +
                "  \"operatorId\": \"testChartNode01\",\n" +
                "  \"workspaceId\": \"testWorkSpace\"\n" +
                "}";
        webSocketResolveServiceImpl.resolve(addNode01);
        String addNode02 = "{\n" +
                "  \"job\": \"start_job\",\n" +
                "  \"chartDescription\": {\n" +
                "  \"dataSource\": [\"\"],\n" +
                "  \"jobType\": \"chart\",\n" +
                "  \"limit\": -1,\n" +
                "  \"filter\": \"\",\n" +
                "  \"onlyUpdateFilter\": false,\n" +
                "  \"menus\": [\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"x-axis\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"y-axis\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"color\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"size\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"row\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"column\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "   ]\n" +
                "  },\n" +
                "  \"operatorType\": \"chart\",\n" +
                "  \"dagType\": \"addNode\",\n" +
                "  \"operatorId\": \"testChartNode02\",\n" +
                "  \"workspaceId\": \"testWorkSpace\"\n" +
                "}";
        webSocketResolveServiceImpl.resolve(addNode02);
        String addEdge01 = "{\n" +
                "  \"job\": \"start_job\",\n" +
                "  \"dagDescription\": {\n" +
                "    \"jobType\": \"addEdge\",\n" +
                "    \"preNodeId\": \"testChartNode01\",\n" +
                "    \"nextNodeId\": \"testChartNode02\",\n" +
                "    \"slotIndex\": \"0\"\n" +
                "  },\n" +
                "  \"operatorType\": \"dag\",\n" +
                "  \"dagType\": \"addEdge\",\n" +
                "  \"workspaceId\": \"testWorkSpace\"\n" +
                "}";
        webSocketResolveServiceImpl.resolve(addEdge01);
        String updateEdge01 = "{\n" +
                "  \"job\": \"start_job\",\n" +
                "  \"dagDescription\": {\n" +
                "    \"jobType\": \"updateEdge\",\n" +
                "    \"preNodeId\": \"testChartNode01\",\n" +
                "    \"nextNodeId\": \"testChartNode02\",\n" +
                "    \"slotIndex\": \"0\",\n" +
                "    \"edgeType\": \"2\"\n" +
                "  },\n" +
                "  \"operatorType\": \"dag\",\n" +
                "  \"dagType\": \"updateEdge\",\n" +
                "  \"workspaceId\": \"testWorkSpace\"\n" +
                "}";
        webSocketResolveServiceImpl.resolve(updateEdge01);
        String updateNode01 = "{\n" +
                "  \"job\": \"start_job\",\n" +
                "  \"chartDescription\": {\n" +
                "  \"dataSource\": [\"dataflow.car\"],\n" +
                "  \"jobType\": \"chart\",\n" +
                "  \"limit\": -1,\n" +
                "  \"filter\": \"Model = 'Model A Sport' AND Type = 'Sedan'\",\n" +
                "  \"onlyUpdateFilter\": true,\n" +
                "  \"menus\": [\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"Model\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"x-axis\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"Type\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"y-axis\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"color\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"size\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"row\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"column\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "   ]\n" +
                "  },\n" +
                "  \"operatorType\": \"chart\",\n" +
                "  \"dagType\": \"updateNode\",\n" +
                "  \"operatorId\": \"testChartNode01\",\n" +
                "  \"workspaceId\": \"testWorkSpace\"\n" +
                "}";
        webSocketResolveServiceImpl.resolve(updateNode01);
        String updateNode02 = "{\n" +
                "  \"job\": \"start_job\",\n" +
                "  \"chartDescription\": {\n" +
                "  \"dataSource\": [\"\"],\n" +
                "  \"jobType\": \"chart\",\n" +
                "  \"limit\": -1,\n" +
                "  \"filter\": \"\",\n" +
                "  \"onlyUpdateFilter\": false,\n" +
                "  \"menus\": [\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"Model\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"x-axis\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"Type\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"y-axis\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"color\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"size\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"row\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"column\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "   ]\n" +
                "  },\n" +
                "  \"operatorType\": \"chart\",\n" +
                "  \"dagType\": \"updateNode\",\n" +
                "  \"operatorId\": \"testChartNode02\",\n" +
                "  \"workspaceId\": \"testWorkSpace\"\n" +
                "}";
        webSocketResolveServiceImpl.resolve(updateNode02);
        dagManager.deleteDag("testWorkSpace");
    }

    @Test
    void linkageTest1() {
        dagManager.deleteDag("testWorkSpace");
        String addNode01 = "{\n" +
                "  \"job\": \"start_job\",\n" +
                "  \"chartDescription\": {\n" +
                "  \"dataSource\": [\"dataflow.car\"],\n" +
                "  \"jobType\": \"chart\",\n" +
                "  \"limit\": -1,\n" +
                "  \"filter\": \"Model = 'Model A Sport' and Type = 'Sedan' or Model = 'Model B FWD' and Type = 'SUV' or Model = 'Model C Standard' and Type = 'HATCHBACK'\",\n" +
                "  \"onlyUpdateFilter\": false,\n" +
                "  \"menus\": [\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"Model\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"x-axis\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"Type\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"y-axis\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"color\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"size\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"row\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"column\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "   ]\n" +
                "  },\n" +
                "  \"operatorType\": \"chart\",\n" +
                "  \"dagType\": \"addNode\",\n" +
                "  \"operatorId\": \"testChartNode01\",\n" +
                "  \"workspaceId\": \"testWorkSpace\"\n" +
                "}";
        webSocketResolveServiceImpl.resolve(addNode01);
        String addNode02 = "{\n" +
                "  \"job\": \"start_job\",\n" +
                "  \"chartDescription\": {\n" +
                "  \"dataSource\": [\"dataflow.car\"],\n" +
                "  \"jobType\": \"chart\",\n" +
                "  \"limit\": -1,\n" +
                "  \"filter\": \"Model = 'Model A Sport' and Type = 'Sedan' or Model = 'Model A Standard' and Type = 'Sedan'\",\n" +
                "  \"onlyUpdateFilter\": false,\n" +
                "  \"menus\": [\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"Model\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"x-axis\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"Type\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"y-axis\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"color\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"size\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"row\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"column\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "   ]\n" +
                "  },\n" +
                "  \"operatorType\": \"chart\",\n" +
                "  \"dagType\": \"addNode\",\n" +
                "  \"operatorId\": \"testChartNode02\",\n" +
                "  \"workspaceId\": \"testWorkSpace\"\n" +
                "}";
        webSocketResolveServiceImpl.resolve(addNode02);
        String addNode03 = "{\n" +
                "  \"job\": \"start_job\",\n" +
                "  \"chartDescription\": {\n" +
                "  \"dataSource\": [\"\"],\n" +
                "  \"jobType\": \"chart\",\n" +
                "  \"limit\": -1,\n" +
                "  \"filter\": \"\",\n" +
                "  \"onlyUpdateFilter\": false,\n" +
                "  \"menus\": [\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"x-axis\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"y-axis\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"color\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"size\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"row\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"column\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "   ]\n" +
                "  },\n" +
                "  \"operatorType\": \"chart\",\n" +
                "  \"dagType\": \"addNode\",\n" +
                "  \"operatorId\": \"testChartNode03\",\n" +
                "  \"workspaceId\": \"testWorkSpace\"\n" +
                "}";
        webSocketResolveServiceImpl.resolve(addNode03);
        String addEdge01 = "{\n" +
                "  \"job\": \"start_job\",\n" +
                "  \"dagDescription\": {\n" +
                "    \"jobType\": \"addEdge\",\n" +
                "    \"preNodeId\": \"testChartNode01\",\n" +
                "    \"nextNodeId\": \"testChartNode03\",\n" +
                "    \"slotIndex\": \"0\"\n" +
                "  },\n" +
                "  \"operatorType\": \"dag\",\n" +
                "  \"dagType\": \"addEdge\",\n" +
                "  \"workspaceId\": \"testWorkSpace\"\n" +
                "}";
        webSocketResolveServiceImpl.resolve(addEdge01);
        String addEdge02 = "{\n" +
                "  \"job\": \"start_job\",\n" +
                "  \"dagDescription\": {\n" +
                "    \"jobType\": \"addEdge\",\n" +
                "    \"preNodeId\": \"testChartNode02\",\n" +
                "    \"nextNodeId\": \"testChartNode03\",\n" +
                "    \"slotIndex\": \"0\"\n" +
                "  },\n" +
                "  \"operatorType\": \"dag\",\n" +
                "  \"dagType\": \"addEdge\",\n" +
                "  \"workspaceId\": \"testWorkSpace\"\n" +
                "}";
        webSocketResolveServiceImpl.resolve(addEdge02);
        String updateEdge01 = "{\n" +
                "  \"job\": \"start_job\",\n" +
                "  \"dagDescription\": {\n" +
                "    \"jobType\": \"updateEdge\",\n" +
                "    \"preNodeId\": \"testChartNode02\",\n" +
                "    \"nextNodeId\": \"testChartNode03\",\n" +
                "    \"slotIndex\": \"0\",\n" +
                "    \"edgeType\": \"2\"\n" +
                "  },\n" +
                "  \"operatorType\": \"dag\",\n" +
                "  \"dagType\": \"updateEdge\",\n" +
                "  \"workspaceId\": \"testWorkSpace\"\n" +
                "}";
        webSocketResolveServiceImpl.resolve(updateEdge01);
        String updateNode01 = "{\n" +
                "  \"job\": \"start_job\",\n" +
                "  \"chartDescription\": {\n" +
                "  \"dataSource\": [\"\"],\n" +
                "  \"jobType\": \"chart\",\n" +
                "  \"limit\": -1,\n" +
                "  \"filter\": \"\",\n" +
                "  \"onlyUpdateFilter\": false,\n" +
                "  \"menus\": [\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"Model\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"x-axis\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"Type\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"y-axis\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"color\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"size\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"row\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "     {\n" +
                "        \"aggregation\": \"none\",\n" +
                "        \"attribute\": \"none\",\n" +
                "        \"binning\": \"none\",\n" +
                "        \"menu\": \"column\",\n" +
                "        \"sort\": \"none\"\n" +
                "     },\n" +
                "   ]\n" +
                "  },\n" +
                "  \"operatorType\": \"chart\",\n" +
                "  \"dagType\": \"updateNode\",\n" +
                "  \"operatorId\": \"testChartNode03\",\n" +
                "  \"workspaceId\": \"testWorkSpace\"\n" +
                "}";
        webSocketResolveServiceImpl.resolve(updateNode01);
        String addNode04 = "{\n" +
                "  \"job\": \"start_job\",\n" +
                "  \"tableDescription\": {\n" +
                "    \"jobType\": \"table\",\n" +
                "    \"dataSource\": [\"\"],\n" +
                "    \"filter\": \"\",\n" +
                "    \"group\": [],\n" +
                "    \"limit\": 5,\n" +
                "    \"project\": []\n" +
                "  },\n" +
                "  \"operatorType\": \"table\",\n" +
                "  \"dagType\": \"addNode\",\n" +
                "  \"operatorId\": \"testNode04\",\n" +
                "  \"workspaceId\": \"testWorkSpace\"\n" +
                "}";
        webSocketResolveServiceImpl.resolve(addNode04);
        String addEdge03 = "{\n" +
                "  \"job\": \"start_job\",\n" +
                "  \"dagDescription\": {\n" +
                "    \"jobType\": \"addEdge\",\n" +
                "    \"preNodeId\": \"testChartNode03\",\n" +
                "    \"nextNodeId\": \"testNode04\",\n" +
                "    \"slotIndex\": \"0\"\n" +
                "  },\n" +
                "  \"operatorType\": \"dag\",\n" +
                "  \"dagType\": \"addEdge\",\n" +
                "  \"workspaceId\": \"testWorkSpace\"\n" +
                "}";
        webSocketResolveServiceImpl.resolve(addEdge03);
        String removeNode01 = "{\n" +
                "  \"job\": \"start_job\",\n" +
                "  \"dagDescription\": {\n" +
                "    \"jobType\": \"chart\",\n" +
                "  },\n" +
                "  \"operatorType\": \"dag\",\n" +
                "  \"dagType\": \"removeNode\",\n" +
                "  \"operatorId\": \"testChartNode01\",\n" +
                "  \"workspaceId\": \"testWorkSpace\"\n" +
                "}";
        webSocketResolveServiceImpl.resolve(removeNode01);
        dagManager.deleteDag("testWorkSpace");
    }
}