package com.bdilab.dataflow.service.impl;

import com.bdilab.dataflow.clickhouse.ClickHouseHttpUtils;
import com.bdilab.dataflow.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.dto.jobdescription.TransposeDescription;
import com.bdilab.dataflow.utils.SQLParseUtils;
import com.bdilab.dataflow.utils.sql.TransposeSQLGenerator;
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
public class TransposeServiceImpl {
    @Value("${clickhouse.http.url}")
    private String httpPrefix;
    @Autowired
    ClickHouseJdbcUtils clickHouseJdbcUtils;

    private List<String> columnValues(TransposeDescription transposeDescription) {
        String column = transposeDescription.getColumn();
        String datasource = transposeDescription.getDataSource();
        String sql = "SELECT distinct(" + column + ") FROM " + datasource;
        return clickHouseJdbcUtils.queryForStrList(sql);
    }

    public String transpose(TransposeDescription transposeDescription) {
        String uuid = SQLParseUtils.getUUID32();
        String sql = new TransposeSQLGenerator(uuid, transposeDescription, columnValues(transposeDescription)).generate();
        URLEncoder urlEncoder = new URLEncoder();
        sql = urlEncoder.encode(sql, Charset.defaultCharset());
        ClickHouseHttpUtils.sendPost(httpPrefix + sql);
        return uuid;
    }
}
