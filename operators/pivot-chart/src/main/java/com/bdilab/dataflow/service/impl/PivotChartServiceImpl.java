package com.bdilab.dataflow.service.impl;

import com.bdilab.dataflow.constants.AggregationConstants;
import com.bdilab.dataflow.constants.BinningConstants;
import com.bdilab.dataflow.constants.Communal;
import com.bdilab.dataflow.constants.SQLConstants;
import com.bdilab.dataflow.dto.jobdescription.Menu;
import com.bdilab.dataflow.dto.jobdescription.PivotChartDescription;
import com.bdilab.dataflow.dto.joboutputjson.ParamTypeRespObj;
import com.bdilab.dataflow.dto.joboutputjson.RespObj;
import com.bdilab.dataflow.service.PivotChartService;
import com.bdilab.dataflow.sql.generator.PivotChartSqlGenerator;
import com.bdilab.dataflow.utils.ComparatorAsc;
import com.bdilab.dataflow.utils.ComparatorDesc;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : [zhangpeiliang]
 * @description : [Service实现类]
 */
@Slf4j
@Service
public class PivotChartServiceImpl implements PivotChartService {

    @Autowired
    private ClickHouseJdbcUtils clickHouseJdbcUtils;

    @Override
    public List<Object> getPivotChart(PivotChartDescription description) {
        PivotChartSqlGenerator pivotChartSqlGenerator = new PivotChartSqlGenerator(description);
        String SQL = pivotChartSqlGenerator.generate();
        log.info(SQL);

        List<Object> results = new ArrayList<>();
        for (Menu menu : description.getMenus()) {
            // reuse the getAttributeRenaming() method
            String menuSb = SQLConstants.SELECT + menu.getAttributeRenaming() + Communal.BLANK + SQLConstants.FROM +
                    SQLConstants.LEFT_BRACKET + SQL + SQLConstants.RIGHT_BRACKET;

            log.info(MessageFormat.format("[Menu StringBuilder SQL]: {0}", menuSb));
            List<String> list = clickHouseJdbcUtils.queryForStrList(menuSb);
            RespObj respObj = new RespObj();
            if (!StringUtils.isEmpty(menu.getSort()) && !menu.getSort().equalsIgnoreCase(Communal.NONE)) {
                if (menu.getSort().equals(SQLConstants.ASC)) {
                    //菜单distinct值升序排列
                    Set<String> distinctSetAsc = new TreeSet<>(new ComparatorAsc());
                    distinctSetAsc.addAll(list);
                    respObj.setDistinctValues(distinctSetAsc);
                } else {
                    // 菜单distinct值降序排列
                    Set<String> distinctSetDesc = new TreeSet<>(new ComparatorDesc());
                    distinctSetDesc.addAll(list);
                    respObj.setDistinctValues(distinctSetDesc);
                }
            } else {
                // 菜单distinct值自然排列（默认就是升序）
                Set<String> distinctSet = new TreeSet<>(list);
                respObj.setDistinctValues(distinctSet);
            }

            respObj.setMenu(menu.getMenu());
            respObj.setName(menu.getAttributeRenaming());

            if (menu.hasBinning()) {
                respObj.setValues(list.stream().map(item -> menu.getAttribute() + Communal.BLANK + Communal.STARTS_WITH +
                        Communal.BLANK + item).collect(Collectors.toList()));
            } else {
                respObj.setValues(list);
            }

            results.add(respObj);
        }
        return results;
    }

    @Override
    public ParamTypeRespObj getType(String type) {
        ParamTypeRespObj respObj = new ParamTypeRespObj();
        List<String> strList = new ArrayList<>();
        List<String> booleanOrDateList = new ArrayList<>();
        List<String> numericList = new ArrayList<>();
        switch (type) {
            case AggregationConstants.AGGREGATION:
                strList.add(Communal.NONE);
                strList.add(AggregationConstants.COUNT);
                strList.add(AggregationConstants.DISTINCT_COUNT);
                respObj.setString(strList);
                respObj.setDate(strList);

                booleanOrDateList.add(Communal.NONE);
                booleanOrDateList.add(AggregationConstants.COUNT);
                booleanOrDateList.add(AggregationConstants.DISTINCT_COUNT);
                booleanOrDateList.add(AggregationConstants.AVERAGE);
                respObj.setBoolean(booleanOrDateList);

                numericList.add(Communal.NONE);
                numericList.add(AggregationConstants.COUNT);
                numericList.add(AggregationConstants.DISTINCT_COUNT);
                numericList.add(AggregationConstants.AVERAGE);
                numericList.add(AggregationConstants.SUM);
                numericList.add(AggregationConstants.MIN);
                numericList.add(AggregationConstants.MAX);
                numericList.add(AggregationConstants.STANDARD_DEV);
                respObj.setNumeric(numericList);
                break;
            case BinningConstants.BIN:
                strList.add(Communal.NONE);
                strList.add(BinningConstants.ALPHABETIC_BINNING);
                strList.add(BinningConstants.NOMINAL_BINNING);
                respObj.setString(strList);
                respObj.setBoolean(strList);

                numericList.add(Communal.NONE);
                numericList.add(BinningConstants.EQUI_WIDTH_BINNING);
                numericList.add(BinningConstants.NATURAL_BINNING);
                respObj.setNumeric(numericList);

                booleanOrDateList.add(Communal.NONE);
                booleanOrDateList.add(BinningConstants.SECOND);
                booleanOrDateList.add(BinningConstants.MINUTE);
                booleanOrDateList.add(BinningConstants.HOUR);
                booleanOrDateList.add(BinningConstants.DAY);
                booleanOrDateList.add(BinningConstants.MONTH);
                booleanOrDateList.add(BinningConstants.YEAR);
                respObj.setDate(booleanOrDateList);
                break;
            default:
                throw new RuntimeException("wrong type!");
        }
        return respObj;
    }

}
