package com.bdilab.dataflow.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author wh
 * @version 1.0
 * @date 2021/09/13
 */

@Repository
@DS("mysql")
public interface TableFilterOperatorMapper extends BaseMapper<TableFilterOperatorMapper> {
    @Select("select * from table_filter_operator")
    List<Map<String, Object>> selectAll();
}
