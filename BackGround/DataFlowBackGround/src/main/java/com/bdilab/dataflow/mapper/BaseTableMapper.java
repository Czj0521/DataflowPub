package com.bdilab.dataflow.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bdilab.dataflow.model.BaseTable;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author wh
 * @version 1.0
 * @date 2021/09/12
 */
@Repository
@DS("mysql")
public interface BaseTableMapper extends BaseMapper<BaseTable> {
    @Select("select base_table.table_uuid from base_table,file where file.file_name=#{fileName} and file.table_id=base_table.id")
    String selectTableUuid(String fileName);
}
