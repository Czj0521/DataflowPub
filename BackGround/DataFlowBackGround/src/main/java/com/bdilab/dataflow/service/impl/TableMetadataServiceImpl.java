package com.bdilab.dataflow.service.impl;

import com.bdilab.dataflow.clickhouse.ClickHouseHttpUtils;
import com.bdilab.dataflow.common.consts.OperatorConstants;
import org.apache.catalina.util.URLEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-18
 * @description:
 **/
@Service
public class TableMetadataServiceImpl {
    @Value("${clickhouse.http.url}")
    private String httpPrefix;


    public Map<String, String> metadataFromDatasource(String datasource) {
        return metadata("SELECT * FROM " + datasource);
    }

    /**
     * 获取任何查询语句的列名和类型（该语法jdbc不支持，所以走http接口形式）
     *
     * @param sql
     * @return
     */
    public Map<String, String> metadata(String sql) {
        String query = "desc%20(" + OperatorConstants.COLUMN_MAGIC_NUMBER + ")";
        URLEncoder encoder = new URLEncoder();
        sql = encoder.encode(sql, Charset.defaultCharset());
        String url = httpPrefix + query.replace(OperatorConstants.COLUMN_MAGIC_NUMBER, sql);
        Map<String, String> result = new HashMap<>();
        String content = ClickHouseHttpUtils.sendGet(url);
        String[] split = content.split("\n");
        for (String s : split) {
            String[] split1 = s.split("\t");
            result.put(split1[0], split1[1]);
        }
        return result;
    }

}
