package com.bdilab.dataflow.clickhouse;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author
 * @version 1.0
 * @date 2021/09/13
 */
@Repository
@DS("clickhouse")
public class ClickHouseJdbcUtils {
    @Resource
    JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> queryForList(String sql) {
        return jdbcTemplate.queryForList(sql);
    }

    public List<String> queryForStrList(String sql) {
        return jdbcTemplate.queryForList(sql, String.class);
    }

    public void execute(String sql) {
        jdbcTemplate.execute(sql);
    }

}
