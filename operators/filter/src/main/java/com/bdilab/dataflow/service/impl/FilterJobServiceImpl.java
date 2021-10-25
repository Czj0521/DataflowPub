package com.bdilab.dataflow.service.impl;

import com.bdilab.dataflow.service.FilterJobService;
import com.bdilab.dataflow.sql.generator.FilterSQLGenerator;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.dto.jobdescription.FilterDescription;
import com.bdilab.dataflow.utils.SQLParseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.bdilab.dataflow.common.enums.FilterOperatorEnum.FILTER_OPERATORS;

/**
 * @author: wh
 * @create: 2021-10-25
 * @description: Filter Job ServiceImpl
 */
@Service
public class FilterJobServiceImpl implements FilterJobService {
    @Override
    public Map<String, Map<String, String>> getFilterOperators() {
        return FILTER_OPERATORS;
    }
}
