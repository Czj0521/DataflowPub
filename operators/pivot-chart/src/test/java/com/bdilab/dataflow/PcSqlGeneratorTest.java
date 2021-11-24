package com.bdilab.dataflow;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.bdilab.dataflow.dto.jobdescription.Menu;
import com.bdilab.dataflow.dto.jobdescription.PivotChartDescription;
import com.bdilab.dataflow.service.impl.PivotChartServiceImpl;
import com.bdilab.dataflow.sql.generator.PivotChartSqlGenerator;
import com.fasterxml.jackson.databind.util.ArrayBuilders;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.bridge.Message;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

/**
 * @author: Guo Yongqiang
 * @date: 2021/11/22 16:56
 * @version:
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PivotChartTestApplication.class)
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

    Menu xAxis = new Menu();

    pivotChartDescription.setDataSource("dataflow.airuuid");
    pivotChartDescription.setJobType("pivot-chart");
    pivotChartDescription.setLimit(100);

    xAxis.setAttribute("city");
    xAxis.setMenu("x-axis");
    xAxis.setBinning("AlphabeticBinning");
    xAxis.setSort("Asc");
    xAxis.setAggregation("none");


    Menu yAxis = new Menu();
    yAxis.setAttribute("AQI");
    yAxis.setMenu("y-axis");
    yAxis.setAggregation("average");
    yAxis.setBinning("none");
    yAxis.setSort("none");

    Menu[] menus = new Menu[] {xAxis, yAxis};
    pivotChartDescription.setMenus(menus);

    pivotChartSqlGenerator = new PivotChartSqlGenerator(pivotChartDescription);

    System.out.println(pivotChartDescription);
    System.out.println(pivotChartSqlGenerator);

    String sql = pivotChartSqlGenerator.generate();
    log.info(MessageFormat.format("[Pivot Chart SQL]: {0}", sql));
  }

  @Test
  public void datetimeBinning() {
    Menu xAxis = new Menu();

    pivotChartDescription.setDataSource("dataflow.airuuid");
    pivotChartDescription.setJobType("pivot-chart");
    pivotChartDescription.setLimit(100);

    xAxis.setAttribute("time");
    xAxis.setMenu("x-axis");
    xAxis.setBinning("day");
    xAxis.setSort("Asc");
    xAxis.setAggregation("none");


    Menu yAxis = new Menu();
    yAxis.setAttribute("AQI");
    yAxis.setMenu("y-axis");
    yAxis.setAggregation("average");
    yAxis.setBinning("none");
    yAxis.setSort("none");

    Menu[] menus = new Menu[] {xAxis, yAxis};
    pivotChartDescription.setMenus(menus);

    pivotChartSqlGenerator = new PivotChartSqlGenerator(pivotChartDescription);

    System.out.println(pivotChartDescription);
    System.out.println(pivotChartSqlGenerator);

    String sql = pivotChartSqlGenerator.generate();
    log.info(MessageFormat.format("[Pivot Chart SQL]: {0}", sql));
  }


}
