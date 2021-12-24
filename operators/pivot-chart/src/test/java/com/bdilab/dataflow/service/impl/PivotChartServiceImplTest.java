package com.bdilab.dataflow.service.impl;

import com.bdilab.dataflow.dto.jobdescription.Menu;
import com.bdilab.dataflow.dto.jobdescription.PivotChartDescription;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : [zhangpeiliang]
 * @description : [Test PivotChartServiceImpl]
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.bdilab.dataflow.PivotChartTestApplication.class)
@Slf4j
public class PivotChartServiceImplTest {

    @Autowired
    private PivotChartServiceImpl pivotChartService;

    private static final PivotChartDescription description = new PivotChartDescription();

    /**
     * 测试联动场景执行sql查询方法
     */
    @Test
    public void testSaveToClickHouse() {
        description.setDataSource(new String[] {"dataflow.car"});
        description.setJobType("chart");
        description.setLimit(-1);
        description.setFilter("");
        description.setOnlyUpdateFilter(false);

        //测试聚合+排序
        Menu xAxis = new Menu();
        xAxis.setMenu("x-axis");
        xAxis.setAttribute("Model");
        xAxis.setBinning("none");
        xAxis.setAggregation("count");
        xAxis.setSort("Desc");

        //测试字符分箱
        Menu yAxis = new Menu();
        yAxis.setMenu("y-axis");
        yAxis.setAttribute("Type");
        yAxis.setBinning("AlphabeticBinning");
        yAxis.setAggregation("none");
        yAxis.setSort("none");

        //联动更改边为带颜色边时，color菜单不显示，默认显示brush字段，这里全设为none，测试brush
        Menu color = new Menu();
        color.setMenu("color");
        color.setAttribute("none");
        color.setBinning("none");
        color.setAggregation("none");
        color.setSort("none");

        //测试numeric类型数据的等宽分箱
        Menu size = new Menu();
        size.setMenu("size");
        size.setAttribute("DealerId");
        size.setBinning("EquiWidthBinning");
        size.setAggregation("none");
        size.setSort("none");

        //测试numeric类型数据的自然分箱
        Menu row = new Menu();
        row.setMenu("row");
        row.setAttribute("CarMileage");
        row.setBinning("NaturalBinning");
        row.setAggregation("none");
        row.setSort("none");

        //测试日期类型数据的时间分箱(包含多种，这里按月分箱)
        Menu column = new Menu();
        column.setMenu("column");
        column.setAttribute("SalesDate");
        column.setBinning("month");
        column.setAggregation("none");
        column.setSort("none");

        description.setMenus(new Menu[] {xAxis, yAxis, color, size, row, column});

        //模拟联动时传来的brushFilters集合
        List<String> brushFilters = new ArrayList<>();
        brushFilters.add("Model = 'Model A Sport' and Type = 'Sedan' or Model = 'Model B FWD' and Type = 'SUV' or Model = 'Model C Standard' and Type = 'HATCHBACK'");
        brushFilters.add("Model = 'Model A Sport' and Type = 'Sedan' or Model = 'Model A Standard' and Type = 'Sedan'");

        try {
            pivotChartService.saveToClickHouse(description, brushFilters);
            log.info("Tests - passed： testSaveToClickHouse.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
