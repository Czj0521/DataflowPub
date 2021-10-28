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
    public void groupTest() {
       assertEquals(transposeSQLGenerator.group()," GROUP BY name");
    }
    @Test
    public void limitTest() {
        assertEquals(transposeSQLGenerator.limit()," LIMIT 3000");
    }
}
