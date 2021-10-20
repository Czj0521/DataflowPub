package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.common.enums.GroupOperatorEnum;
import com.bdilab.dataflow.dto.pivotchartjson.*;
import com.bdilab.dataflow.common.pojo.PivotChartParameterInfo;
import com.bdilab.dataflow.common.pojo.PivotChartParameters;
import com.bdilab.dataflow.common.pojo.PivotChartSqlPojo;
import com.bdilab.dataflow.service.PivotChartJobService;
import com.bdilab.dataflow.utils.CommonUtils;
import com.bdilab.dataflow.utils.PivotChartJobUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

import static com.bdilab.dataflow.common.consts.CommonConstants.*;
import static com.bdilab.dataflow.common.consts.OperatorConstants.COLUMN_MAGIC_NUMBER;


/**
 * @author wh
 * @version 1.0
 * @date 2021/09/17
 * PivotChart仪表盘-数据处理Service接口实现类
 */
@Service
@Slf4j
public class PivotChartJobServiceImpl implements PivotChartJobService {

    @Resource
    PivotChartJobUtils pivotChartJobUtils;
    @Resource
    ClickHouseJdbcUtils clickHouseJdbcUtils;

    @Override
    public PivotChartResponseJson submitPivotChartJob(String inputJson) {
        JSONObject jsonObject = JSONObject.parseObject(inputJson);
        if(PIVOT_CHART.equals(jsonObject.getString("jobType"))){
            PivotChartDescription pivotChartDescription = jsonObject.getObject("jobDescription", PivotChartDescription.class);
            PivotChartResponseJson pivotChartResponseJson = new PivotChartResponseJson();
            ResponseJobInfor responseJobInfor;
            String mark = pivotChartDescription.getMark();
            switch (mark){
                case "bar":
                    responseJobInfor =  barMarkPivotChartJob(pivotChartDescription);
                    break;
                case "line":
                case "area":
                case "point":
                    responseJobInfor =  pointMarkPivotChartJob(pivotChartDescription);
                    break;
                case "tick":
                    throw new RuntimeException("未完成此类型mark");
                default:
                    throw new RuntimeException("无此类型mark：" + mark);
            }
            pivotChartResponseJson.setJob(jsonObject.get("job"));
            pivotChartResponseJson.setJobId(jsonObject.get("jobId"));
            pivotChartResponseJson.setResponseJobInfor(responseJobInfor);
            pivotChartResponseJson.setWorkspaceId(jsonObject.get("workspaceId"));
            return pivotChartResponseJson;
        }
        return null;
    }


    public ResponseJobInfor barMarkPivotChartJob(PivotChartDescription pivotChartDescription){
        String input = pivotChartDescription.getDataSource();
        PivotChartSqlPojo pcSql = new PivotChartSqlPojo();
        ResponseJobInfor responseJobInfor = new ResponseJobInfor();
        ResponseAxisValue responseAxisValue = new ResponseAxisValue();
        int valueSize = 1;
        PivotChartParameters pivotChartParameters = pivotChartJobUtils.jobDescriptionToPivotChartParameters(pivotChartDescription);
        String axisType;

        /**
         * 柱状图 第一维度必须为X轴！
         * 参数必须按照，"x-axis"，"y-axis"，"color"…… 的顺序，中间项可为空。
         * 二维时：Y轴ATTRIBUTE操作和AGGREGATION操作必须同时有；Y轴不能为string类型（可在color完成）；color和Y轴不能共存，因为在bar图中color一般区分数量
         */
        if (pivotChartParameters.getXAxis()==null) {
            throw new RuntimeException("X轴为第一维度，必须存在");
        }
        barOperationInsertPcSql(pcSql, pivotChartParameters, input, responseAxisValue);
        axisType = "x-axis";

        //todo color(查多次)(brush)（条形图color没搞懂怎么画）

        //todo row(查多次)

        //todo column(查多次)

        String sql = generatePivotChartBarSql(pcSql, pivotChartDescription.getDataSource());
        log.info(sql);
        List<Map<String, Object>> maps = clickHouseJdbcUtils.queryForList(sql);
        List<Object> height = new ArrayList<>();

        if(pivotChartParameters.getXAxis().getDataType().equals(STRING_NAME)){
            List<Object> calibrations = new ArrayList<>();
            for (Map<String, Object> map : maps) {
                Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
                height.add(iterator.next().getValue());
                calibrations.add(iterator.next().getValue());
            }
            responseAxisValue.setAxisCalibration(calibrations);

        } else {
            for (Map<String, Object> map : maps) {
                height.add(map.entrySet().iterator().next().getValue());
            }
        }
        responseAxisValue.setHeight(height);
        responseAxisValue.setType(axisType);
        responseJobInfor.getAbstractResponseValues().add(responseAxisValue);
        responseJobInfor.setMark("bar");
        responseJobInfor.setValueSize(valueSize);

        return responseJobInfor;
    }
    public void barOperationInsertPcSql(PivotChartSqlPojo pcSql, PivotChartParameters pivotChartParameters, String input, ResponseAxisValue responseAxisValue){
        PivotChartParameterInfo xAxis = pivotChartParameters.getXAxis();
        String xColName = xAxis.getAttribute();
        //ATTRIBUTE操作
        Map<String, Object> sqlAndCalibration = pivotChartJobUtils.getSqlAndCalibration(input, xColName);
        if (xAxis.getDataType().equals(STRING_NAME)) {
            pcSql.getGroup().add(xColName);
        } else {
            pcSql.setUnionFilter(CommonUtils.objectToListString(sqlAndCalibration.get("sql")));
            Object calibrationObject = sqlAndCalibration.get("calibration");
            List<Object> calibrationList = new ArrayList<>();
            if (calibrationObject instanceof List<?>){
                calibrationList = new ArrayList<>((List<?>) calibrationObject);
            }
            responseAxisValue.setAxisCalibration(calibrationList);
        }
        //AGGREGATION操作
        GroupOperatorEnum groupOperatorEnum;
        if (xAxis.getAggregation()!=null) {
            groupOperatorEnum = GroupOperatorEnum.getGroupOperatorEnum(xAxis.getAggregation());
            if (groupOperatorEnum.isOnlyNumeric() && !xAxis.getDataType().equals(NUMERIC_NAME)){
                throw new RuntimeException("操作类型错误！列属性类型：" + xAxis.getType() + "，无法进行"+groupOperatorEnum.getOperatorName()+"操作！");
            }
        } else {
            groupOperatorEnum = GroupOperatorEnum.COUNT;
        }
        String xProj = groupOperatorEnum.getSQLParam().replace(COLUMN_MAGIC_NUMBER, xColName);
        if(xAxis.getDataType().equals(STRING_NAME)){
            pcSql.getProjection().add(xProj);
            pcSql.getProjection().add(xColName);
        } else {
            pcSql.getUnionProjection().add(xProj);
        }
        //BINNING操作
        if (xAxis.getBinning()!=null) {
            throw new RuntimeException("操作类型错误！条形图无法进行BINNING操作！");
        }
        //SORT操作
        if (xAxis.getSort()!=null) {
            if (xAxis.getDataType().equals(STRING_NAME)){
                StringBuilder tempSb = new StringBuilder();
                tempSb.append(xColName).append(" ").append(xAxis.getSort());
                pcSql.getSort().add(new String(tempSb));
            } else {
                throw new RuntimeException("操作类型错误！（x轴bar图numeric不支持sort）");
            }
        }
        //Y轴
        if(pivotChartParameters.getYAxis()==null){
            return;
        }
        PivotChartParameterInfo yAxis = pivotChartParameters.getYAxis();
        String yColName = yAxis.getAttribute();
        if (yAxis.getAggregation()==null || yAxis.getAggregation().contains("count")) {
            throw new RuntimeException("输入错误！bar图Y轴必须有AGGREGATION，且不能为count类型");
        }
        String yProj;
        if (yAxis.getDataType().equals(STRING_NAME)) {
            throw new RuntimeException("输入错误！Y轴不能为string类型（可在color完成）");
        } else {
            yProj = GroupOperatorEnum.getGroupOperatorEnum(yAxis.getAggregation()).getSQLParam().replace(COLUMN_MAGIC_NUMBER, yColName);
            if(xAxis.getDataType().equals(STRING_NAME)){
                pcSql.getProjection().clear();
                pcSql.getProjection().add(yProj);
                pcSql.getProjection().add(xColName);
            } else {
                pcSql.getUnionProjection().clear();
                pcSql.getUnionProjection().add(yProj);
            }
        }
        //BINNING操作（不支持）;SORT操作（x轴bar图为numeric时不支持,逻辑很难写，坐标轴会变，跟当前完成逻辑不符，难以修改）
        if (yAxis.getSort()!=null) {
            if (xAxis.getDataType().equals(STRING_NAME)){
                StringBuilder tempSb = new StringBuilder();
                tempSb.append(yProj).append(" ").append(yAxis.getSort());
                pcSql.getSort().clear();
                pcSql.getSort().add(new String(tempSb));
            } else {
                throw new RuntimeException("操作类型错误！（x轴bar图numeric时，不支持Y轴sort）");
            }
        }
    }
    public String generatePivotChartBarSql(PivotChartSqlPojo sqlPojo, String input){
        StringBuilder sql = new StringBuilder();
        //X轴字符串类型
        if(sqlPojo.getUnionFilter().isEmpty()){
            if(sqlPojo.getProjection().isEmpty()){
                throw new RuntimeException("select不能为空");
            }
            sql.append("select ").append(sqlPojo.getProjectionSql()).append(" from ").append(input).append(" ");
            if(!sqlPojo.getFilter().isEmpty()){
                sql.append("where ").append(sqlPojo.getFilterSql()).append(" ");
            }
            if(!sqlPojo.getGroup().isEmpty()){
                sql.append("group by ").append(sqlPojo.getGroupSql()).append(" ");
            }
            if(!sqlPojo.getSort().isEmpty()){
                sql.append("order by ").append(sqlPojo.getSortSql());
            }
        } else {
            if(sqlPojo.getProjection().isEmpty()){
                sql.append("select * from (");
            } else {
                throw new RuntimeException("参数出错或功能未完成");
            }
            String unionProjection = sqlPojo.getUnionProjectionSql();
            List<String> unionFilter = sqlPojo.getUnionFilter();
            for (int i = 0; i < unionFilter.size(); i++) {
                sql.append("select ").append(unionProjection).append(",").append(i).append(" keepOrderTemp from ").append(input)
                        .append(" where ").append(unionFilter.get(i)).append(sqlPojo.getUnionBasicFilterSql()).append(" union all ");
            }
            sql = new StringBuilder(sql.substring(0,sql.length()-11));
            sql.append(" )");
            sql.append("order by keepOrderTemp");
        }
        return new String(sql);
    }


    /**
     * 散点图
     */
    public ResponseJobInfor pointMarkPivotChartJob(PivotChartDescription pivotChartDescription){
        PivotChartSqlPojo pcSql = new PivotChartSqlPojo();
        ResponseJobInfor responseJobInfor = new ResponseJobInfor();
        String axisDimension = pivotChartDescription.getAxisDimension();
        int valueSize = 1;
        PivotChartParameters pivotChartParameters = pivotChartJobUtils.jobDescriptionToPivotChartParameters(pivotChartDescription);
        String axisType = null;
        /**
         * 1、一个坐标轴聚合，另一个坐标轴一定要为离散类型（否则画不了图）
         * 2、同上，两个坐标轴都不为离散时无法聚合（目前默认考虑日期类型为连续的）。
         * 3、只选泽两个坐标，不做任何聚合，则直接返回给前端数据；有聚合则返回给前端高度（跟直方图一样）
         * 4、后端不考虑二维下，只对字符串类型坐标轴进行排序（不聚合），交给前端处理
         * 5、Binning 全权交由前端处理
         */
        boolean flag = false;
        if (Integer.parseInt(axisDimension) == 1) {
            if (pivotChartParameters.getXAxis()!=null) {
                pcSql.getProjection().add(pivotChartParameters.getXAxis().getAttribute());
            } else if (pivotChartParameters.getYAxis()!=null) {
                pcSql.getProjection().add(pivotChartParameters.getYAxis().getAttribute());

            }
        } else {
            PivotChartParameterInfo xAxis = pivotChartParameters.getXAxis();
            PivotChartParameterInfo yAxis = pivotChartParameters.getYAxis();
            if (xAxis.getDataType().equals(STRING_NAME) && yAxis.getDataType().equals(STRING_NAME)
                    || !xAxis.getDataType().equals(STRING_NAME) && !yAxis.getDataType().equals(STRING_NAME)){
                pcSql.getProjection().add(xAxis.getAttribute());
                pcSql.getProjection().add(yAxis.getAttribute());
            } else if (xAxis.getDataType().equals(STRING_NAME) && !yAxis.getDataType().equals(STRING_NAME)){
                flag = pointOperationInsertPcSql(pcSql, xAxis, yAxis);
                axisType = "x-axis";
            } else if (!xAxis.getDataType().equals(STRING_NAME) && yAxis.getDataType().equals(STRING_NAME)){
                flag = pointOperationInsertPcSql(pcSql, yAxis, xAxis);
                axisType = "y-axis";
            }
        }
        //todo color

        //todo row

        //todo column


        if (!flag) {
            pcSql.setLimit(String.valueOf(CHART_POINT_LIMIT));
        }
        String sql = generatePivotChartPointSql(pcSql, pivotChartDescription.getDataSource());
        log.info(sql);
        List<Map<String, Object>> maps = clickHouseJdbcUtils.queryForList(sql);
        if (!flag) {
            ResponseDataValue responseDataValue = new ResponseDataValue();
            responseDataValue.setType("data");
            responseDataValue.setData(maps);
            responseDataValue.setDataDim(Integer.parseInt(axisDimension));
            responseJobInfor.getAbstractResponseValues().add(responseDataValue);
        } else {
            List<Object> calibrations = new ArrayList<>();
            List<Object> height = new ArrayList<>();
            for (Map<String, Object> map : maps) {
                Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
                height.add(iterator.next().getValue());
                calibrations.add(iterator.next().getValue());
            }
            ResponseAxisValue responseAxisValue = new ResponseAxisValue();
            responseAxisValue.setType(axisType);
            responseAxisValue.setHeight(height);
            responseAxisValue.setAxisCalibration(calibrations);
            responseJobInfor.getAbstractResponseValues().add(responseAxisValue);
        }
        responseJobInfor.setMark("point");
        //todo 目前固定为1
        responseJobInfor.setValueSize(valueSize);
        return responseJobInfor;
    }

    public boolean pointOperationInsertPcSql(PivotChartSqlPojo pcSql, PivotChartParameterInfo stringType, PivotChartParameterInfo noStringType){
        if (noStringType.getAggregation() == null) {
            //String轴Sort交给前端做
            pcSql.getProjection().add(stringType.getAttribute());
            pcSql.getProjection().add(noStringType.getAttribute());
            return false;
        }
        GroupOperatorEnum groupOperatorEnum = GroupOperatorEnum.getGroupOperatorEnum(noStringType.getAggregation());
        //建议前端不要进行cout操作，坐标轴会变的。
        if(noStringType.getAggregation().contains(GroupOperatorEnum.COUNT.getOperatorName())){
            throw new RuntimeException("不要进行count相关操作，坐标轴会变的!");
        }
        String aggreProjection = groupOperatorEnum.getSQLParam().replace(COLUMN_MAGIC_NUMBER, noStringType.getAttribute());
        pcSql.getProjection().add(aggreProjection);
        pcSql.getProjection().add(stringType.getAttribute());
        pcSql.getGroup().add(stringType.getAttribute());
        if(noStringType.getAggregation()!=null){
            StringBuilder tempSb = new StringBuilder();
            tempSb.append(aggreProjection).append(" ").append(noStringType.getSort());
            pcSql.getSort().add(new String(tempSb));
        }
        return true;
    }

    public String generatePivotChartPointSql(PivotChartSqlPojo sqlPojo, String input){
        StringBuilder sql = new StringBuilder();
        if(sqlPojo.getProjection().isEmpty()){
            throw new RuntimeException("select不能为空");
        }
        sql.append("select ").append(sqlPojo.getProjectionSql()).append(" from ").append(input).append(" ");
        if(!sqlPojo.getFilter().isEmpty()){
            sql.append("where ").append(sqlPojo.getFilterSql()).append(" ");
        }
        if(!sqlPojo.getGroup().isEmpty()){
            sql.append("group by ").append(sqlPojo.getGroupSql()).append(" ");
        }
        if(!sqlPojo.getSort().isEmpty()){
            sql.append("order by ").append(sqlPojo.getSortSql());
        }
        if(sqlPojo.getLimit() != null){
            sql.append("limit ").append(sqlPojo.getLimit());
        }

        return new String(sql);
    }
}
