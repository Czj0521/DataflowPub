package com.bdilab.dataflow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.jobdescription.MaterializeDescription;
import com.bdilab.dataflow.dto.jobdescription.TableDescription;
import com.bdilab.dataflow.dto.jobinputjson.MaterializeInputJson;
import com.bdilab.dataflow.dto.joboutputjson.MaterializeOutputJson;
import com.bdilab.dataflow.service.MaterializeJobService;
import com.bdilab.dataflow.service.impl.MaterializeJobServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * UT of the materialize operator.

 * @author: wh
 * @create: 2021-10-30
 */
@SpringBootTest
public class MaterializeTest {
    @Resource
    MaterializeJobService materializeJobService;

    @ParameterizedTest
    @MethodSource("descriptionProvider")
    public void materializeTable(String materializedType, String materializedOperator) {
        MaterializeDescription materializeDescription = new MaterializeDescription();
        materializeDescription.setMaterializedType(materializedType);
        materializeDescription.setMaterializedOperator(JSONObject.parseObject(materializedOperator));
        assertDoesNotThrow(() -> {
            MaterializeOutputJson materialize = materializeJobService.materialize(new MaterializeInputJson(materializeDescription));
            System.out.println(materialize);
            System.out.println(materializeJobService.deleteSubTable(materialize.getSubTableId()));

        });
    }

    private static List<Arguments> descriptionProvider() {
        return Arrays.asList(
                Arguments.arguments(
                         "table",
                         "{\n" +
                                "      \"dataSource\": \"dataflow.airuuid\",\n" +
                                "      \"filter\": \"\",\n" +
                                "      \"group\": [],\n" +
                                "      \"jobType\": \"table\",\n" +
                                "      \"limit\": 200,\n" +
                                "      \"project\": [\"city\",\"time\",\"AQI\"]\n" +
                                "    }"
                ),
                Arguments.arguments(
                        "transpose",
                        "{\n" +
                                "      \"column\":\"city\",\n" +
                                "      \"columnIsNumeric\":false,\n" +
                                "      \"groupBy\":[\"time\"],\n" +
                                "      \"attributeWithAggregationMap\":{\n" +
                                "        \"PM2_5\":\"sum\"\n" +
                                "      },\n" +
                                "      \"jobType\": \"transpose\",\n" +
                                "      \"datasource\": \"dataflow.airuuid\",\n" +
                                "      \"limit\": 2000\n" +
                                "    }"
                ),
                Arguments.arguments(
                        "join",
                        "{\n" +
                                "      \"jobType\": \"join\",\n" +
                                "      \"leftDataSource\": \"dataflow.student\",\n" +
                                "      \"rightDataSource\": \"dataflow.class\",\n" +
                                "      \"joinType\": \"left join\",\n" +
                                "      \"joinKeys\": [{\n" +
                                "        \"left\": \"classId\",\n" +
                                "        \"right\": \"id\"\n" +
                                "      }],\n" +
                                "      \"includePrefixes\": \"true\",\n" +
                                "      \"leftPrefix\": \"left_\",\n" +
                                "      \"rightPrefix\": \"right_\"\n" +
                                "    }"
                )
        );
    }
}