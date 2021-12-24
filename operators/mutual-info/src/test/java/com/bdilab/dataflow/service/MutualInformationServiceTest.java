package com.bdilab.dataflow.service;

import com.bdilab.dataflow.MiTestApplication;
import com.bdilab.dataflow.dto.jobdescription.MutualInformationDescription;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author: Guo Yongqiang
 * @date: 2021/12/19 22:27
 * @version:
 */

@SpringBootTest(classes = MiTestApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MutualInformationServiceTest {
  @Autowired
  private MutualInformationService mutualInformationService;

  private static MutualInformationDescription description;
  static {
    description = new MutualInformationDescription();
    description.setJobType("mutual information");
  }

  public static MutualInformationDescription getCopy() {
    MutualInformationDescription desc = new MutualInformationDescription();
    desc.setJobType(description.getJobType());
    desc.setDataSource(description.getDataSource());
    desc.setTarget(description.getTarget());
    desc.setFeatures(description.getFeatures());
    return desc;
  }

  @Test
  public void execute_TargetConstant() {
    MutualInformationDescription description = getCopy();
    description.setDataSource(new String[] {"(select arrayJoin([0]) AS x, number AS y from numbers(20))"});
    description.setTarget("x");
    description.setFeatures(new String[] {"y"});

    List<Map<String, Object>> data = mutualInformationService.getMutualInformation(description);

    assertThat(data.get(0)).containsKey("msg").containsValue("Target variable is constant!");
  }



  @ParameterizedTest
  @MethodSource("discreteDatasource")
  public void execute_Success(String datasource, double res1, double res2) {
    String ds;
    if (datasource.startsWith("select")) {
      ds = MessageFormat.format("({0})", datasource);
    }
    else {
      ds = datasource;
    }
    MutualInformationDescription desc = getCopy();
    desc.setDataSource(new String[]{ds});
    desc.setTarget("x");
    desc.setFeatures(new String[]{"y"});

    List<Map<String, Object>> l1 = mutualInformationService.getMutualInformation(desc);
    double r1 = (double) l1.get(1).get("y");
    Assertions.assertEquals(r1, res1,  Math.abs(res1 - r1));
    System.out.println(l1);
    desc.setTarget("y");
    desc.setFeatures(new String[]{"x"});
    List<Map<String, Object>> l2 = mutualInformationService.getMutualInformation(desc);
    double r2 = (double) l2.get(1).get("x");
    Assertions.assertEquals(r2, res2,  Math.abs(res2 - r2));
    System.out.println(l2);
  }

  public static List<Arguments> discreteDatasource() {

    return Arrays.asList(
        Arguments.arguments("select number AS x, number + 1 AS y from numbers(20)", 1, 1),
        Arguments.arguments("select Model AS x, Type AS y from operator_mi.car_sales", 0.624, 1),
        Arguments.arguments("select Married AS x, EducationalLevel AS y from operator_mi.promotion", 0.0013, 0.000542),
        Arguments.arguments("operator_mi.pointwise_mutual_information", 0.297, 0.264)
    );
  }

}
