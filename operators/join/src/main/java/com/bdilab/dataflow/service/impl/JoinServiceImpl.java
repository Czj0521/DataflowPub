package com.bdilab.dataflow.service.impl;



import com.bdilab.dataflow.dto.JoinJson;
import com.bdilab.dataflow.service.JoinService;
import com.bdilab.dataflow.sql.generator.JoinSQLGenerator;
import com.bdilab.dataflow.utils.SQLParseUtils;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
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
    public List<Map<String, Object>> join(JoinJson joinJson){
        String sql = new JoinSQLGenerator(joinJson,tableMetadataService).generate();
        System.out.println(sql);
        List<Map<String, Object>> result = clickHouseJdbcUtils.queryForList(sql);
        System.out.println(result);
        return result;
    }
}
