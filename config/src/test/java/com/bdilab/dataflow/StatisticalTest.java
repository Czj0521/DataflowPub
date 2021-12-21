package com.bdilab.dataflow;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.StatisticalTestDescription;
import com.bdilab.dataflow.service.impl.StatisticalTestServiceImpl;
import com.bdilab.dataflow.utils.dag.DagNode;
import com.bdilab.dataflow.utils.dag.dto.DagNodeInputDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StatisticalTest {

    @Autowired
    StatisticalTestServiceImpl statisticalTestService;


    @ParameterizedTest
    @MethodSource("aggregateProvider")
    public void test1(StatisticalTestDescription statisticalTestDescription){
//        StatisticalTestDescription statisticalTestDescription = new StatisticalTestDescription();
//        statisticalTestDescription.setDataSource(new String[]{"dataflow.promotion_csv","dataflow.promotion_csv"});
//        statisticalTestDescription.setTest("Age");
//        statisticalTestDescription.setControl("LeaseLength");
//        statisticalTestDescription.setType("numerical");

        DagNodeInputDto dagNodeInputDto = new DagNodeInputDto();
        dagNodeInputDto.setNodeDescription(JSONObject.toJSON(statisticalTestDescription));

        DagNode dagNode = new DagNode(dagNodeInputDto);
//        System.out.println(dagNode);
        Map<String, Object> result = statisticalTestService.getPValue(dagNode);
        System.out.println("result : \n" + result);

    }

    private static List<Arguments> aggregateProvider() {
        return Arrays.<Arguments>asList(
                //TTEST
                Arguments.arguments(
                        new StatisticalTestDescription(){
                            {
                                setDataSource(new String[]{"dataflow.promotion_csv","dataflow.promotion_csv"});
                                setTest("Age");
                                setControl("LeaseLength");
                                setType("numerical");

                            }
                        }
                ),
                //CHISQUARED  num
                Arguments.arguments(
                        new StatisticalTestDescription(){
                            {
                                setDataSource(new String[]{"dataflow.promotion_csv","dataflow.promotion_csv"});
                                setTest("Age");
                                setControl("LeaseLength");
                                setType("categorical");

                            }
                        }
                ),
                //CHISQUARED  string
                Arguments.arguments(
                        new StatisticalTestDescription(){
                            {
                                setDataSource(new String[]{"( select * from dataflow.promotion_csv where Married != \'single\') a0","( select * from dataflow.promotion_csv where Married = \'single\') a1"});
                                setTest("EducationLevel");
                                setControl("EducationLevel");
                                setType("categorical");

                            }
                        }
                )
        );
    }

}
