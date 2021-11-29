package com.bdilab.dataflow;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.bdilab.dataflow.dto.jobdescription.Menu;
import com.bdilab.dataflow.dto.jobdescription.PivotChartDescription;
import com.bdilab.dataflow.service.impl.PivotChartServiceImpl;
import com.bdilab.dataflow.sql.generator.PivotChartSqlGenerator;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.bridge.Message;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: Guo Yongqiang
 * @date: 2021/11/22 16:56
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.bdilab.dataflow.PivotChartTestApplication.class)
public class PcSqlGeneratorTest {
  private static PivotChartDescription pivotChartDescription = new PivotChartDescription();
  private static PivotChartSqlGenerator pivotChartSqlGenerator;

  @Autowired
  private PivotChartServiceImpl pivotChartService;

  @BeforeAll
  public static void initAll() {
  }

  @Test
  public void sqlGeneration() {
    pivotChartDescription.setDataSource(new String[]{"dataflow.airuuid"});
    pivotChartDescription.setJobType("pivot-chart");
    pivotChartDescription.setLimit(100);
    Menu xaxis = new Menu();
    xaxis.setAttribute("city");
    xaxis.setMenu("x-axis");
    xaxis.setBinning("AlphabeticBinning");
    xaxis.setSort("Asc");
    xaxis.setAggregation("none");

    Menu yaxis = new Menu();
    yaxis.setAttribute("AQI");
    yaxis.setMenu("y-axis");
    yaxis.setAggregation("average");
    yaxis.setBinning("none");
    yaxis.setSort("none");

    Menu[] menus = new Menu[] {xaxis, yaxis};
    pivotChartDescription.setMenus(menus);

    pivotChartSqlGenerator = new PivotChartSqlGenerator(pivotChartDescription);

    System.out.println(pivotChartDescription);
    System.out.println(pivotChartSqlGenerator);

    String sql = pivotChartSqlGenerator.generate();
    log.info(MessageFormat.format("[Pivot Chart SQL]: {0}", sql));
  }

  @Test
  public void datetimeBinning() {
    pivotChartDescription.setDataSource(new String[]{"dataflow.airuuid"});
    pivotChartDescription.setJobType("pivot-chart");
    pivotChartDescription.setLimit(100);
    Menu xaxis = new Menu();
    xaxis.setAttribute("time");
    xaxis.setMenu("x-axis");
    xaxis.setBinning("day");
    xaxis.setSort("Asc");
    xaxis.setAggregation("none");

    Menu yaxis = new Menu();
    yaxis.setAttribute("AQI");
    yaxis.setMenu("y-axis");
    yaxis.setAggregation("average");
    yaxis.setBinning("none");
    yaxis.setSort("none");

    Menu[] menus = new Menu[] {xaxis, yaxis};
    pivotChartDescription.setMenus(menus);

    pivotChartSqlGenerator = new PivotChartSqlGenerator(pivotChartDescription);

    System.out.println(pivotChartDescription);
    System.out.println(pivotChartSqlGenerator);

    String sql = pivotChartSqlGenerator.generate();
    log.info(MessageFormat.format("[Pivot Chart SQL]: {0}", sql));
  }


}
