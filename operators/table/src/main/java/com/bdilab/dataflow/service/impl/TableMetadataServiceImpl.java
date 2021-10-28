package com.bdilab.dataflow.service.impl;

import com.bdilab.dataflow.common.consts.OperatorConstants;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseHttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.util.URLEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static com.bdilab.dataflow.common.consts.CommonConstants.DATABASE;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-18
 * @description:
 **/
@Service
@Slf4j
public class TableMetadataServiceImpl {
    @Value("${clickhouse.http.url}")
    private String httpPrefix;


    public Map<String, String> metadataFromDatasource(String datasource) {
        return metadata("SELECT * FROM " + DATABASE + "." + datasource);
    }

    /**
     * Gets the column name and type of any query statement
     * （This syntax is not supported by JDBC, so use the HTTP interface format）
     * @param sql
     * @return
     */
    public Map<String, String> metadata(String sql) {
        String query = "desc%20(" + OperatorConstants.COLUMN_MAGIC_NUMBER + ")";
        URLEncoder encoder = new URLEncoder();
        sql = encoder.encode(sql, Charset.defaultCharset());
        String url = httpPrefix + query.replace(OperatorConstants.COLUMN_MAGIC_NUMBER, sql);
        Map<String, String> result = new HashMap<>();
        log.info(url);
        String content = ClickHouseHttpUtils.sendGet(url);
        String[] split = content.split("\n");
        for (String s : split) {
            String[] split1 = s.split("\t");
            result.put(split1[0], split1[1]);
        }
        return result;
    }

}
