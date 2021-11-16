package com.bdilab.dataflow.utils;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bdilab.dataflow.common.enums.DataTypeEnum;
import com.bdilab.dataflow.common.pojo.PivotChartAxisCalibration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wh
 * @version 1.0
 * @date 2021/09/16
 *
 */
@Component
public  class PivotChartJobUtils {
  public  Map<String, Object> getSqlAndCalibration(String dataSource, Map<String, Object> columnInfo) {
    String columnName = (String) columnInfo.get("columnName");
    PivotChartAxisCalibration<Object> pChartAxisCal = getPivotChartAxisParameter(dataSource, columnInfo);
    String type = pChartAxisCal.getType();
    List<Object> calibration = pChartAxisCal.getCalibration();
    List<String> sqlList = new ArrayList<>();
    List<Object> calibrationStandardFormat = new ArrayList<>();
    Map<String, Object> result = new HashMap<>();
    switch (DataTypeEnum.CLICKHOUSE_DATATYPE_MAP.get(type)) {
      case "numeric":
        double l = Double.parseDouble(String.valueOf(pChartAxisCal.getMin()));
        double step = Double.parseDouble(String.valueOf(pChartAxisCal.getStep()));
        double r = l + step;

        for (int i = 0; i < pChartAxisCal.getTicks() - 1; i++, l = r, r += step) {
          StringBuilder nsql = new StringBuilder();
          nsql.append("( ").append(l).append(" <= ").append(columnName).append(" and ").append(columnName).append(" < ").append(r).append(" )");
          sqlList.add(new String(nsql));
        }
        StringBuilder nsql = new StringBuilder();
        nsql.append("( ").append(l).append(" <= ").append(columnName).append(" and ").append(columnName).append(" <= ").append(r).append(" )");
        sqlList.add(new String(nsql));
        for (int i = 1; i < calibration.size(); i++) {
          StringBuilder csf = new StringBuilder();
          csf.append(calibration.get(i - 1)).append(" - ").append(calibration.get(i));
          calibrationStandardFormat.add(csf);
        }
        result.put("calibration", calibrationStandardFormat);
        break;
      case "string":
        StringBuilder ssql = new StringBuilder();
        ssql.append("group by ").append(columnName);
        sqlList.add(new String(ssql));
        break;
      case "date":
        SimpleDateFormat sdf;
        switch (type) {
          case "Date":
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            break;
          case "DateTime":
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            break;
          case "DateTime64":
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            break;
          default:
            throw new RuntimeException("Error type!");
        }
        List<Object> calList = pChartAxisCal.getCalibration();
        for (int i = 1; i < calList.size() - 1; i++) {
          long dl = Long.parseLong(String.valueOf(calList.get(i - 1)));
          long dr = Long.parseLong(String.valueOf(calList.get(i)));
          StringBuilder dsql = new StringBuilder();
          dsql.append("( '").append(sdf.format(new Date(dl))).append("' <= ").append(columnName)
              .append(" and ").append(columnName).append(" < '").append(sdf.format(new Date(dr))).append("' )");
          sqlList.add(new String(dsql));

        }
        long dl = Long.parseLong(String.valueOf(calList.get(calList.size() - 2)));
        ;
        long dr = Long.parseLong(String.valueOf(calList.get(calList.size() - 1)));
        StringBuilder dsql = new StringBuilder();
        dsql.append("( '").append(sdf.format(new Date(dl))).append("' <= ").append(columnName)
            .append(" and ").append(columnName).append(" < '").append(sdf.format(new Date(dr))).append("' )");
        sqlList.add(new String(dsql));
        for (int i = 1; i < calibration.size(); i++) {
          StringBuilder csf = new StringBuilder();

          csf.append(sdf.format(new Date(Long.parseLong(String.valueOf(calibration.get(i - 1)))))).append(" - ")
              .append(sdf.format(new Date(Long.parseLong(String.valueOf(calibration.get(i))))));
          calibrationStandardFormat.add(csf);
        }
        result.put("calibration", calibrationStandardFormat);
        break;
      default:
        throw new RuntimeException("错误列属性类型");
    }


    result.put("sql", sqlList);
    result.put("type", type);
    return result;
  }

  public PivotChartAxisCalibration<Object> getPivotChartAxisParameter(String dataSource, Map<String, Object> columnInfo) {

    PivotChartAxisCalibration<Object> resAxisCal = new PivotChartAxisCalibration<>();
    String columnType = (String) columnInfo.get("columnType");

    switch (DataTypeEnum.CLICKHOUSE_DATATYPE_MAP.get(columnType)) {
      case "numeric":
        double min = Double.parseDouble((String)columnInfo.get("min"));
        double max = Double.parseDouble((String)columnInfo.get("max"));
        PivotChartAxisCalibration<Double> numericAxisCal = AdaptiveAxisCalibrationUtils.getAdaptiveNumericAxisCalibration(min, max, 14);
        resAxisCal = transferToNumericAxisParameter(numericAxisCal, columnType);
        break;
      case "string":
        resAxisCal.setType(columnType);
        break;
      case "date":
        SimpleDateFormat sdf;
        switch (columnType) {
          case "Date":
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            break;
          case "DateTime":
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            break;
          case "DateTime64":
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            break;
          default:
            throw new RuntimeException("Error type!");
        }
        try{
          Date mind = sdf.parse((String)columnInfo.get("min"));
          Date maxd = sdf.parse((String)columnInfo.get("max"));
          PivotChartAxisCalibration<Long> dateAxisCal = AdaptiveAxisCalibrationUtils.getAdaptiveDateAxisCalibration(mind, maxd, columnType);
          resAxisCal = transferToDateAxisParameter(dateAxisCal, columnType);
        }catch (Exception e){
          e.printStackTrace();
        }
        break;
      default:
        throw new RuntimeException("错误列属性类型");
    }
    return resAxisCal;
  }


  public PivotChartAxisCalibration<Object> transferToNumericAxisParameter(PivotChartAxisCalibration<Double> axisCal, String columnType) {
    PivotChartAxisCalibration<Object> resAxisCal = new PivotChartAxisCalibration<>();
    switch (DataTypeEnum.CLICKHOUSE_COLUMN_DATATYPE_MAP.get(columnType)) {
      case "Integer":
        int iMin = axisCal.getMin().intValue();
        int iMax = axisCal.getMax().intValue();
        int iStep = axisCal.getStep().intValue();
        resAxisCal.setMin(iMin);
        resAxisCal.setMax(iMax);
        resAxisCal.setStep(iStep);
        List<Object> iTemp = new ArrayList<>();
        iTemp.add(iMin);
        iMin += iStep;
        for (int i = 0; i < axisCal.getTicks(); i++, iMin += iStep) {
          iTemp.add(iMin);
        }
        resAxisCal.setCalibration(iTemp);
        break;
      case "Long":
        long lMin = axisCal.getMin().longValue();
        long lMax = axisCal.getMax().longValue();
        long lStep = axisCal.getStep().longValue();
        resAxisCal.setMin(lMin);
        resAxisCal.setMax(lMax);
        resAxisCal.setStep(lStep);
        List<Object> lTemp = new ArrayList<>();
        lTemp.add(lMin);
        lMin += lStep;
        for (int i = 0; i < axisCal.getTicks(); i++, lMin += lStep) {
          lTemp.add(lMin);
        }
        resAxisCal.setCalibration(lTemp);
        break;
      case "Float":
        float fMin = axisCal.getMin().floatValue();
        float fMax = axisCal.getMax().floatValue();
        float fStep = axisCal.getStep().floatValue();
        resAxisCal.setMin(fMin);
        resAxisCal.setMax(fMax);
        resAxisCal.setStep(fStep);
        List<Object> fTemp = new ArrayList<>();
        fTemp.add(fMin);
        fMin += fStep;
        for (int i = 0; i < axisCal.getTicks(); i++, fMin += fStep) {
          fTemp.add(fMin);
        }
        resAxisCal.setCalibration(fTemp);
        break;
      case "Double":
        double dMin = axisCal.getMin();
        double dMax = axisCal.getMax();
        double dStep = axisCal.getStep();
        resAxisCal.setMin(dMin);
        resAxisCal.setMax(dMax);
        resAxisCal.setStep(dStep);
        List<Object> dTemp = new ArrayList<>();
        dTemp.add(dMin);
        dMin += dStep;
        for (int i = 0; i < axisCal.getTicks(); i++, dMin += dStep) {
          dTemp.add(dMin);
        }
        resAxisCal.setCalibration(dTemp);
        break;
      default:
        break;
    }
    resAxisCal.setTicks(axisCal.getTicks());
    resAxisCal.setType(columnType);
    return resAxisCal;
  }

  public PivotChartAxisCalibration<Object> transferToDateAxisParameter(PivotChartAxisCalibration<Long> axisCal, String columnType) {
    PivotChartAxisCalibration<Object> resAxisCal = new PivotChartAxisCalibration<>();
    Long min = axisCal.getMin();
    Long max = axisCal.getMax();
    Date minDate = new Date(min);
    Date maxDate = new Date(max);
    Calendar minCal = Calendar.getInstance();
    Calendar maxCal = Calendar.getInstance();
    minCal.setTime(minDate);
    maxCal.setTime(maxDate);
    List<Object> dateTemp = new ArrayList<>();
    while (maxCal.after(minCal)) {
      dateTemp.add(minCal.getTime().getTime());
      minCal.add(axisCal.getStep().intValue(), axisCal.getTicks());
    }
    dateTemp.add(maxCal.getTime().getTime());
    resAxisCal.setCalibration(dateTemp);
    resAxisCal.setMin(min);
    resAxisCal.setMax(max);
    resAxisCal.setStep(axisCal.getStep());
    resAxisCal.setTicks(axisCal.getTicks());
    resAxisCal.setType(columnType);
    return resAxisCal;
  }
}

