package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.constants.AggregationConstants;
import com.bdilab.dataflow.constants.BinningConstants;
import com.bdilab.dataflow.constants.Communal;
import com.bdilab.dataflow.constants.SqlConstants;
import com.bdilab.dataflow.dto.jobdescription.Menu;
import com.bdilab.dataflow.dto.jobdescription.PivotChartDescription;
import com.bdilab.dataflow.exception.BizCodeEnum;
import com.bdilab.dataflow.exception.RRException;
import com.bdilab.dataflow.operator.dto.jobdescription.SqlGeneratorBase;
import com.bdilab.dataflow.utils.*;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;

import java.text.MessageFormat;
import java.util.*;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author : [zhangpeiliang]
 * @description : [透视图SQL解析类]
 */
public class PivotChartSqlGenerator extends SqlGeneratorBase {

  public static final String ROWS = "ROWS";

  private final String datasource;

  private final List<Menu> menus;

  private final ClickHouseJdbcUtils clickHouseJdbcUtils;

  /**
   * 存储多个等宽分箱箱子集合的ThreadLocal变量，比如[[0,50,100],[2015,1016]]存储了两个不同菜单均使用了等宽分箱后的箱子集合.
   */
  public static ThreadLocal<List<Set<Object>>> EquiWidthBinningSetThreadLocal = new ThreadLocal<>();

  /**
   * 作用同等宽分箱箱子集合的ThreadLocal变量.
   */
  public static ThreadLocal<List<Set<Object>>> NaturalBinningSetThreadLocal = new ThreadLocal<>();

  /**
   * 纯属性和分箱集合.
   */
  Set<String> attributeAndBinningSet;

  /**
   * 聚合函数集合.
   */
  Set<String> aggregationSet;

  /**
   * 排序集合.
   */
  Set<String> orderSet;

  /**
   * 分组字段集合.
   */
  Set<String> groupSet;

  /**
   * 存储等宽分箱Set集合的List集合.
   */
  List<Set<Object>> equiWidthBinningSetList = new ArrayList<>();

  /**
   * 存储自然分箱Set集合的List集合.
   */
  List<Set<Object>> naturalBinningSetList = new ArrayList<>();

  public PivotChartSqlGenerator(PivotChartDescription description) {
    super(description);
    if (description.getDataSource().length == 1) {
      this.datasource = description.getDataSource()[0];
    } else {
      this.datasource = "";
    }
    this.menus = description.getInputMenus();
    this.clickHouseJdbcUtils = SpringUtil.getBean(ClickHouseJdbcUtils.class);
    this.attributeAndBinningSet = new LinkedHashSet<>();
    this.aggregationSet = new LinkedHashSet<>();
    this.orderSet = new LinkedHashSet<>();
    this.groupSet = new LinkedHashSet<>();
  }

  @Override
  public String generate() {
    return generateSql(null);
  }

  /**
   * 生成SQL.
   */
  public String generateSql(List<String> inputBrushFilters) {
    if (StringUtils.isEmpty(this.datasource) || menus.size() == 0) {
      return "";
    }

    //对传入的inputBrushFilters进行处理
    List<String> brushFilters = null;
    if (!CollectionUtils.isEmpty(inputBrushFilters)) {
      brushFilters = new ArrayList<>();
      for (String brushFilter : inputBrushFilters) {
        List<String> seg = Arrays.asList(brushFilter.split(" AND "));
        if (seg.size() > 0) {
          StringBuilder sb = new StringBuilder();
          for (int i = 0; i < seg.size(); i++) {
            if (seg.get(i).equals("1 = 1")) {
              continue;
            }
            if (i == seg.size() - 1) {
              sb.append(seg.get(i));
            } else {
              sb.append(seg.get(i)).append(" AND ");
            }
          }
          if (sb.length() > 0) {
            brushFilters.add(sb.toString());
          }
        }
      }
    }

    //分析菜单
    for (Menu menu : menus) {
      analyzeMenu(menu);
    }

    if (groupSet.size() == 0) {
      String column = menus.get(menus.size() - 1).getAttributeRenaming();
      throw new RRException(BizCodeEnum.CANNOT_FIND_COLUMN.getMsg() + column,
              BizCodeEnum.CANNOT_FIND_COLUMN.getCode());
    }

    String sql = totalRows();
    String caseWhenSegment = "";
    if (!CollectionUtils.isEmpty(brushFilters)) {
      List<String> withBrushes = new ArrayList<>();
      sql = sql + extractWithBrushSegment(brushFilters, withBrushes);
      caseWhenSegment = extractCaseWhenSegment(withBrushes);

    }
    sql = sql + Communal.BLANK + SqlConstants.SELECT;

    String groupBy = "";
    String orderBy;
    if (StringUtils.isEmpty(extractSegment(attributeAndBinningSet))
            && StringUtils.isEmpty(extractSegment(aggregationSet))) {
      return "";
    } else {

      if (!StringUtils.isEmpty(extractSegment(attributeAndBinningSet))) {
        if (!StringUtils.isEmpty(caseWhenSegment)) {
          sql = sql + extractSegment(attributeAndBinningSet) + SqlConstants.COMMA + caseWhenSegment;
          groupBy = SqlConstants.GROUP_BY + extractSegment(groupSet) + ",brush";
        } else {
          sql = sql + extractSegment(attributeAndBinningSet);
          groupBy = SqlConstants.GROUP_BY + extractSegment(groupSet);
        }
      }

      if (!StringUtils.isEmpty(extractSegment(aggregationSet))) {
        if (!StringUtils.isEmpty(extractSegment(attributeAndBinningSet))) {
          sql = sql + SqlConstants.COMMA + extractSegment(aggregationSet);
        } else {
          sql = sql + extractSegment(aggregationSet);
        }
      }
      // orderBy
      if (!StringUtils.isEmpty(extractSegment(orderSet))) {
        orderBy = SqlConstants.ORDER_BY + extractSegment(orderSet);
      } else {
        orderBy = SqlConstants.ORDER_BY + extractSegment(groupSet);
      }

      return sql + Communal.BLANK + SqlConstants.FROM + datasource
              + Communal.BLANK + groupBy + Communal.BLANK + orderBy;

    }
  }

  /**
   * 画刷Case-When片段
   */
  private String extractCaseWhenSegment(List<String> withBrushes) {
    StringBuilder sb = new StringBuilder();
    sb.append("CASE ");
    if (withBrushes.size() > 1) {
      //处理多个画刷
      sb.append("WHEN");
      for (int i = 0; i < withBrushes.size(); i++) {
        if (i == withBrushes.size() - 1) {
          sb.append(Communal.BLANK).append(withBrushes.get(i))
                  .append(Communal.BLANK);
        } else {
          sb.append(Communal.BLANK).append(withBrushes.get(i))
                  .append(Communal.BLANK).append(SqlConstants.AND);
        }
      }
      sb.append("THEN 'overlap' ");
    }
    withBrushes.forEach(brush -> sb.append("WHEN ").append(brush).append(" THEN ")
            .append("'").append(brush).append("' "));
    sb.append("ELSE 'rest' END AS brush");
    return sb.toString();
  }

  /**
   * 从用于画刷的过滤语句集合中提取成WITH语句中的片段，同时设置画刷集合
   */
  private String extractWithBrushSegment(List<String> brushes, List<String> withBrushes) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < brushes.size(); i++) {
      sb.append(SqlConstants.COMMA).append(brushes.get(i)).append(Communal.BLANK)
              .append(SqlConstants.AS).append(Communal.BLANK).append("brush").append(i + 1);
      withBrushes.add("brush" + (i + 1));
    }
    return sb.toString();
  }

  /**
   * 从各个集合中提取成String类型的片段.
   */
  private String extractSegment(Set<String> set) {
    StringBuilder sb = new StringBuilder();
    List<String> list = new ArrayList<>(set);
    if (list.size() > 0) {
      for (int i = 0; i < list.size(); i++) {
        if (i != list.size() - 1) {
          sb.append(list.get(i)).append(SqlConstants.COMMA);
        } else {
          sb.append(list.get(i));
        }
      }
      return sb.toString();
    }
    return "";
  }

  /**
   * 如果菜单不为空，分析菜单中的选项，加到对应选项（纯属性和分箱，聚合函数，排序）集合.
   */
  private void analyzeMenu(Menu menu) {
    // 先看是否是单纯的属性，或者是属性聚合或者是属性分箱
    if (!menu.hasAggregation() && !menu.hasBinning()) {
      if (!StringUtils.isEmpty(menu.getAttribute())
              && !menu.getAttribute().equalsIgnoreCase(Communal.NONE)) {
        attributeAndBinningSet.add(menu.getAttribute());
        groupSet.add(menu.getAttribute());
        setOrder(menu, menu.getAttribute());
      }
      return;
    }
    // 菜单不止包含属性
    StringBuilder b = new StringBuilder();
    // handle binning
    if (menu.hasBinning()) {
      switch (menu.getBinning()) {
        case BinningConstants.ALPHABETIC_BINNING:
          handleAlphabeticBinning(menu, b);
          break;
        case BinningConstants.NOMINAL_BINNING:
          handleNominalBinning(menu, b);
          break;
        case BinningConstants.EQUI_WIDTH_BINNING:
          handleEquiWidthBinning(menu, b);
          break;
        case BinningConstants.NATURAL_BINNING:
          handleNaturalBinning(menu, b);
          break;
        case BinningConstants.SECOND:
        case BinningConstants.MINUTE:
        case BinningConstants.HOUR:
        case BinningConstants.DAY:
        case BinningConstants.MONTH:
        case BinningConstants.YEAR:
          handleDatetimeBinning(menu);
          break;
        default:
          throw new RRException(BizCodeEnum.INVALID_BINNING_TYPE.getMsg() + menu.getMenu(),
                  BizCodeEnum.INVALID_BINNING_TYPE.getCode());
      }

    } else if (menu.hasAggregation()) {
      // handle aggregation
      switch (menu.getAggregation()) {
        case AggregationConstants.COUNT:
          b.append(AggregationConstants.COUNT).append(SqlConstants.LEFT_BRACKET)
                  .append(menu.getAttribute()).append(SqlConstants.RIGHT_BRACKET)
                  .append(Communal.BLANK).append(menu.getAttributeRenaming());
          break;
        case AggregationConstants.DISTINCT_COUNT:
          b.append(AggregationConstants.COUNT).append(SqlConstants.LEFT_BRACKET)
                  .append(AggregationConstants.DISTINCT).append(Communal.BLANK)
                  .append(menu.getAttribute()).append(SqlConstants.RIGHT_BRACKET)
                  .append(Communal.BLANK).append(menu.getAttribute()).append(Communal.UNDER_CROSS)
                  .append(AggregationConstants.DISTINCT).append(Communal.UNDER_CROSS)
                  .append(AggregationConstants.COUNT);
          break;
        case AggregationConstants.AVERAGE:
          b.append(AggregationConstants.AVG).append(SqlConstants.LEFT_BRACKET)
                  .append(menu.getAttribute()).append(SqlConstants.RIGHT_BRACKET)
                  .append(Communal.BLANK).append(menu.getAttributeRenaming());
          break;
        case AggregationConstants.SUM:
          b.append(AggregationConstants.SUM).append(SqlConstants.LEFT_BRACKET)
                  .append(menu.getAttribute()).append(SqlConstants.RIGHT_BRACKET)
                  .append(Communal.BLANK).append(menu.getAttributeRenaming());
          break;
        case AggregationConstants.MIN:
          b.append(AggregationConstants.MIN).append(SqlConstants.LEFT_BRACKET)
                  .append(menu.getAttribute()).append(SqlConstants.RIGHT_BRACKET)
                  .append(Communal.BLANK).append(menu.getAttributeRenaming());
          break;
        case AggregationConstants.MAX:
          b.append(AggregationConstants.MAX).append(SqlConstants.LEFT_BRACKET)
                  .append(menu.getAttribute()).append(SqlConstants.RIGHT_BRACKET)
                  .append(Communal.BLANK).append(menu.getAttributeRenaming());
          break;
        case AggregationConstants.STANDARD_DEV:
          b.append(AggregationConstants.STDDEVPOP).append(SqlConstants.LEFT_BRACKET)
                  .append(menu.getAttribute()).append(SqlConstants.RIGHT_BRACKET)
                  .append(Communal.BLANK).append(menu.getAttributeRenaming());
          break;
        default:
          throw new RRException(BizCodeEnum.INVALID_AGGREGATION_TYPE.getMsg() + menu.getMenu(),
                  BizCodeEnum.INVALID_AGGREGATION_TYPE.getCode());
      }
      aggregationSet.add(b.toString());
    }
    // set ordering for both case
    setOrder(menu, menu.getAttributeRenaming());
  }


  private void handleAlphabeticBinning(Menu menu, StringBuilder b) {
    b.append(SqlConstants.SELECT).append(SqlConstants.DISTINCT).append(menu.getAttribute())
            .append(Communal.BLANK).append(SqlConstants.FROM).append(datasource);
    int index = AlphabeticBinning.commonIndex(clickHouseJdbcUtils.queryForStrList(b.toString()));
    b.delete(0, b.length());
    b.append(SqlConstants.SUBSTRING).append(SqlConstants.LEFT_BRACKET)
            .append(menu.getAttribute()).append(SqlConstants.COMMA)
            .append(1).append(SqlConstants.COMMA).append(index)
            .append(SqlConstants.RIGHT_BRACKET).append(Communal.BLANK)
            .append(menu.getAttributeRenaming());

    attributeAndBinningSet.add(b.toString());
    groupSet.add(menu.getAttributeRenaming());
  }

  private void handleNominalBinning(Menu menu, StringBuilder b) {
    b.append(menu.getAttribute()).append(Communal.BLANK).append(menu.getAttribute())
            .append(Communal.UNDER_CROSS).append(BinningConstants.BIN);
    attributeAndBinningSet.add(b.toString());
    groupSet.add(menu.getAttributeRenaming());
  }

  private void handleEquiWidthBinning(Menu menu, StringBuilder b) {
    String s1 = SqlConstants.SELECT + SqlConstants.MAX + SqlConstants.LEFT_BRACKET
            + menu.getAttribute() + SqlConstants.RIGHT_BRACKET + Communal.BLANK
            + SqlConstants.FROM + datasource;
    String s2 = SqlConstants.SELECT + SqlConstants.MIN + SqlConstants.LEFT_BRACKET
            + menu.getAttribute() + SqlConstants.RIGHT_BRACKET + Communal.BLANK
            + SqlConstants.FROM + datasource;
    Long max = clickHouseJdbcUtils.queryForLong(s1);
    Long min = clickHouseJdbcUtils.queryForLong(s2);
    //等宽分箱
    EquiWidthBinning equiWidthBinning = new EquiWidthBinning(max, min, menu.getInclude_zero());
    //获取分箱后的箱子集合
    Set<Object> binningSet = equiWidthBinning.binningSet();
    equiWidthBinningSetList.add(binningSet);
    EquiWidthBinningSetThreadLocal.set(equiWidthBinningSetList);

    b.append(SqlConstants.ROUND_DOWN).append(SqlConstants.LEFT_BRACKET).append(menu.getAttribute())
            .append(SqlConstants.COMMA).append(binningSet).append(SqlConstants.RIGHT_BRACKET)
            .append(Communal.BLANK).append(menu.getAttribute()).append(Communal.UNDER_CROSS)
            .append(BinningConstants.BIN);
    attributeAndBinningSet.add(b.toString());
    groupSet.add(menu.getAttributeRenaming());
  }

  private void handleNaturalBinning(Menu menu, StringBuilder b) {
    String ns = SqlConstants.SELECT + SqlConstants.DISTINCT + menu.getAttribute()
            + Communal.BLANK + SqlConstants.FROM + datasource;
    List<?> list = clickHouseJdbcUtils.query(ns);

    Set<Double> doubleList = new TreeSet<>(new ComparatorAsc());
    for (Object o : list) {
      doubleList.add(Double.parseDouble(o.toString()));
    }
    Double[] p = doubleList.toArray(new Double[0]);
    //获取自然分箱后的箱子集合
    Set<Object> naturalSet = KMeans.NaturalBinning(p, menu.getInclude_zero());
    naturalBinningSetList.add(naturalSet);
    NaturalBinningSetThreadLocal.set(naturalBinningSetList);

    b.append(SqlConstants.ROUND_DOWN).append(SqlConstants.LEFT_BRACKET).append(menu.getAttribute())
            .append(SqlConstants.COMMA).append(naturalSet).append(SqlConstants.RIGHT_BRACKET)
            .append(Communal.BLANK).append(menu.getAttribute()).append(Communal.UNDER_CROSS)
            .append(BinningConstants.BIN);
    attributeAndBinningSet.add(b.toString());
    groupSet.add(menu.getAttributeRenaming());
  }

  /**
   * handle datetime binning series.
   *
   * @param menu 菜单
   */
  private void handleDatetimeBinning(Menu menu) {
    DatetimeBinning binning = new DatetimeBinning(menu);
    attributeAndBinningSet.addAll(binning.binningAttributes());
    groupSet.add(menu.getAttributeRenaming());
  }

  /**
   * 设置排序集合，前提是菜单中的排序字段不为空，且不为none.
   */
  private void setOrder(Menu menu, String attribute) {
    if (!StringUtils.isEmpty(menu.getSort()) && !menu.getSort().equalsIgnoreCase(Communal.NONE)) {
      StringBuilder sb = new StringBuilder();
      switch (menu.getSort()) {
        case SqlConstants.ASC:
          sb.append(attribute).append(Communal.BLANK).append(SqlConstants.ASC);
          orderSet.add(sb.toString());
          break;
        case SqlConstants.DESC:
          sb.append(attribute).append(Communal.BLANK).append(SqlConstants.DESC);
          orderSet.add(sb.toString());
          break;
        default:
          throw new RRException(BizCodeEnum.INVALID_SORT_TYPE.getMsg() + menu.getMenu(),
                  BizCodeEnum.INVALID_SORT_TYPE.getCode());
      }
    }
  }

  /**
   * A WITH subquery returning row number in total.
   *
   * @return with sql
   */
  private String totalRows() {
    return MessageFormat.format("WITH (select count(*) from {0}) AS {1}", datasource, ROWS);
  }
}