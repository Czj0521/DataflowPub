//package com.bdilab.dataflow.utils;
//
//import com.alibaba.fastjson.JSONObject;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.bdilab.dataflow.common.enums.DataTypeEnum;
//import com.bdilab.dataflow.common.pojo.PivotChartAxisCalibration;
//import com.bdilab.dataflow.dto.pivotchartjson.PivotChartDescription;
//import com.bdilab.dataflow.dto.pivotchartjson.Operations;
//import com.bdilab.dataflow.common.pojo.PivotChartParameterInfo;
//import com.bdilab.dataflow.common.pojo.PivotChartParameters;
//import com.bdilab.dataflow.mapper.DataSourceStatisticMapper;
//import com.bdilab.dataflow.model.DataSourceStatistic;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//import static com.bdilab.dataflow.common.consts.CommonConstants.*;
//
///**
// * @author wh
// * @version 1.0
// * @date 2021/09/16
// *
// */
//@Component
//public class PivotChartJobUtils {
////    public final static Pattern NUMERIC_TYPE_PATTERN = Pattern.compile("^(Integer|Long|Float|Double)$");
//
//    @Resource
//    DataSourceStatisticMapper dataSourceStatisticMapper;
//
//    public Map<String,Object> getSqlAndCalibration(String dataSource, String columnName){
//        PivotChartAxisCalibration<Object> pChartAxisCal = getPivotChartAxisParameter(dataSource, columnName);
//        String type = pChartAxisCal.getType();
//        List<Object> calibration = pChartAxisCal.getCalibration();
//        List<String> sqlList = new ArrayList<>();
//        List<Object> calibrationStandardFormat= new ArrayList<>();
//        Map<String,Object> result = new HashMap<>(3);
//        switch (DataTypeEnum.COLUMN_DATATYPE_MAP.get(type)){
//            case NUMERIC_NAME:
//                double l = Double.parseDouble(String.valueOf(pChartAxisCal.getMin()));
//                double step = Double.parseDouble(String.valueOf(pChartAxisCal.getStep()));
//                double r = l+step;
//
//                for (int i = 0; i < pChartAxisCal.getTicks()-1; i++,l=r,r+=step) {
//                    StringBuilder nsql = new StringBuilder();
//                    nsql.append("( ").append(l).append(" <= ").append(columnName).append(" and ").append(columnName).append(" < ").append(r).append(" )");
//                    sqlList.add(new String(nsql));
//                }
//                StringBuilder nsql = new StringBuilder();
//                nsql.append("( ").append(l).append(" <= ").append(columnName).append(" and ").append(columnName).append(" <= ").append(r).append(" )");
//                sqlList.add(new String(nsql));
//                for (int i = 1; i < calibration.size(); i++) {
//                    StringBuilder csf = new StringBuilder();
//                    csf.append(calibration.get(i-1)).append(" - ").append(calibration.get(i));
//                    calibrationStandardFormat.add(csf);
//                }
//                result.put("calibration",calibrationStandardFormat);
//                break;
//            case STRING_NAME:
//                StringBuilder ssql = new StringBuilder();
//                ssql.append("group by ").append(columnName);
//                sqlList.add(new String(ssql));
//                break;
//            case DATE_NAME:
//                SimpleDateFormat sdf;
//                switch (type){
//                    case "Date":
//                        sdf = new SimpleDateFormat(DATE_FORMAT);
//                        break;
//                    case "DateTime":
//                        sdf = new SimpleDateFormat(DATETIME_FORMAT);
//                        break;
//                    case "DateTime64":
//                        sdf = new SimpleDateFormat(DATETIME64_FORMAT);
//                        break;
//                    default:
//                        throw new RuntimeException("Error type!");
//                }
//                List<Object> calList = pChartAxisCal.getCalibration();
//                for (int i = 1; i < calList.size()-1; i++) {
//                    long dl = Long.parseLong(String.valueOf(calList.get(i-1)));
//                    long dr = Long.parseLong(String.valueOf(calList.get(i)));
//                    StringBuilder dsql = new StringBuilder();
//                    dsql.append("( '").append(sdf.format(new Date(dl))).append("' <= ").append(columnName)
//                            .append(" and ").append(columnName).append(" < '").append(sdf.format(new Date(dr))).append("' )");
//                    sqlList.add(new String(dsql));
//
//                }
//                long dl = Long.parseLong(String.valueOf(calList.get(calList.size()-2)));;
//                long dr = Long.parseLong(String.valueOf(calList.get(calList.size()-1)));
//                StringBuilder dsql = new StringBuilder();
//                dsql.append("( '").append(sdf.format(new Date(dl))).append("' <= ").append(columnName)
//                        .append(" and ").append(columnName).append(" < '").append(sdf.format(new Date(dr))).append("' )");
//                sqlList.add(new String(dsql));
//                for (int i = 1; i < calibration.size(); i++) {
//                    StringBuilder csf = new StringBuilder();
//
//                    csf.append(sdf.format(new Date(Long.parseLong(String.valueOf(calibration.get(i-1)))))).append(" - ")
//                            .append(sdf.format(new Date(Long.parseLong(String.valueOf(calibration.get(i))))));
//                    calibrationStandardFormat.add(csf);
//                }
//                result.put("calibration",calibrationStandardFormat);
//                break;
//            default:
//                throw new RuntimeException("错误列属性类型");
//        }
//        result.put("sql",sqlList);
//        result.put("type",type);
//        return result;
//    }
//
//    public PivotChartAxisCalibration<Object> getPivotChartAxisParameter(String dataSource, String columnName){
//        QueryWrapper<DataSourceStatistic> dataSourceStatisticQueryWrapper = new QueryWrapper<>();
//        dataSourceStatisticQueryWrapper.eq("datasource",dataSource);
//        DataSourceStatistic dataSourceStatistic = dataSourceStatisticMapper.selectOne(dataSourceStatisticQueryWrapper);
//        JSONObject colMinJsonOb = JSONObject.parseObject(dataSourceStatistic.getColumnMin());
//        JSONObject colMaxJsonOb = JSONObject.parseObject(dataSourceStatistic.getColumnMax());
//        String columnType = JSONObject.parseObject(dataSourceStatistic.getColumnNameType()).getString(columnName);
//        PivotChartAxisCalibration<Object> resAxisCal = new PivotChartAxisCalibration< >();
//
//        switch (DataTypeEnum.COLUMN_DATATYPE_MAP.get(columnType)){
//            case NUMERIC_NAME:
//                PivotChartAxisCalibration<Double> numericAxisCal = AdaptiveAxisCalibrationUtils.getAdaptiveNumericAxisCalibration(colMinJsonOb.getDouble(columnName), colMaxJsonOb.getDouble(columnName), CHART_MAX_COLUMN);
//                resAxisCal = transferToNumericAxisParameter(numericAxisCal,columnType);
//                break;
//            case STRING_NAME:
//                resAxisCal.setType(columnType);
//                break;
//            case DATE_NAME:
//                PivotChartAxisCalibration<Long> dateAxisCal = AdaptiveAxisCalibrationUtils.getAdaptiveDateAxisCalibration(colMinJsonOb.getDate(columnName),colMaxJsonOb.getDate(columnName),columnType);
//                resAxisCal = transferToDateAxisParameter(dateAxisCal,columnType);
//                break;
//            default:
//                throw new RuntimeException("错误列属性类型");
//        }
//        return resAxisCal;
//    }
//
//
//
//    public PivotChartAxisCalibration<Object> transferToNumericAxisParameter(PivotChartAxisCalibration<Double> axisCal, String columnType){
//        PivotChartAxisCalibration<Object> resAxisCal = new PivotChartAxisCalibration<>();
//        switch (columnType){
//            case "Integer":
//                int iMin = axisCal.getMin().intValue();
//                int iMax = axisCal.getMax().intValue();
//                int iStep = axisCal.getStep().intValue();
//                resAxisCal.setMin(iMin);
//                resAxisCal.setMax(iMax);
//                resAxisCal.setStep(iStep);
//                List<Object> iTemp = new ArrayList<>();
//                iTemp.add(iMin);
//                iMin += iStep;
//                for (int i = 0; i < axisCal.getTicks(); i++,iMin+=iStep) {
//                    iTemp.add(iMin);
//                }
//                resAxisCal.setCalibration(iTemp);
//                break;
//            case "Long":
//                long lMin = axisCal.getMin().longValue();
//                long lMax = axisCal.getMax().longValue();
//                long lStep = axisCal.getStep().longValue();
//                resAxisCal.setMin(lMin);
//                resAxisCal.setMax(lMax);
//                resAxisCal.setStep(lStep);
//                List<Object> lTemp = new ArrayList<>();
//                lTemp.add(lMin);
//                lMin += lStep;
//                for (int i = 0; i < axisCal.getTicks(); i++,lMin+=lStep) {
//                    lTemp.add(lMin);
//                }
//                resAxisCal.setCalibration(lTemp);
//                break;
//            case "Float":
//                float fMin = axisCal.getMin().floatValue();
//                float fMax = axisCal.getMax().floatValue();
//                float fStep = axisCal.getStep().floatValue();
//                resAxisCal.setMin(fMin);
//                resAxisCal.setMax(fMax);
//                resAxisCal.setStep(fStep);
//                List<Object> fTemp = new ArrayList<>();
//                fTemp.add(fMin);
//                fMin += fStep;
//                for (int i = 0; i < axisCal.getTicks(); i++,fMin+=fStep) {
//                    fTemp.add(fMin);
//                }
//                resAxisCal.setCalibration(fTemp);
//                break;
//            case "Double":
//                double dMin = axisCal.getMin();
//                double dMax = axisCal.getMax();
//                double dStep = axisCal.getStep();
//                resAxisCal.setMin(dMin);
//                resAxisCal.setMax(dMax);
//                resAxisCal.setStep(dStep);
//                List<Object> dTemp = new ArrayList<>();
//                dTemp.add(dMin);
//                dMin += dStep;
//                for (int i = 0; i < axisCal.getTicks(); i++,dMin+=dStep) {
//                    dTemp.add(dMin);
//                }
//                resAxisCal.setCalibration(dTemp);
//                break;
//            default:
//                break;
//        }
//        resAxisCal.setTicks(axisCal.getTicks());
//        resAxisCal.setType(columnType);
//        return resAxisCal;
//    }
//
//    public PivotChartAxisCalibration<Object> transferToDateAxisParameter(PivotChartAxisCalibration<Long> axisCal, String columnType){
//        PivotChartAxisCalibration<Object> resAxisCal = new PivotChartAxisCalibration<>();
//        Long min = axisCal.getMin();
//        Long max = axisCal.getMax();
//        Date minDate = new Date(min);
//        Date maxDate = new Date(max);
//        Calendar minCal = Calendar.getInstance();
//        Calendar maxCal = Calendar.getInstance();
//        minCal.setTime(minDate);
//        maxCal.setTime(maxDate);
//        List<Object> dateTemp = new ArrayList<>();
//        while (maxCal.after(minCal)){
//            dateTemp.add(minCal.getTime().getTime());
//            minCal.add(axisCal.getStep().intValue(),axisCal.getTicks());
//        }
//        dateTemp.add(maxCal.getTime().getTime());
//        resAxisCal.setCalibration(dateTemp);
//        resAxisCal.setMin(min);
//        resAxisCal.setMax(max);
//        resAxisCal.setStep(axisCal.getStep());
//        resAxisCal.setTicks(axisCal.getTicks());
//        resAxisCal.setType(columnType);
//        return resAxisCal;
//    }
//
//    public String getColumnType(String dataSource, String columnName){
//        QueryWrapper<DataSourceStatistic> dataSourceStatisticQueryWrapper = new QueryWrapper<>();
//        dataSourceStatisticQueryWrapper.eq("datasource",dataSource);
//        DataSourceStatistic dataSourceStatistic = dataSourceStatisticMapper.selectOne(dataSourceStatisticQueryWrapper);
//        return JSONObject.parseObject(dataSourceStatistic.getColumnNameType()).getString(columnName);
//    }
//
//    /**
//     *
//     */
//    public PivotChartParameters jobDescriptionToPivotChartParameters(PivotChartDescription pivotChartDescription){
//        List<Operations> operations = pivotChartDescription.getOperations();
//        String input = pivotChartDescription.getDataSource();
//        PivotChartParameters pivotChartParameters = new PivotChartParameters();
//        for (Operations operation : operations) {
//            String operationType = operation.getType();
//            Map<String, String> operMap = operation.getOperation();
//            PivotChartParameterInfo pivotChartParameterInfo = new PivotChartParameterInfo();
//            if(operMap.containsKey(ATTRIBUTE_NAME) && !"none".equals(operMap.get(ATTRIBUTE_NAME))){
//                pivotChartParameterInfo.setAttribute(operMap.get(ATTRIBUTE_NAME));
//                pivotChartParameterInfo.setType(getColumnType(input,operMap.get(ATTRIBUTE_NAME)));
//                pivotChartParameterInfo.setDataType(DataTypeEnum.COLUMN_DATATYPE_MAP.get(pivotChartParameterInfo.getType()));
//            } else{
//                throw new RuntimeException("输入错误，缺少参数：" + ATTRIBUTE_NAME);
//            }
//            if(operMap.containsKey(BINNING_NAME) && !"none".equals(operMap.get(BINNING_NAME))){
//                pivotChartParameterInfo.setBinning(operMap.get(BINNING_NAME));
//            }
//            if(operMap.containsKey(AGGREGATION_NAME) && !"none".equals(operMap.get(AGGREGATION_NAME))){
//                pivotChartParameterInfo.setAggregation(operMap.get(AGGREGATION_NAME));
//            }
//            if(operMap.containsKey(SORT_NAME) && !"none".equals(operMap.get(SORT_NAME))){
//                pivotChartParameterInfo.setSort(operMap.get(SORT_NAME));
//            }
//            switch (operationType){
//
//                case "x-axis":
//                    pivotChartParameters.setXAxis(pivotChartParameterInfo);
//                    break;
//                case "y-axis":
//                    pivotChartParameters.setYAxis(pivotChartParameterInfo);
//                    break;
//                case "color":
//                    pivotChartParameters.setColor(pivotChartParameterInfo);
//                    break;
//                case "size":
//                    pivotChartParameters.setSize(pivotChartParameterInfo);
//                    break;
//                case "row":
//                    pivotChartParameters.setRow(pivotChartParameterInfo);
//                    break;
//                case "column":
//                    pivotChartParameters.setColumn(pivotChartParameterInfo);
//                    break;
//                default:
//                    throw new RuntimeException("功能类型错误！");
//            }
//        }
//        return pivotChartParameters;
//    }
//}
