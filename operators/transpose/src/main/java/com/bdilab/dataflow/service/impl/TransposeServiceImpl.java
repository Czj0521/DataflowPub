package com.bdilab.dataflow.service.impl;

import com.bdilab.dataflow.dto.jobdescription.TransposeDescription;
import com.bdilab.dataflow.service.TransposeService;
import com.bdilab.dataflow.sql.generator.TransposeSQLGenerator;
import com.bdilab.dataflow.utils.SQLParseUtils;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseHttpUtils;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import org.apache.catalina.util.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-23
 * @description:
 **/
@Service
public class TransposeServiceImpl implements TransposeService {
    @Autowired
    ClickHouseJdbcUtils clickHouseJdbcUtils;

    /**
     *
     * @param transposeDescription
     * @return column value list
     */
    private List<String> columnValues(TransposeDescription transposeDescription) {
        String column = transposeDescription.getColumn();
        String datasource = transposeDescription.getDataSource();
        String sql = "SELECT distinct(" + column + ") FROM " + datasource;
        return clickHouseJdbcUtils.queryForStrList(sql);
    }

    @Override
    public String transpose(TransposeDescription transposeDescription) {
        return new TransposeSQLGenerator(transposeDescription, columnValues(transposeDescription)).generate();
    }

    public String generateDataSourceSql(TransposeDescription transposeDescription) {
        return new TransposeSQLGenerator(transposeDescription, columnValues(transposeDescription)).generateDataSourceSql();
    }
}
