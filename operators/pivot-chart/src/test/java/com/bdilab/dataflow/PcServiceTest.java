package com.bdilab.dataflow;

import com.alibaba.fastjson.JSON;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.bdilab.dataflow.dto.jobdescription.Menu;
import com.bdilab.dataflow.dto.jobdescription.PivotChartDescription;
import com.bdilab.dataflow.service.impl.PivotChartServiceImpl;
import com.bdilab.dataflow.sql.generator.PivotChartSqlGenerator;
import java.text.MessageFormat;
import java.util.List;

import com.bdilab.dataflow.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: Guo Yongqiang
 * @date: 2021/11/23 15:50
 */

@Slf4j
/*@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.bdilab.dataflow.PivotChartTestApplication.class)*/
public class PcServiceTest {

  private PivotChartDescription pivotChartDescription = new PivotChartDescription();

  @Autowired
  private PivotChartServiceImpl pivotChartService;



  /*@Test
  public void service() {
    pivotChartDescription.setDataSource(new String[]{"dataflow.airuuid"});
    pivotChartDescription.setJobType("pivot-chart");
    pivotChartDescription.setLimit(100);
    Menu xaxis = new Menu();
    xaxis.setAttribute("primary_pollutant");
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



    R res = pivotChartService.getPivotChart(pivotChartDescription);
    log.info(MessageFormat.format("[Pivot Chart Response]: {0}", res));
  }*/

 /* @Test
  public void datetimeService() {
    pivotChartDescription.setDataSource(new String[]{"dataflow.airuuid"});
    pivotChartDescription.setJobType("pivot-chart");
    //    pivotChartDescription.setLimit(100);
    Menu xaxis = new Menu();
    xaxis.setAttribute("time");
    xaxis.setMenu("x-axis");
    xaxis.setBinning("day");
    xaxis.setAggregation("none");
    xaxis.setSort("none");

    Menu yaxis = new Menu();
    yaxis.setAttribute("AQI");
    yaxis.setMenu("y-axis");
    yaxis.setAggregation("none");
    yaxis.setBinning("none");
    yaxis.setSort("none");

    Menu[] menus = new Menu[] {xaxis, yaxis};
    pivotChartDescription.setMenus(menus);

    R res = pivotChartService.getPivotChart(pivotChartDescription);
    log.info(MessageFormat.format("[Pivot Chart Response]: {0}", res));
    log.info("\n" + JSON.toJSONString(res, true));
  }*/
}


