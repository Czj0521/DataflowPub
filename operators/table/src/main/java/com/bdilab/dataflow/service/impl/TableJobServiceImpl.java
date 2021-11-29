package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bdilab.dataflow.common.consts.CommonConstants;
import com.bdilab.dataflow.dto.jobdescription.TableDescription;
import com.bdilab.dataflow.mapper.DataSourceStatisticMapper;
import com.bdilab.dataflow.mapper.TableStatisticMapper;
import com.bdilab.dataflow.model.DataSourceStatistic;
import com.bdilab.dataflow.model.TableStatistic;
import com.bdilab.dataflow.service.TableJobService;
import com.bdilab.dataflow.sql.generator.TableSqlGenerator;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseManager;
import com.bdilab.dataflow.utils.dag.DagNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * TableJobServiceImpl.
 *
 * @author gluttony team
 * @version 1.0
 * @date 2021/09/01
 */

@Service
@Slf4j
public class TableJobServiceImpl implements TableJobService {
  @Autowired
  private ClickHouseJdbcUtils clickHouseJdbcUtils;
  @Autowired
  private TableStatisticMapper tableStatisticMapper;
  @Autowired
  private DataSourceStatisticMapper dataSourceStatisticMapper;
  @Autowired
  private ClickHouseManager clickHouseManager;


  /**
   * Save Table Structure.
   * tableName: XX.xx
   */
  public void saveTableConstruct(String tableName) {
    TableStatistic tableStatistic = new TableStatistic();

    tableStatistic.setTableName(tableName);

    List<Map<String, Object>> maps =
      clickHouseJdbcUtils.queryForList("select count(*) from " + tableName);
    BigInteger count = (BigInteger) maps.get(0).get("count()");
    tableStatistic.setTableCount(count.longValue());

    Map<String, String> nameAndType = new LinkedHashMap<>();
    Map<String, Object> nameAndMin = new LinkedHashMap<>();
    Map<String, Object> nameAndMax = new LinkedHashMap<>();


    List<Map<String, Object>> descibeTableName =
      clickHouseJdbcUtils.queryForList("describe "
        + com.bdilab.dataflow.common.consts.CommonConstants.DATABASE + "." + tableName);
    for (int i = 0; i < descibeTableName.size(); i++) {
      String name = ((String) descibeTableName.get(i).get("name"));
      String type = ((String) descibeTableName.get(i).get("type"));
      nameAndType.put(name,
        com.bdilab.dataflow.common.enums.DataTypeEnum.CLICKHOUSE_COLUMN_DATATYPE_MAP.get(type));
      if (!"String".equals(type)) {
        List<Map<String, Object>> maps1 =
          clickHouseJdbcUtils.queryForList("select min(" + name + ") from " + tableName);
        for (Map.Entry<String, Object> stringObjectEntry : maps1.get(0).entrySet()) {
          Object value = stringObjectEntry.getValue();
          nameAndMin.put(name, value);
        }
        List<Map<String, Object>> maps2 =
          clickHouseJdbcUtils.queryForList("select max(" + name + ") from " + tableName);
        for (Map.Entry<String, Object> stringObjectEntry : maps2.get(0).entrySet()) {
          nameAndMax.put(name, (stringObjectEntry.getValue()));
        }
      } else {
        nameAndMin.put(name, "");
        nameAndMax.put(name, "");
      }
    }
    tableStatistic.setColumnMin(JSON.toJSONString(nameAndMin));
    tableStatistic.setColumnMax(JSON.toJSONString(nameAndMax));
    tableStatistic.setColumnNameType(JSON.toJSONString(nameAndType));

    DataSourceStatistic dataSourceStatistic = new DataSourceStatistic();
    BeanUtils.copyProperties(tableStatistic, dataSourceStatistic);
    dataSourceStatistic.setDatasource(tableStatistic.getTableName());
    dataSourceStatisticMapper.insert(dataSourceStatistic);
  }

  /**
   * getColumnNameAndType.
   */
  public String getColumnNameAndType(String tableName) {
    return tableStatisticMapper.getColumnsName(tableName);
  }


  /**
   * Get Table Structure.
   */
  @Override
  public DataSourceStatistic getProfiler(String tableName) {
    QueryWrapper<DataSourceStatistic> qw = new QueryWrapper<>();
    qw.eq("datasource", tableName);
    return dataSourceStatisticMapper.selectOne(qw);
  }

  @Override
  public String generateDataSourceSql(TableDescription tableDescription) {
    return new TableSqlGenerator(tableDescription).generateDataSourceSql();
  }

  @Override
  public List<Map<String, Object>> execute(TableDescription jobDescription) {
    String sql = new TableSqlGenerator(jobDescription).generate();
    log.info("Table Job SQL :" + sql);
    return clickHouseJdbcUtils.queryForList(sql);
  }

  @Override
  public List<Map<String, Object>> saveToClickHouse(DagNode dagNode, Map<Integer, StringBuffer> preFilterMap) {
    // 读取数据源，并根据filter加入过滤条件
    JSONObject nodeDescription = (JSONObject) dagNode.getNodeDescription();
    String filter0 = nodeDescription.getString("filter");
    if (!StringUtils.isEmpty(filter0)) {
      nodeDescription.put("filter", filter0 + " AND " + preFilterMap.get(0).toString());
    } else {
      nodeDescription.put("filter", preFilterMap.get(0).toString());
    }

    TableDescription tableDescription = nodeDescription.toJavaObject(TableDescription.class);

    // 将计算结果保存到ClickHouse
    TableSqlGenerator tableSqlGenerator = new TableSqlGenerator(tableDescription);
    String sql = tableSqlGenerator.generateDataSourceSql();
    String tableName = CommonConstants.CPL_TEMP_TABLE_PREFIX + dagNode.getNodeId();

    clickHouseManager.createView(tableName, sql);

    // return the result
    return clickHouseJdbcUtils.queryForList(tableSqlGenerator.generate());
  }
}
