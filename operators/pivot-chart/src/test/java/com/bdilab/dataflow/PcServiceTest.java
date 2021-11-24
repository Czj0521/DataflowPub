package com.bdilab.dataflow;

import com.alibaba.fastjson.JSON;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.bdilab.dataflow.dto.jobdescription.Menu;
import com.bdilab.dataflow.dto.jobdescription.PivotChartDescription;
import com.bdilab.dataflow.service.impl.PivotChartServiceImpl;
import com.bdilab.dataflow.sql.generator.PivotChartSqlGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.MessageFormat;
import java.util.List;

/**
 * @author: Guo Yongqiang
 * @date: 2021/11/23 15:50
 * @version:
 */

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PivotChartTestApplication.class)
public class PcServiceTest {

  private PivotChartDescription pivotChartDescription = new PivotChartDescription();

  @Autowired
  private PivotChartServiceImpl pivotChartService;

  @Test
  public void service() {

    Menu xAxis = new Menu();

    pivotChartDescription.setDataSource("dataflow.airuuid");
    pivotChartDescription.setJobType("pivot-chart");
    pivotChartDescription.setLimit(100);

    xAxis.setAttribute("primary_pollutant");
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



    List<Object> res = pivotChartService.getPivotChart(pivotChartDescription);
    log.info(MessageFormat.format("[Pivot Chart Response]: {0}", res));
  }

  @Test
  public void datetimeService() {

    Menu xAxis = new Menu();

    pivotChartDescription.setDataSource("dataflow.airuuid");
    pivotChartDescription.setJobType("pivot-chart");
//    pivotChartDescription.setLimit(100);

    xAxis.setAttribute("time");
    xAxis.setMenu("x-axis");
    xAxis.setBinning("day");
    xAxis.setAggregation("none");
    xAxis.setSort("none");


    Menu yAxis = new Menu();
    yAxis.setAttribute("AQI");
    yAxis.setMenu("y-axis");
    yAxis.setAggregation("none");
    yAxis.setBinning("none");
    yAxis.setSort("none");

    Menu[] menus = new Menu[] {xAxis, yAxis};
    pivotChartDescription.setMenus(menus);



    List<Object> res = pivotChartService.getPivotChart(pivotChartDescription);
    log.info(MessageFormat.format("[Pivot Chart Response]: {0}", res));
    log.info("\n" + JSON.toJSONString(res, true));
  }
}


