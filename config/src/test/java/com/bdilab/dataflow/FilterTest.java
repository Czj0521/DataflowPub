package com.bdilab.dataflow;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.jobdescription.FilterDescription;
import com.bdilab.dataflow.sql.generator.FilterSqlGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * UT of the filter operator.
 *
 * @author: wh
 * @create: 2021-10-30
 */
@SpringBootTest
public class FilterTest {
  @Test
  public void testFilter() {
    String profilerJson = "{\n" +
        "    \"dataSource\": \"dataflow.airuuid\",\n" +
        "    \"filter\": \"AQI > 30 or AQI <10\",\n" +
        "    \"jobType\": \"\",\n" +
        "    \"limit\": 100,\n" +
        "}";
    FilterDescription filterDescription = JSONObject.parseObject(profilerJson, FilterDescription.class);
    FilterSqlGenerator filterSqlGenerator = new FilterSqlGenerator(filterDescription);
    System.out.println(filterSqlGenerator.generate());
  }
}
