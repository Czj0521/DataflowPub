package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.dto.jobdescription.FilterDescription;
import com.bdilab.dataflow.sql.generator.FilterSqlGenerator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * What-If: Test Filter Job ServiceImpl.
 *
 * @author: wh
 * @create: 2021-12-22
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class FilterJobServiceImplTest {

  @Test
  public void testGenerateDataSourceSql() {
    String profilerJson = "{\n" +
        "    \"dataSource\": [\"dataflow.airuuid\"],\n" +
        "    \"filter\": \"AQI > 30 or AQI <10\",\n" +
        "    \"jobType\": \"\",\n" +
        "    \"limit\": 100,\n" +
        "}";
    FilterDescription filterDescription = JSONObject.parseObject(profilerJson, FilterDescription.class);
    FilterSqlGenerator filterSqlGenerator = new FilterSqlGenerator(filterDescription);
    String generate = filterSqlGenerator.generate();
    if ("SELECT *  FROM dataflow.airuuid WHERE AQI > 30 or AQI <10".equals(generate)) {
      log.info("Test - Pass： testGenerateDataSourceSql.");
    } else {
      throw new RuntimeException("Test - Error： testGenerateDataSourceSql - Generate sql error !");
    }
  }
}