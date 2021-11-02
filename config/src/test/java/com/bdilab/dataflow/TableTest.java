package com.bdilab.dataflow;

import com.alibaba.fastjson.JSON;
import com.bdilab.dataflow.dto.jobdescription.TableDescription;
import com.bdilab.dataflow.service.impl.TableJobServiceImpl;
import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springfox.documentation.spring.web.json.Json;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @description: UT of the Table operator
 * @author:zhb
 * @createTime:2021/10/25 12:28
 */

@SpringBootTest
public class TableTest {
  @Autowired
  TableJobServiceImpl tableJobService;

  @Test
  public void table() {
    TableDescription tableDescription = new TableDescription();
    tableDescription.setDataSource("dataflow.airuuid");
    tableDescription.setFilter("");
    tableDescription.setGroup(new String[]{});
    tableDescription.setJobType("");
    tableDescription.setLimit(20);
    tableDescription.setProject(new String[]{"city"});

    long before = System.currentTimeMillis();
    System.out.println(tableJobService.table(tableDescription));
    long after = System.currentTimeMillis();

    System.out.println("Time spent :" + (after - before));
  }

  // filter function test
  @ParameterizedTest
  @ValueSource(strings = {
      "match(primary_pollutant, 'PM2[\\._]?5')",
      "AQI >= 100",
      "time >= toDateTime('2014-08-14')",
      "match(primary_pollutant, 'PM2[\\._]?5') and AQI > 100 and time >= toDateTime('2014-08-14')",
      "1",
      "0"
//      , "where group by"    // SQL injection detected, java.sql.SQLException: sql injection violation
  })
  public void tableFilter(String filterStm) {
    TableDescription td = new TableDescription();
    td.setDataSource("dataflow.airuuid");
    td.setProject(new String[]{"*"});
    td.setGroup(new String[]{});
    td.setLimit(10);
    td.setJobType("");
    td.setFilter(filterStm);

    assertDoesNotThrow(() -> {
      System.out.println(JSON.toJSONString(td));
      System.out.println(tableJobService.table(td));
    });
  }


  // aggregate function test
  @ParameterizedTest
  @MethodSource("aggregateProvider")
  public void tableAggregate(String[] groups, String[] aggs) {

    TableDescription td = new TableDescription();
    td.setDataSource("dataflow.airuuid");
    td.setProject(aggs);
    td.setGroup(groups);
    td.setLimit(10);
    td.setJobType("");
    td.setFilter("");

    assertDoesNotThrow(() -> {
      System.out.println(JSON.toJSONString(td));
      System.out.println(tableJobService.table(td));
    });
  }

  private static List<Arguments> aggregateProvider() {
    return Arrays.asList(
        Arguments.arguments(
            new String[]{"city"},
            new String[]{"city", "avg(AQI)", "count(distinct AQI)", "avg(AQI)"}
        ),
        Arguments.arguments(
            new String[]{"city", "city", "time"},
            new String[]{"city", "city", "time", "avg(AQI)", "min(AQI)", "max(AQI)", "stddevSamp(AQI)", "corr(PM2_5, PM10)"}
        ),
        Arguments.arguments(
            new String[]{},
            new String[]{"avg(AQI)", "min(AQI)", "max(AQI)", "stddevSamp(AQI)", "corr(PM2_5, PM10)"}
        ),
        Arguments.arguments(
            new String[]{},
            new String[]{"stddevSamp(AQI) as std"}
        ),
        // This agg function will generate 'uniqExact(AQI)' as column name
        Arguments.arguments(
            new String[]{},
            new String[]{"count(distinct AQI)"}
        )
    );
  }


}
