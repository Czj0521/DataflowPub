package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.dto.profilerjson.ProfilerJson;
import com.bdilab.dataflow.dto.profilerjson.ProfilerResponseJson;
import com.bdilab.dataflow.service.ProfilerService;
import com.bdilab.dataflow.utils.PivotChartJobUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.*;

@Service
@Slf4j
public class ProfilerServiceImpl implements ProfilerService {
    @Resource
    PivotChartJobUtils pivotChartJobUtils;
    @Resource
    ClickHouseJdbcUtils clickHouseJdbcUtils;


    public ProfilerResponseJson getProfiler(String profilerJson){
        ProfilerJson ps = JSONObject.parseObject(profilerJson, ProfilerJson.class);
        String tableName = ps.getJobDescription().getInput();
        String column = ps.getJobDescription().getColumn();
        ProfilerResponseJson profilerResponseJson = new ProfilerResponseJson();
        profilerResponseJson.setResponseJobInfo(getProfiler(tableName,column));
        profilerResponseJson.setJobId(ps.getJobId());
        profilerResponseJson.setJob("response");
        profilerResponseJson.setWorkspaceId(ps.getWorkspaceId());
        return profilerResponseJson;
    }


    @Override
    public Map<String,Object> getProfiler(String tableName, String column) {
        Map<String, Object> sqlAndCalibration = pivotChartJobUtils.getSqlAndCalibration(tableName, column);
        //记录总条数
        Long sum = 0L;
        List<Map<String, Object>> maps0 = clickHouseJdbcUtils.queryForList("select count(*) from " + tableName);
        for (Map.Entry<String, Object> stringObjectEntry : maps0.get(0).entrySet()) {
            sum = ((BigInteger) stringObjectEntry.getValue()).longValue();
        }
        //当前字符串类型不为String时
        if(!"String".equals(sqlAndCalibration.get("type"))){
            List<String> sql = (List<String>) sqlAndCalibration.get("sql");
            List<Long> res = new ArrayList<>();
            //绘制直方图
            for (String s : sql) {
                List<Map<String, Object>> maps = clickHouseJdbcUtils.queryForList("select count(*) from " + tableName + " where " + s);
                for (Map.Entry<String, Object> stringObjectEntry : maps.get(0).entrySet()) {
                    BigInteger value = ((BigInteger) stringObjectEntry.getValue());
                    res.add(value.longValue());
                }
            }
            HashMap<String, Object> result = new LinkedHashMap<>();
            result.put("axisCalibration",sqlAndCalibration.get("calibration"));
            result.put("height",res);

            String[] tmp = {"mean", "min", "max", "std"};
            String[] tmp2 = {"null", "+inf", "-inf"};
            int i = 0;
            List<Map<String, Object>> maps1 = clickHouseJdbcUtils.queryForList("select avg(" + column + "),min("+column+"),max("+column+"),stddevPop("+column+") from " + tableName);
            for (Map.Entry<String, Object> stringObjectEntry : maps1.get(0).entrySet()) {
                result.put(tmp[i++],stringObjectEntry.getValue());
            }

            List<Map<String, Object>> maps5 = clickHouseJdbcUtils.queryForList("select count(*) from "+tableName+" where "+column +" =null union all select count(*) from "+tableName+" where "+column +" =inf union all select count(*) from "+tableName+" where "+column +" =-inf"  );
            for (int j = 0; j < maps5.size(); j++) {
                for (Map.Entry<String, Object> stringObjectEntry : maps5.get(j).entrySet()) {
                    result.put(tmp2[j]+"val",stringObjectEntry.getValue());
                    result.put(tmp2[j],((BigInteger) stringObjectEntry.getValue()).longValue()/sum);
                }
            }
            result.put("zerosVal",0);
            result.put("zeros", 0);
            return result;
        }
        //当前数据类型为String时
        else{
            List<String> sql = (List<String>) sqlAndCalibration.get("sql");
            List<Map<String, Object>> maps = clickHouseJdbcUtils.queryForList("select "+column+",count(*) from " + tableName +" "+ sql.get(0)+" order by count() desc");
            LinkedHashMap<String, Object> result = new LinkedHashMap<>();
            ArrayList<String> obj= new ArrayList<>();
            ArrayList<Long> val= new ArrayList<>();
            //绘制直方图
            for (Map<String, Object> map : maps) {
                obj.add(((String) map.get(column)));
                long l = ((BigInteger) map.get("count()")).longValue();
                val.add(l);
            }
            result.put("axisCalibration",obj);
            result.put("height",val);

            List<Map<String, Object>> maps5 = clickHouseJdbcUtils.queryForList("select count(*) from "+tableName+" where "+column +" =null");
            for (Map.Entry<String, Object> stringObjectEntry : maps5.get(0).entrySet()) {
                result.put("nullVal",stringObjectEntry.getValue());
                result.put("null", (sum==0)?0:((BigInteger) stringObjectEntry.getValue()).longValue()/sum);
            }

            return result;
        }
    }



}
