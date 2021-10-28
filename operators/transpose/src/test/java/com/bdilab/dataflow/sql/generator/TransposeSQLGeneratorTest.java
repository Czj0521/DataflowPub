package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.TransposeDescription;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author: Zunjing Chen
 * @create: 2021-10-25
 * @description:
 **/
public class TransposeSQLGeneratorTest {
    TransposeSQLGenerator transposeSQLGenerator;
    @Before
    public void init(){
        List<String> columnValues = Lists.newArrayList("W","M");
        TransposeDescription transposeDescription = new TransposeDescription("transpose","data.csv",3000);
        transposeDescription.setColumnIsNumeric(false);
        transposeDescription.setColumn("sex");
        transposeDescription.setGroupBy(new String[]{"name"});
        transposeDescription.setAttributeWithAggregationMap(ImmutableMap.<String,String>builder().put("age","avg").build());
        transposeSQLGenerator = new TransposeSQLGenerator(transposeDescription,columnValues);
    }

    @Test
    public void appendGroupColumnsTest() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        Whitebox.invokeMethod(transposeSQLGenerator, "appendGroupColumns",stringBuilder);
        assertEquals(stringBuilder.toString(), "name,");
    }
    @Test
    public void appendColumnsTest() throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        Whitebox.invokeMethod(transposeSQLGenerator, "appendColumns",stringBuilder);
        String re = stringBuilder.toString();
        // 列名正确性
        assertTrue(re.contains("W_avg_age"));
        assertTrue(re.contains("'W'"));
        assertTrue(re.contains("avgIf"));
    }
    @Test
    public void airTableTest(){
        TransposeDescription transposeDescription = new TransposeDescription("transpose","airuuid",3000);
        transposeDescription.setColumnIsNumeric(false);
        transposeDescription.setColumn("city");
        transposeDescription.setGroupBy(new String[]{"time"});
        transposeDescription.setAttributeWithAggregationMap(ImmutableMap.<String,String>builder().put("PM2_5","sum").build());
        List<String> columnValues = Lists.newArrayList("杭州","南京");
        TransposeSQLGenerator transposeSQLGenerator = new TransposeSQLGenerator(transposeDescription,columnValues);
        assertEquals(transposeSQLGenerator.generate(),"SELECT time,sumIf(PM2_5,city = '杭州' )  AS `杭州_sum_PM2_5` ,sumIf(PM2_5,city = '南京' )  AS `南京_sum_PM2_5`  FROM airuuid GROUP BY time LIMIT 3000");
    }
    @Test
    public void groupTest() {
       assertEquals(transposeSQLGenerator.group()," GROUP BY name");
    }
    @Test
    public void limitTest() {
        assertEquals(transposeSQLGenerator.limit()," LIMIT 3000");
    }
}
