package com.bdilab.dataflow.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.consts.CommonConstants;
import com.bdilab.dataflow.dto.JoinDescription;
import com.bdilab.dataflow.service.JoinService;
import com.bdilab.dataflow.sql.generator.JoinSqlGenerator;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import java.util.List;
import java.util.Map;

import com.bdilab.dataflow.utils.dag.DagNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


/**
 * join service implement.

 * @author: Yu Shaochao
 * @create: 2021-10-24
 * @description:
 */

@Service
public class JoinServiceImpl implements JoinService {
  @Autowired
  ClickHouseJdbcUtils clickHouseJdbcUtils;

  @Autowired
  TableMetadataServiceImpl tableMetadataService;

  @Override
  public List<Map<String, Object>> execute(JoinDescription joinDescription) {
    return null;
  }

  @Override
  public List<Map<String, Object>> saveToClickHouse(DagNode dagNode, Map<Integer, StringBuffer> preFilterMap) {
    //由dagNode拿到JoinDesc
    JSONObject nodeDescription = (JSONObject) dagNode.getNodeDescription();
    JoinDescription joinDescription = nodeDescription.toJavaObject(JoinDescription.class);

    //设置过滤后的join的两个数据源
    StringBuffer leftFilter = preFilterMap.get(0);
    StringBuffer rightFilter = preFilterMap.get(1);

    String dataSourceWithFilter0;
    String dataSourceWithFilter1;
    if(leftFilter.length()!=0){
      dataSourceWithFilter0 = "(SELECT * FROM "+ joinDescription.getDataSource()[0]+" WHERE " + leftFilter+")";
    }else{
      dataSourceWithFilter0 = joinDescription.getDataSource()[0];
    }
    if(rightFilter.length()!=0){
      dataSourceWithFilter1 = "(SELECT * FROM "+ joinDescription.getDataSource()[1]+" WHERE " + rightFilter+")";
    }else{
      dataSourceWithFilter1 = joinDescription.getDataSource()[1];
    }


    //生成sql
    joinDescription.setDataSource(new String[]{dataSourceWithFilter0,dataSourceWithFilter1});
    String sql = new JoinSqlGenerator(joinDescription, tableMetadataService).generate();

    //创建视图（后续提取公共部分），此处代码不同操作符都一样
    String tableName = CommonConstants.CPL_TEMP_TABLE_PREFIX + dagNode.getNodeId();
    StringBuilder sb = new StringBuilder();
    String viewSql = sb.append("CREATE VIEW ").append(tableName).append(" AS ")
            .append("(").append(sql).append(")").toString();
    try {
      clickHouseJdbcUtils.execute(viewSql);
    } catch (Exception e) {
      clickHouseJdbcUtils.execute("drop view " + tableName);
      clickHouseJdbcUtils.execute(viewSql);
    }

    // return the result,join return null;
    return null;

  }

  //弃用
  @Override
  public String join(JoinDescription joinDescription) {
    String sql = new JoinSqlGenerator(joinDescription, tableMetadataService).generate();
    return sql;
  }

  public String generateDataSourceSql(JoinDescription joinDescription) {
    return new JoinSqlGenerator(joinDescription, tableMetadataService).generateDataSourceSql();
  }
}
