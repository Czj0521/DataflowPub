package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.Menu;
import com.bdilab.dataflow.dto.jobdescription.PivotChartDescription;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : [zhangpeiliang]
 * @description : [Test PivotChartSqlGenerator]
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.bdilab.dataflow.PivotChartTestApplication.class)
@Slf4j
public class PivotChartSqlGeneratorTest {

    private static final PivotChartDescription description = new PivotChartDescription();

    /**
     * 生成sql
     * 正常情况
     */
    @Test
    public void testGenerateSql_Success() {
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
        PivotChartSqlGenerator pivotChartSqlGenerator = new PivotChartSqlGenerator(description);

        //模拟联动时传来的brushFilters集合
        List<String> brushFilters = new ArrayList<>();
        brushFilters.add("Model = 'Model A Sport' and Type = 'Sedan' or Model = 'Model B FWD' and Type = 'SUV' or Model = 'Model C Standard' and Type = 'HATCHBACK'");
        brushFilters.add("Model = 'Model A Sport' and Type = 'Sedan' or Model = 'Model A Standard' and Type = 'Sedan'");

        String sql = pivotChartSqlGenerator.generateSql(brushFilters);

        assert sql.equals("select * from (" +
                "WITH Model = 'Model A Sport' and Type = 'Sedan'" +
                " or Model = 'Model B FWD' and Type = 'SUV'" +
                " or Model = 'Model C Standard' and Type = 'HATCHBACK' AS brush1" +
                ",Model = 'Model A Sport' and Type = 'Sedan'" +
                " or Model = 'Model A Standard' and Type = 'Sedan' AS brush2" +
                " select roundDown(DealerId,[0.0, 50.0, 100.0, 150.0, 200.0, 250.0, 300.0, 350.0, 400.0, 450.0, 500.0, 550.0, 600.0, 650.0, 700.0, 750.0, 800.0, 850.0]) DealerId_bin" +
                ",roundDown(CarMileage,[0.0, 4580.0, 8013.0, 11433.0, 14916.0, 18506.0, 22262.0, 26242.0, 30499.0, 35089.0, 40092.0, 45684.0, 52166.0, 60073.0, 70859.0, 106806.0]) CarMileage_bin" +
                ",date_trunc('month', SalesDate) AS SalesDate_bin,date_add(month, 1, date_trunc('month', SalesDate)) AS SalesDate_bin_RIGHT" +
                ",substringUTF8(Type,1,1) Type_bin" +
                ",CASE WHEN brush1 AND brush2 THEN 'overlap'" +
                " WHEN brush1 THEN 'brush1' WHEN brush2 THEN 'brush2'" +
                " ELSE 'rest' END AS brush" +
                ",count(Model) Model_count from dataflow.car" +
                " group by DealerId_bin,CarMileage_bin,SalesDate_bin,Type_bin,brush" +
                " order by Model_count Desc)" +
                " where mod(rowNumberInAllBlocks(),104)=1");
    }

    /**
     * 菜单不做任何操作
     * 生成空字符串
     */
    @Test
    public void testGenerateSql_Empty() {
        description.setDataSource(new String[] {"dataflow.car"});
        description.setJobType("chart");
        description.setLimit(-1);
        description.setFilter("");
        description.setOnlyUpdateFilter(false);

        Menu xAxis = new Menu();
        xAxis.setMenu("x-axis");
        xAxis.setAttribute("none");
        xAxis.setBinning("none");
        xAxis.setAggregation("none");
        xAxis.setSort("none");

        Menu yAxis = new Menu();
        yAxis.setMenu("y-axis");
        yAxis.setAttribute("none");
        yAxis.setBinning("none");
        yAxis.setAggregation("none");
        yAxis.setSort("none");

        Menu color = new Menu();
        color.setMenu("color");
        color.setAttribute("none");
        color.setBinning("none");
        color.setAggregation("none");
        color.setSort("none");

        Menu size = new Menu();
        size.setMenu("size");
        size.setAttribute("none");
        size.setBinning("none");
        size.setAggregation("none");
        size.setSort("none");

        Menu row = new Menu();
        row.setMenu("row");
        row.setAttribute("none");
        row.setBinning("none");
        row.setAggregation("none");
        row.setSort("none");

        Menu column = new Menu();
        column.setMenu("column");
        column.setAttribute("none");
        column.setBinning("none");
        column.setAggregation("none");
        column.setSort("none");

        description.setMenus(new Menu[] {xAxis, yAxis, color, size, row, column});
        PivotChartSqlGenerator pivotChartSqlGenerator = new PivotChartSqlGenerator(description);

        //无传入的brush filters集合，尝试生成sql
        String sql = pivotChartSqlGenerator.generateSql(null);
        assert sql.equals("");
    }

    /**
     * 多个菜单只选择相同属性
     * 生成只包含一个属性的sql
     */
    @Test
    public void testGenerateSql_OnlyOneAttribute() {
        description.setDataSource(new String[] {"dataflow.car"});
        description.setJobType("chart");
        description.setLimit(-1);
        description.setFilter("");
        description.setOnlyUpdateFilter(false);

        Menu xAxis = new Menu();
        xAxis.setMenu("x-axis");
        xAxis.setAttribute("Model");
        xAxis.setBinning("none");
        xAxis.setAggregation("none");
        xAxis.setSort("none");

        Menu yAxis = new Menu();
        yAxis.setMenu("y-axis");
        yAxis.setAttribute("Model");
        yAxis.setBinning("none");
        yAxis.setAggregation("none");
        yAxis.setSort("none");

        Menu color = new Menu();
        color.setMenu("color");
        color.setAttribute("Model");
        color.setBinning("none");
        color.setAggregation("none");
        color.setSort("none");

        Menu size = new Menu();
        size.setMenu("size");
        size.setAttribute("Model");
        size.setBinning("none");
        size.setAggregation("none");
        size.setSort("none");

        Menu row = new Menu();
        row.setMenu("row");
        row.setAttribute("Model");
        row.setBinning("none");
        row.setAggregation("none");
        row.setSort("none");

        Menu column = new Menu();
        column.setMenu("column");
        column.setAttribute("Model");
        column.setBinning("none");
        column.setAggregation("none");
        column.setSort("none");

        description.setMenus(new Menu[] {xAxis, yAxis, color, size, row, column});
        PivotChartSqlGenerator pivotChartSqlGenerator = new PivotChartSqlGenerator(description);

        //无传入的brush filters集合，尝试生成sql
        String sql = pivotChartSqlGenerator.generateSql(null);
        assert sql.equals("select Model from dataflow.car group by Model order by Model");
    }

    /**
     * 每个菜单根据选择的属性，聚合，分箱，得到3种重命名
     * 1.单一属性（和原属性名一致，如原菜单只选择了属性Model，重命名也是Model）
     * 2.属性_聚合名（如Model_count表示原菜单中，属性选择了Model，聚合选择了count）
     * 3.属性_bin（这里bin代表分箱，如原菜单中，属性选了Model，分箱选了AlphabeticBinning，重命名为Model_bin）
     *
     * 这里测试多个菜单选择相同属性，分箱不同的sql生成情况
     * 结果只保留row菜单最后一个属性_bin
     *
     * 规则：
     * 1.后面的菜单覆盖前面菜单，但除了row菜单，row菜单优先级最高
     * 2.菜单顺序默认为x-axis、y-axis、color、size、row、column
     * 3.菜单优先级升序为x-axis、y-axis、color、size、column、row
     */
    @Test
    public void testGenerateSql_OnlyOneAttributeBIN() {
        description.setDataSource(new String[] {"dataflow.car"});
        description.setJobType("chart");
        description.setLimit(-1);
        description.setFilter("");
        description.setOnlyUpdateFilter(false);

        //x菜单选择销售时间+秒分箱
        Menu xAxis = new Menu();
        xAxis.setMenu("x-axis");
        xAxis.setAttribute("SalesDate");
        xAxis.setBinning("second");
        xAxis.setAggregation("none");
        xAxis.setSort("none");

        //y菜单选择销售时间+分钟分箱
        Menu yAxis = new Menu();
        yAxis.setMenu("y-axis");
        yAxis.setAttribute("SalesDate");
        yAxis.setBinning("minute");
        yAxis.setAggregation("none");
        yAxis.setSort("none");

        //color菜单选择销售时间+小时分箱
        Menu color = new Menu();
        color.setMenu("color");
        color.setAttribute("SalesDate");
        color.setBinning("hour");
        color.setAggregation("none");
        color.setSort("none");

        //size菜单选择销售时间+天分箱
        Menu size = new Menu();
        size.setMenu("size");
        size.setAttribute("SalesDate");
        size.setBinning("day");
        size.setAggregation("none");
        size.setSort("none");

        //row菜单选择销售时间+月分箱
        Menu row = new Menu();
        row.setMenu("row");
        row.setAttribute("SalesDate");
        row.setBinning("month");
        row.setAggregation("none");
        row.setSort("none");

        //column菜单选择销售时间+年分箱
        Menu column = new Menu();
        column.setMenu("column");
        column.setAttribute("SalesDate");
        column.setBinning("year");
        column.setAggregation("none");
        column.setSort("none");

        description.setMenus(new Menu[] {xAxis, yAxis, color, size, row, column});
        PivotChartSqlGenerator pivotChartSqlGenerator = new PivotChartSqlGenerator(description);

        //无传入的brush filters集合，尝试生成sql
        String sql = pivotChartSqlGenerator.generateSql(null);
        assert sql.equals("select date_trunc('month', SalesDate) AS SalesDate_bin" +
                ",date_add(month, 1, date_trunc('month', SalesDate)) AS SalesDate_bin_RIGHT" +
                " from dataflow.car group by SalesDate_bin order by SalesDate_bin");
    }

    /**
     * X轴菜单或Y轴菜单属性+count/属性+distinct count的百分比
     * 生成的sql中包含百分比
     */
    @Test
    public void testGenerateSql_Percentage() {
        description.setDataSource(new String[] {"dataflow.car"});
        description.setJobType("chart");
        description.setLimit(-1);
        description.setFilter("");
        description.setOnlyUpdateFilter(false);

        //x菜单选择属性+count/属性+distinct count，这里选择属性+count
        Menu xAxis = new Menu();
        xAxis.setMenu("x-axis");
        xAxis.setAttribute("Model");
        xAxis.setBinning("none");
        xAxis.setAggregation("count");
        xAxis.setSort("none");

        //y菜单不再选择聚合函数
        Menu yAxis = new Menu();
        yAxis.setMenu("y-axis");
        yAxis.setAttribute("Type");
        yAxis.setBinning("none");
        yAxis.setAggregation("none");
        yAxis.setSort("none");

        //color菜单随便选
        Menu color = new Menu();
        color.setMenu("color");
        color.setAttribute("CarYear");
        color.setBinning("none");
        color.setAggregation("none");
        color.setSort("none");

        //size菜单随便选
        Menu size = new Menu();
        size.setMenu("size");
        size.setAttribute("ExteriorColor");
        size.setBinning("none");
        size.setAggregation("none");
        size.setSort("none");

        //row菜单不选，否则无百分比
        Menu row = new Menu();
        row.setMenu("row");
        row.setAttribute("none");
        row.setBinning("none");
        row.setAggregation("none");
        row.setSort("none");

        //column菜单不选，否则无百分比
        Menu column = new Menu();
        column.setMenu("column");
        column.setAttribute("none");
        column.setBinning("none");
        column.setAggregation("none");
        column.setSort("none");

        description.setMenus(new Menu[] {xAxis, yAxis, color, size, row, column});
        PivotChartSqlGenerator pivotChartSqlGenerator = new PivotChartSqlGenerator(description);

        //无传入的brush filters集合，尝试生成sql
        String sql = pivotChartSqlGenerator.generateSql(null);

        assert sql.equals("WITH (select sum(Model_count) from" +
                " (select Type,ExteriorColor,CarYear,count(Model) Model_count from dataflow.car" +
                " group by Type,ExteriorColor,CarYear order by Type,ExteriorColor,CarYear)) AS S" +
                " select Type,ExteriorColor,CarYear,count(Model) Model_count,round(Model_count/S*100,2) \"%\"" +
                " from dataflow.car group by Type,ExteriorColor,CarYear order by Type,ExteriorColor,CarYear");
    }

}
