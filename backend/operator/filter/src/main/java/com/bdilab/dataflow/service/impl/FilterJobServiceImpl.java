package com.bdilab.dataflow.service.impl;

import com.bdilab.dataflow.sql.generator.FilterSQLGenerator;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.dto.jobdescription.FilterDescription;
import com.bdilab.dataflow.utils.SQLParseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-22
 * @description:
 **/
@Service
public class FilterJobServiceImpl {
    @Autowired
    ClickHouseJdbcUtils clickHouseJdbcUtils;
    public String filter(FilterDescription filterDescription) {
        String UUID = SQLParseUtils.getUUID32();
        String sql = new FilterSQLGenerator(filterDescription,UUID).generate();
        clickHouseJdbcUtils.execute(sql);
        return UUID;
    }
}
