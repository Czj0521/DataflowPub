package com.bdilab.dataflow.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bdilab.dataflow.model.DataSourceStatistic;
import org.springframework.stereotype.Repository;

/**
 * DataSourceStatistic Mapper.

 * @author wh
 * @version 1.0
 * @date 2021/09/16
 */
@Repository
@DS("mysql")
public  interface DataSourceStatisticMapper extends BaseMapper<DataSourceStatistic> {

}
