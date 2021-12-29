package com.bdilab.dataflow.service.impl;

import com.bdilab.dataflow.constants.AggregationConstants;
import com.bdilab.dataflow.constants.BinningConstants;
import com.bdilab.dataflow.constants.Communal;
import com.bdilab.dataflow.constants.SqlConstants;
import com.bdilab.dataflow.dto.jobdescription.Menu;
import com.bdilab.dataflow.dto.jobdescription.PivotChartDescription;
import com.bdilab.dataflow.dto.joboutputjson.ParamTypeRespObj;
import com.bdilab.dataflow.dto.joboutputjson.RespObj;
import com.bdilab.dataflow.exception.BizCodeEnum;
import com.bdilab.dataflow.exception.RunException;
import com.bdilab.dataflow.service.PivotChartService;
import com.bdilab.dataflow.sql.generator.PivotChartSqlGenerator;
import com.bdilab.dataflow.utils.ComparatorAsc;
import com.bdilab.dataflow.utils.ComparatorDesc;
import com.bdilab.dataflow.utils.DataUtil;
import com.bdilab.dataflow.utils.DatetimeBinning;
import com.bdilab.dataflow.utils.LocalUtil;
import com.bdilab.dataflow.utils.R;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Service实现类.
 * @ author: [zhangpeiliang]
 */
@Slf4j
@Service
public class PivotChartServiceImpl implements PivotChartService {

  @Autowired
  private ClickHouseJdbcUtils clickHouseJdbcUtils;

  @SuppressWarnings("unused")
  @Override
  public R getPivotChart(PivotChartDescription description) {
    //请求对象效验
    verify(description);

    //通过SQL解析类获取完整SQL
    PivotChartSqlGenerator pivotChartSqlGenerator = new PivotChartSqlGenerator(description);
    String sql = pivotChartSqlGenerator.generate();

    //封装的结果集合
    List<Object> results = new ArrayList<>();

    //对每个菜单进行处理
    for (Menu menu : description.getMenus()) {
      //如果菜单属性为none，直接跳过该菜单，处理下一个菜单
      if (StringUtils.isEmpty(menu.getAttribute())
              || menu.getAttribute().equalsIgnoreCase(Communal.NONE)) {
        continue;
      }

      // reuse the getAttributeRenaming() method
      String menuSb = SqlConstants.SELECT + menu.getAttributeRenaming() + Communal.BLANK
              + SqlConstants.FROM + SqlConstants.LEFT_BRACKET + sql + SqlConstants.RIGHT_BRACKET;

      log.info(MessageFormat.format("[Menu StringBuilder SQL]: {0}", menuSb));
      List<?> columnList = clickHouseJdbcUtils.query(menuSb);

      //响应对象
      RespObj respObj = new RespObj();
      respObj.setMenu(menu.getMenu());
      respObj.setName(menu.getAttributeRenaming());

      //处理排序
      handleSort(menu, respObj, columnList);

      //处理分箱
      if (!StringUtils.isEmpty(menu.getBinning())
              && !menu.getBinning().equalsIgnoreCase(Communal.NONE)) {
        switch (menu.getBinning()) {
          case BinningConstants.ALPHABETIC_BINNING:
            respObj.setValues(columnList.stream().map(item -> menu.getAttribute() + Communal.BLANK
                    + Communal.STARTS_WITH + Communal.BLANK + item).collect(Collectors.toList()));
            break;
          case BinningConstants.NOMINAL_BINNING:
            respObj.setValues(columnList.stream().map(item -> menu.getAttribute() + Communal.BLANK
                    + SqlConstants.EQUAL + Communal.BLANK + item).collect(Collectors.toList()));
            break;
          case BinningConstants.EQUI_WIDTH_BINNING:
            List<Set<Object>> equiWidthBinningSetList
                    = PivotChartSqlGenerator.EquiWidthBinningSetThreadLocal.get();
            Set<Object> equiWidthBinningSet = equiWidthBinningSetList.get(0);
            equiWidthBinningSetList.remove(0);
            //等宽分箱，箱子集合排序要单独处理
            handleSort(menu, respObj, equiWidthBinningSet);
            //将值所在的箱子封装
            respObj.setValues(DataUtil.values(columnList, equiWidthBinningSet));
            break;
          case BinningConstants.NATURAL_BINNING:
            List<Set<Object>> naturalBinningSetList
                    = PivotChartSqlGenerator.NaturalBinningSetThreadLocal.get();
            Set<Object> naturalBinningSet = naturalBinningSetList.get(0);
            naturalBinningSetList.remove(0);
            //自然分箱，箱子集合排序要单独处理
            handleSort(menu, respObj, naturalBinningSet);
            //将值所在的箱子封装
            respObj.setValues(DataUtil.values(columnList, naturalBinningSet));
            break;
          case BinningConstants.SECOND:
          case BinningConstants.MINUTE:
          case BinningConstants.HOUR:
          case BinningConstants.DAY:
          case BinningConstants.MONTH:
          case BinningConstants.YEAR:
            List<String> dateTimeSetList = DatetimeBinning.DateTimeListThreadLocal.get();
            String params = dateTimeSetList.get(0);
            dateTimeSetList.remove(0);
            String dateTimeSql = SqlConstants.SELECT + params + Communal.BLANK + SqlConstants.FROM
                    + SqlConstants.LEFT_BRACKET + sql + SqlConstants.RIGHT_BRACKET;
            log.info(MessageFormat.format("[DateTime SQL]: {0}", dateTimeSql));
            List<Map<String, Object>> mapList = clickHouseJdbcUtils.queryForList(dateTimeSql);
            respObj.setValues(getDate(mapList));
            break;
          default:
            throw new RunException(BizCodeEnum.INVALID_BINNING_TYPE.getMsg(),
                    BizCodeEnum.INVALID_BINNING_TYPE.getCode());
        }
      } else {
        respObj.setValues(columnList);
      }

      respObj.setSize(respObj.getValues().size());
      results.add(respObj);
    }

    R r = new R();
    r.put(Communal.RESULTS, results);
    r.put(Communal.TRUNCATED_NUM, PivotChartSqlGenerator.TRUNCATED_NUM);
    return r;
  }

  /**
   * 对日期分箱数据进行处理.
   */
  private List<Object> getDate(List<Map<String, Object>> mapList) {
    List<Object> dateList = new ArrayList<>();
    for (Map<String, Object> map : mapList) {
      Object from = map.values().toArray()[0];
      Object to = map.values().toArray()[map.size() - 1];
      List<Object> list = new ArrayList<>();
      list.add(from);
      list.add(to);
      dateList.add(list);
    }
    return dateList;
  }

  /**
   * 处理排序.
   */
  private void handleSort(Menu menu, RespObj respObj, Collection<?> columnList) {
    if (!StringUtils.isEmpty(menu.getSort())) {
      switch (menu.getSort()) {
        case Communal.NONE:
        case SqlConstants.ASC:
          //菜单distinct值升序排列
          Set<Object> distinctSetAsc = new TreeSet<>(new ComparatorAsc());
          distinctSetAsc.addAll(columnList);
          respObj.setDistinctValues(distinctSetAsc);
          break;
        case SqlConstants.DESC:
          //菜单distinct值降序排列
          Set<Object> distinctSetDesc = new TreeSet<>(new ComparatorDesc());
          distinctSetDesc.addAll(columnList);
          respObj.setDistinctValues(distinctSetDesc);
          break;
        default:
          throw new RunException(BizCodeEnum.INVALID_SORT_TYPE.getMsg(),
                  BizCodeEnum.INVALID_SORT_TYPE.getCode());
      }
    }
  }

  /**
   * 联动场景的pivot-chart，pivot-chart输出同filter，无需保存到clickhouse，只返回节点数据.
   * @ param description chart描述.
   * @ param brushFilters 用于画刷的过滤条件集合.
   * @ return 响应数据.
   */
  @Override
  public List<Map<String, Object>> saveToClickHouse(PivotChartDescription description,
                                                    List<String> brushFilters) {
    //请求对象效验
    verify(description);

    //最终返回的结果集合
    List<Map<String, Object>> results = new ArrayList<>();

    //集合中第一个map为信息map，返回菜单选择信息
    Map<String, Object> infoMap = description.getInfoMap();
    results.add(infoMap);

    //sql生成
    PivotChartSqlGenerator pivotChartSqlGenerator = new PivotChartSqlGenerator(description);
    String sql = pivotChartSqlGenerator.generateSql(brushFilters);
    if (!StringUtils.isEmpty(sql)) {
      List<Map<String, Object>> queryListMap = clickHouseJdbcUtils.queryForList(sql);
      if (queryListMap.size() > 0) {
        //对每个菜单进行处理
        for (Menu menu : description.getInputMenus()) {
          //如果菜单属性为none，直接跳过该菜单，处理下一个菜单
          if (StringUtils.isEmpty(menu.getAttribute())
                  || menu.getAttribute().equalsIgnoreCase(Communal.NONE)) {
            continue;
          }

          if (!StringUtils.isEmpty(menu.getBinning())
                  && !menu.getBinning().equalsIgnoreCase(Communal.NONE)) {
            StringBuilder sb;
            String renaming = menu.getAttributeRenaming();
            for (Map<String, Object> map : queryListMap) {
              switch (menu.getBinning()) {
                case BinningConstants.ALPHABETIC_BINNING:
                  sb = new StringBuilder();
                  map.put(renaming, sb.append(menu.getAttribute()).append(Communal.BLANK)
                          .append(Communal.STARTS_WITH).append(Communal.BLANK)
                          .append(map.get(renaming)));
                  break;
                case BinningConstants.NOMINAL_BINNING:
                  sb = new StringBuilder();
                  map.put(renaming, sb.append(menu.getAttribute()).append(Communal.BLANK)
                          .append(SqlConstants.EQUAL).append(Communal.BLANK)
                          .append(map.get(renaming)));
                  break;
                case BinningConstants.EQUI_WIDTH_BINNING:
                  handleBinning(renaming, map,
                          PivotChartSqlGenerator.EquiWidthBinningSetThreadLocal.get());
                  break;
                case BinningConstants.NATURAL_BINNING:
                  handleBinning(renaming, map,
                          PivotChartSqlGenerator.NaturalBinningSetThreadLocal.get());
                  break;
                case BinningConstants.SECOND:
                case BinningConstants.MINUTE:
                case BinningConstants.HOUR:
                case BinningConstants.DAY:
                case BinningConstants.MONTH:
                case BinningConstants.YEAR:
                  List<Object> dateBinningList = new ArrayList<>();
                  String dateRight = renaming + "_RIGHT";
                  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                  dateBinningList.add(sdf.format(map.get(renaming)));
                  dateBinningList.add(sdf.format(map.get(dateRight)));
                  map.remove(dateRight);
                  map.put(renaming, dateBinningList);
                  break;
                default:
                  throw new RunException(BizCodeEnum.INVALID_BINNING_TYPE.getMsg(),
                          BizCodeEnum.INVALID_BINNING_TYPE.getCode());
              }
            }
            switch (menu.getBinning()) {
              case BinningConstants.EQUI_WIDTH_BINNING:
                PivotChartSqlGenerator.EquiWidthBinningSetThreadLocal.get().remove(0);
                break;
              case BinningConstants.NATURAL_BINNING:
                PivotChartSqlGenerator.NaturalBinningSetThreadLocal.get().remove(0);
                break;
              case BinningConstants.SECOND:
              case BinningConstants.MINUTE:
              case BinningConstants.HOUR:
              case BinningConstants.DAY:
              case BinningConstants.MONTH:
              case BinningConstants.YEAR:
                DatetimeBinning.DateTimeListThreadLocal.get().remove(0);
                break;
              default:
                break;
            }
          }
        }
        results.addAll(queryListMap);
      }
    }
    //信息map放入计算后的截断数据
    infoMap.put(Communal.TRUNCATED_NUM, PivotChartSqlGenerator.TRUNCATED_NUM);
    //如果brushFilters集合不为空，color菜单前端不显示，用于展示brush
    if (!CollectionUtils.isEmpty(PivotChartSqlGenerator.brushFilters)) {
      infoMap.put("color", "brush");
    }
    //完成封装后，清空已有数据，准备下一次计算
    PivotChartSqlGenerator.TRUNCATED_NUM = 0L;
    PivotChartSqlGenerator.brushFilters = null;
    return results;
  }

  private void handleBinning(String renaming, Map<String, Object> map,
                             List<Set<Object>> binningSetList) {
    if (binningSetList.size() > 0) {
      Set<Object> binningSet = binningSetList.get(0);
      List<Object> binningList = new ArrayList<>(binningSet);
      int preIndex = binningList.indexOf(map.get(renaming));
      int nextIndex = preIndex + 1;
      List<Object> resultBinningList = new ArrayList<>();
      resultBinningList.add(binningList.get(preIndex));
      resultBinningList.add(binningList.get(nextIndex));
      map.put(renaming, resultBinningList);
    }
  }

  @Override
  public ParamTypeRespObj getType(String type, String language) {
    ParamTypeRespObj respObj = new ParamTypeRespObj();
    List<String> strList = new ArrayList<>();
    List<String> booleanOrDateList = new ArrayList<>();
    List<String> numericList = new ArrayList<>();
    if (language.equalsIgnoreCase("english")) {
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
          respObj.setBooleans(booleanOrDateList);

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
          respObj.setBooleans(strList);

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
          throw new RunException("wrong type!");
      }
    } else if (language.equalsIgnoreCase("chinese")) {
      switch (type) {
        case AggregationConstants.AGGREGATION:
          strList.add(LocalUtil.getEnVersion("pivot.none"));
          strList.add(LocalUtil.getEnVersion("pivot.count"));
          strList.add(LocalUtil.getEnVersion("pivot.distinct_count"));
          respObj.setString(strList);
          respObj.setDate(strList);

          booleanOrDateList.add(LocalUtil.getEnVersion(Communal.NONE));
          booleanOrDateList.add(LocalUtil.getEnVersion("pivot.count"));
          booleanOrDateList.add(LocalUtil.getEnVersion("pivot.distinct_count"));
          booleanOrDateList.add(LocalUtil.getEnVersion("pivot.average"));
          respObj.setBooleans(booleanOrDateList);

          numericList.add(LocalUtil.getEnVersion(Communal.NONE));
          numericList.add(LocalUtil.getEnVersion("pivot.count"));
          numericList.add(LocalUtil.getEnVersion("pivot.distinct_count"));
          numericList.add(LocalUtil.getEnVersion("pivot.average"));
          numericList.add(LocalUtil.getEnVersion("pivot.sum"));
          numericList.add(LocalUtil.getEnVersion("pivot.min"));
          numericList.add(LocalUtil.getEnVersion("pivot.max"));
          numericList.add(LocalUtil.getEnVersion("pivot.standard_dev"));
          respObj.setNumeric(numericList);
          break;
        case BinningConstants.BIN:
          strList.add(LocalUtil.getEnVersion("pivot.none"));
          strList.add(LocalUtil.getEnVersion("pivot.AlphabeticBinning"));
          strList.add(LocalUtil.getEnVersion("pivot.NominalBinning"));
          respObj.setString(strList);
          respObj.setBooleans(strList);

          numericList.add(LocalUtil.getEnVersion("pivot.none"));
          numericList.add(LocalUtil.getEnVersion("pivot.EquiWidthBinning"));
          numericList.add(LocalUtil.getEnVersion("pivot.NaturalBinning"));
          respObj.setNumeric(numericList);

          booleanOrDateList.add(LocalUtil.getEnVersion("pivot.none"));
          booleanOrDateList.add(LocalUtil.getEnVersion("pivot.second"));
          booleanOrDateList.add(LocalUtil.getEnVersion("pivot.minute"));
          booleanOrDateList.add(LocalUtil.getEnVersion("pivot.hour"));
          booleanOrDateList.add(LocalUtil.getEnVersion("pivot.day"));
          booleanOrDateList.add(LocalUtil.getEnVersion("pivot.month"));
          booleanOrDateList.add(LocalUtil.getEnVersion("pivot.year"));
          respObj.setDate(booleanOrDateList);
          break;
        default:
          throw new RunException("wrong type!");
      }
    }
    return respObj;
  }

  /**
   * 请求对象效验.
   */
  private void verify(PivotChartDescription description) {
    Set<String> menus = new HashSet<>();
    if (description.getMenus().length != 6) {
      throw new RunException(BizCodeEnum.MENU_NUM_EXCEPTION.getMsg(),
              BizCodeEnum.MENU_NUM_EXCEPTION.getCode());
    }
    for (Menu menu : description.getMenus()) {
      String menuName = menu.getMenu();
      menus.add(menuName);
      if (!StringUtils.isEmpty(menu.getAttribute()) && !menu.getAttribute().equals(Communal.NONE)) {
        if (!StringUtils.isEmpty(menu.getBinning()) && !menu.getBinning().equals(Communal.NONE)
                && !StringUtils.isEmpty(menu.getAggregation())
                && !menu.getAggregation().equals(Communal.NONE)) {
          throw new RunException(menuName + BizCodeEnum.MENU_BOTH_BIN_AGR_EXCEPTION.getMsg(),
                  BizCodeEnum.MENU_BOTH_BIN_AGR_EXCEPTION.getCode());
        }
        checkType(menu);
      } else {
        if (!StringUtils.isEmpty(menu.getBinning()) && !menu.getBinning().equals(Communal.NONE)
                || !StringUtils.isEmpty(menu.getAggregation())
                && !menu.getAggregation().equals(Communal.NONE)
                || !StringUtils.isEmpty(menu.getSort())
                && !menu.getSort().equals(Communal.NONE)) {
          throw new RunException(menuName + BizCodeEnum.MENU_NONE_ATTRIBUTE_EXCEPTION.getMsg(),
                  BizCodeEnum.MENU_NONE_ATTRIBUTE_EXCEPTION.getCode());
        }
      }
    }
    if (menus.size() != 6) {
      throw new RunException(BizCodeEnum.MENU_REPEAT_EXCEPTION.getMsg(),
              BizCodeEnum.MENU_REPEAT_EXCEPTION.getCode());
    }
  }

  private void checkType(Menu menu) {
    if (!StringUtils.isEmpty(menu.getBinning()) && !menu.getBinning().equals(Communal.NONE)) {
      switch (menu.getBinning()) {
        case BinningConstants.ALPHABETIC_BINNING:
        case BinningConstants.NOMINAL_BINNING:
        case BinningConstants.EQUI_WIDTH_BINNING:
        case BinningConstants.NATURAL_BINNING:
        case BinningConstants.SECOND:
        case BinningConstants.MINUTE:
        case BinningConstants.HOUR:
        case BinningConstants.DAY:
        case BinningConstants.MONTH:
        case BinningConstants.YEAR:
          break;
        default:
          throw new RunException(BizCodeEnum.INVALID_BINNING_TYPE.getMsg() + menu.getMenu(),
                  BizCodeEnum.INVALID_BINNING_TYPE.getCode());
      }
    }

    if (!StringUtils.isEmpty(menu.getAggregation())
            && !menu.getAggregation().equals(Communal.NONE)) {
      switch (menu.getAggregation()) {
        case AggregationConstants.COUNT:
        case AggregationConstants.DISTINCT_COUNT:
        case AggregationConstants.AVERAGE:
        case AggregationConstants.MAX:
        case AggregationConstants.MIN:
        case AggregationConstants.SUM:
        case AggregationConstants.STANDARD_DEV:
          break;
        default:
          throw new RunException(BizCodeEnum.INVALID_AGGREGATION_TYPE.getMsg() + menu.getMenu(),
                  BizCodeEnum.INVALID_AGGREGATION_TYPE.getCode());
      }
    }

    if (!StringUtils.isEmpty(menu.getSort()) && !menu.getSort().equals(Communal.NONE)) {
      switch (menu.getSort()) {
        case SqlConstants.ASC:
        case SqlConstants.DESC:
          break;
        default:
          throw new RunException(BizCodeEnum.INVALID_SORT_TYPE.getMsg() + menu.getMenu(),
                  BizCodeEnum.INVALID_SORT_TYPE.getCode());
      }
    }
  }
}