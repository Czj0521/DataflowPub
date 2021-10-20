package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bdilab.dataflow.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.dto.jobdescription.TableDescription;
import com.bdilab.dataflow.mapper.DataSourceStatisticMapper;
import com.bdilab.dataflow.mapper.TableStatisticMapper;
import com.bdilab.dataflow.model.DataSourceStatistic;
import com.bdilab.dataflow.model.TableStatistic;
import com.bdilab.dataflow.service.TableJobService;
import com.bdilab.dataflow.utils.sql.TableSQLGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.*;

import static com.bdilab.dataflow.common.consts.CommonConstants.DATABASE;
import static com.bdilab.dataflow.common.enums.DataTypeEnum.CLICKHOUSE_COLUMN_DATATYPE_MAP;

/**
 * @author gluttony team
 * @version 1.0
 * @date 2021/09/01
 * Table仪表盘-数据处理Service接口实现类
 */

@Service
@Slf4j
public class TableJobServiceImpl implements TableJobService {
    @Autowired
    ClickHouseJdbcUtils clickHouseJdbcUtils;
    @Autowired
    TableStatisticMapper tableStatisticMapper;
    @Autowired
    TableMetadataServiceImpl tableMetadataServiceImpl;
    @Resource
    DataSourceStatisticMapper dataSourceStatisticMapper;

    /**
     * 保存表结构
     * tablename要写全名，XX.xx
     * @param tableName
     */
    public void saveTableConstruct(String tableName) {
        TableStatistic tableStatistic = new TableStatistic();

        tableStatistic.setTableName(tableName);

        List<Map<String, Object>> maps = clickHouseJdbcUtils.queryForList("select count(*) from " + tableName);
        BigInteger count = (BigInteger) maps.get(0).get("count()");
        tableStatistic.setTableCount(count.longValue());

        Map<String, String> nameAndType = new LinkedHashMap<>();
        Map<String, Object> nameAndMin = new LinkedHashMap<>();
        Map<String, Object> nameAndMax = new LinkedHashMap<>();


        List<Map<String, Object>> descibeTableName = clickHouseJdbcUtils.queryForList("describe " + DATABASE +"."+ tableName);
        for (int i = 0; i < descibeTableName.size(); i++) {
            String name =  ((String) descibeTableName.get(i).get("name"));
            String type = ((String) descibeTableName.get(i).get("type"));
            nameAndType.put(name,CLICKHOUSE_COLUMN_DATATYPE_MAP.get(type));
            if(!"String".equals(type)){
                List<Map<String, Object>> maps1 = clickHouseJdbcUtils.queryForList("select min(" + name + ") from " + tableName);
                for (Map.Entry<String, Object> stringObjectEntry : maps1.get(0).entrySet()) {
                    Object value = stringObjectEntry.getValue();
                    nameAndMin.put(name, value);
                }
                List<Map<String, Object>> maps2 = clickHouseJdbcUtils.queryForList("select max(" + name + ") from " + tableName);
                for (Map.Entry<String, Object> stringObjectEntry : maps2.get(0).entrySet()) {
                    nameAndMax.put(name, (stringObjectEntry.getValue()));
                }
            }else{
                nameAndMin.put(name, "");
                nameAndMax.put(name, "");
            }
        }
        tableStatistic.setColumnMin(JSON.toJSONString(nameAndMin));
        tableStatistic.setColumnMax(JSON.toJSONString(nameAndMax));
        tableStatistic.setColumnNameType(JSON.toJSONString(nameAndType));

        DataSourceStatistic dataSourceStatistic = new DataSourceStatistic();
        BeanUtils.copyProperties(tableStatistic,dataSourceStatistic);
        dataSourceStatistic.setDatasource(tableStatistic.getTableName());
        dataSourceStatisticMapper.insert(dataSourceStatistic);
    }

    /**
     * 获取表结构
     * @param tableName
     * @return
     */
    public String getColumnNameAndType(String tableName){
        return tableStatisticMapper.getColumnsName(tableName);
    }


    /**
     * table 控件 数据获取
     * @param tableDescription
     * @return
     */
    public List<Map<String,Object>> table(TableDescription tableDescription){
        String sql = new TableSQLGenerator(tableDescription).generate();
        log.info("Table Job SQL :" + sql);
        return clickHouseJdbcUtils.queryForList(sql);
    }

    @Override
    public DataSourceStatistic getProfiler(String tablename) {
        QueryWrapper<DataSourceStatistic> qw = new QueryWrapper<>();
        qw.eq("datasource",tablename);
        return dataSourceStatisticMapper.selectOne(qw);
    }
}
