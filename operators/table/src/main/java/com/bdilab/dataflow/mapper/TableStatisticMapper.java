package com.bdilab.dataflow.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bdilab.dataflow.model.TableStatistic;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * TableStatistic Mapper.
 *
 * @author wh
 * @version 1.0
 * @date 2021/09/12
 */
@Repository
@DS("mysql")
public interface TableStatisticMapper extends BaseMapper<TableStatistic> {
  /**
   * Query all column names and attributes of the corresponding table.
   */
  @Select("select column_name_type from table_statistic where table_name = #{tableName}")
  String getColumnsName(String tableName);
}
