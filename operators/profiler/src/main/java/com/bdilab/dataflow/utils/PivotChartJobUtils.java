package com.bdilab.dataflow.utils;


import com.bdilab.dataflow.common.enums.DataTypeEnum;
import com.bdilab.dataflow.common.pojo.PivotChartAxisCalibration;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;



/**
 * pixochart utils.

 * @author wh,Yushaochao
 * @version 1.0
 * @date 2021/11/16
 *
 */
@Component
public  class PivotChartJobUtils {

  /**
   * Profiler sql and calibration of a column.
   *
   * @return for a column: {"calibration": ,"sql":}
   */
  public  Map<String, Object> getSqlAndCalibration(String dataSource,
                                                   Map<String, Object> columnInfo) {
    String columnName = (String) columnInfo.get("columnName");
    PivotChartAxisCalibration<Object> pixoChartAxisCal = getPivotChartAxisParameter(
        dataSource, columnInfo);
    String type = pixoChartAxisCal.getType();
    List<Object> calibration = pixoChartAxisCal.getCalibration();
    List<String> sqlList = new ArrayList<>();
    List<Object> calibrationStandardFormat = new ArrayList<>();
    Map<String, Object> result = new HashMap<>();
    switch (DataTypeEnum.CLICKHOUSE_DATATYPE_MAP.get(type)) {
      case "numeric":
        double l = Double.parseDouble(String.valueOf(pixoChartAxisCal.getMin()));
        double step = Double.parseDouble(String.valueOf(pixoChartAxisCal.getStep()));
        double r = l + step;

        for (int i = 0; i < pixoChartAxisCal.getTicks() - 1; i++, l = r, r += step) {
          StringBuilder nsql = new StringBuilder();
          nsql.append("( ").append(l).append(" <= ").append(columnName)
              .append(" and ").append(columnName).append(" < ").append(r).append(" )");
          sqlList.add(new String(nsql));
        }
        StringBuilder nsql = new StringBuilder();
        nsql.append("( ").append(l).append(" <= ").append(columnName)
            .append(" and ").append(columnName).append(" <= ").append(r).append(" )");
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
        List<Object> calList = pixoChartAxisCal.getCalibration();
        for (int i = 1; i < calList.size() - 1; i++) {
          long dl = Long.parseLong(String.valueOf(calList.get(i - 1)));
          long dr = Long.parseLong(String.valueOf(calList.get(i)));
          StringBuilder dsql = new StringBuilder();
          dsql.append("( '").append(sdf.format(new Date(dl))).append("' <= ").append(columnName)
              .append(" and ").append(columnName).append(" < '")
              .append(sdf.format(new Date(dr))).append("' )");
          sqlList.add(new String(dsql));

        }
        long dl = Long.parseLong(String.valueOf(calList.get(calList.size() - 2)));
        ;
        long dr = Long.parseLong(String.valueOf(calList.get(calList.size() - 1)));
        StringBuilder dsql = new StringBuilder();
        dsql.append("( '").append(sdf.format(new Date(dl))).append("' <= ").append(columnName)
            .append(" and ").append(columnName).append(" < '")
            .append(sdf.format(new Date(dr))).append("' )");
        sqlList.add(new String(dsql));
        for (int i = 1; i < calibration.size(); i++) {
          StringBuilder csf = new StringBuilder();

          csf.append(sdf.format(new Date(Long.parseLong(String.valueOf(calibration.get(i - 1))))))
              .append(" - ")
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

  /**
   * Profiler basic info for calibration of a column.
   *
   * @return PivotChartAxisCalibration
   */
  public PivotChartAxisCalibration<Object> getPivotChartAxisParameter(String dataSource,
                                                                 Map<String, Object> columnInfo) {

    PivotChartAxisCalibration<Object> resAxisCal = new PivotChartAxisCalibration<>();
    String columnType = (String) columnInfo.get("columnType");

    switch (DataTypeEnum.CLICKHOUSE_DATATYPE_MAP.get(columnType)) {
      case "numeric":
        double min = Double.parseDouble((String) columnInfo.get("min"));
        double max = Double.parseDouble((String) columnInfo.get("max"));
        PivotChartAxisCalibration<Double> numericAxisCal =
            AdaptiveAxisCalibrationUtils.getAdaptiveNumericAxisCalibration(min, max, 14);
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
        try {
          Date mind = sdf.parse((String) columnInfo.get("min"));
          Date maxd = sdf.parse((String) columnInfo.get("max"));
          PivotChartAxisCalibration<Long> dateAxisCal =
              AdaptiveAxisCalibrationUtils.getAdaptiveDateAxisCalibration(mind, maxd, columnType);
          resAxisCal = transferToDateAxisParameter(dateAxisCal, columnType);
        } catch (Exception e) {
          e.printStackTrace();
        }
        break;
      default:
        throw new RuntimeException("错误列属性类型");
    }
    return resAxisCal;
  }

  /**
   * Profiler calibration of a numic column.
   *
   * @return PivotChartAxisCalibration
   */
  public PivotChartAxisCalibration<Object> transferToNumericAxisParameter(
      PivotChartAxisCalibration<Double> axisCal, String columnType) {
    PivotChartAxisCalibration<Object> resAxisCal = new PivotChartAxisCalibration<>();
    switch (DataTypeEnum.CLICKHOUSE_COLUMN_DATATYPE_MAP.get(columnType)) {
      case "Integer":
        int intMin = axisCal.getMin().intValue();
        int intMax = axisCal.getMax().intValue();
        int intStep = axisCal.getStep().intValue();
        resAxisCal.setMin(intMin);
        resAxisCal.setMax(intMax);
        resAxisCal.setStep(intStep);
        List<Object> intTemp = new ArrayList<>();
        intTemp.add(intMin);
        intMin += intStep;
        for (int i = 0; i < axisCal.getTicks(); i++, intMin += intStep) {
          intTemp.add(intMin);
        }
        resAxisCal.setCalibration(intTemp);
        break;
      case "Long":
        long loMin = axisCal.getMin().longValue();
        long loMax = axisCal.getMax().longValue();
        long loStep = axisCal.getStep().longValue();
        resAxisCal.setMin(loMin);
        resAxisCal.setMax(loMax);
        resAxisCal.setStep(loStep);
        List<Object> loTemp = new ArrayList<>();
        loTemp.add(loMin);
        loMin += loStep;
        for (int i = 0; i < axisCal.getTicks(); i++, loMin += loStep) {
          loTemp.add(loMin);
        }
        resAxisCal.setCalibration(loTemp);
        break;
      case "Float":
        float flMin = axisCal.getMin().floatValue();
        float flMax = axisCal.getMax().floatValue();
        float flStep = axisCal.getStep().floatValue();
        resAxisCal.setMin(flMin);
        resAxisCal.setMax(flMax);
        resAxisCal.setStep(flStep);
        List<Object> flTemp = new ArrayList<>();
        flTemp.add(flMin);
        flMin += flStep;
        for (int i = 0; i < axisCal.getTicks(); i++, flMin += flStep) {
          flTemp.add(flMin);
        }
        resAxisCal.setCalibration(flTemp);
        break;
      case "Double":
        double douMin = axisCal.getMin();
        double douMax = axisCal.getMax();
        double douStep = axisCal.getStep();
        resAxisCal.setMin(douMin);
        resAxisCal.setMax(douMax);
        resAxisCal.setStep(douStep);
        List<Object> douTemp = new ArrayList<>();
        douTemp.add(douMin);
        douMin += douStep;
        for (int i = 0; i < axisCal.getTicks(); i++, douMin += douStep) {
          douTemp.add(douMin);
        }
        resAxisCal.setCalibration(douTemp);
        break;
      default:
        break;
    }
    resAxisCal.setTicks(axisCal.getTicks());
    resAxisCal.setType(columnType);
    return resAxisCal;
  }

  /**
   * Profiler calibration of a date column.
   *
   * @return PivotChartAxisCalibration
   */
  public PivotChartAxisCalibration<Object> transferToDateAxisParameter(
      PivotChartAxisCalibration<Long> axisCal, String columnType) {
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
    PivotChartAxisCalibration<Object> resAxisCal = new PivotChartAxisCalibration<>();
    resAxisCal.setCalibration(dateTemp);
    resAxisCal.setMin(min);
    resAxisCal.setMax(max);
    resAxisCal.setStep(axisCal.getStep());
    resAxisCal.setTicks(axisCal.getTicks());
    resAxisCal.setType(columnType);
    return resAxisCal;
  }
}

