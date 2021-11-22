package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.constants.AggregationConstants;
import com.bdilab.dataflow.constants.BinningConstants;
import com.bdilab.dataflow.constants.Communal;
import com.bdilab.dataflow.constants.SQLConstants;
import com.bdilab.dataflow.dto.jobdescription.Menu;
import com.bdilab.dataflow.dto.jobdescription.PivotChartDescription;
import com.bdilab.dataflow.operator.dto.jobdescription.SqlGeneratorBase;
import com.bdilab.dataflow.utils.AlphabeticBinning;
import com.bdilab.dataflow.utils.SpringUtil;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @author : [zhangpeiliang]
 * @description : [透视图SQL解析类]
 */
public class PivotChartSqlGenerator extends SqlGeneratorBase{

    private final PivotChartDescription description;

    private final String datasource;

    private final ClickHouseJdbcUtils clickHouseJdbcUtils;

    /**
     * 纯属性和分箱集合
     */
    Set<String> attributeAndBinningSet;

    /**
     * 聚合函数集合
     */
    Set<String> aggregationSet;

    /**
     * 排序集合
     */
    Set<String> orderSet;

    /**
     * GROUP_BY参数
     */
    String GROUP_BY_ARGS = "";

    /**
     * ORDER_BY参数
     */
    String ORDER_BY_ARGS = "";

    public PivotChartSqlGenerator(PivotChartDescription description) {
        super(description);
        this.description = description;
        this.datasource = description.getDataSource();
        this.clickHouseJdbcUtils = SpringUtil.getBean(ClickHouseJdbcUtils.class);
        this.attributeAndBinningSet = new LinkedHashSet<>();
        this.aggregationSet = new LinkedHashSet<>();
        this.orderSet = new LinkedHashSet<>();
    }

    @Override
    public String generate() {
        return generateSQL();
    }

    /**
     *  生成SQL
     */
    private String generateSQL() {
        //根据选择属性的菜单数目，给集合中添加值
        for (Menu menu : description.getMenus()) {
            analyzeMenu(menu);
        }

        String SQL = SQLConstants.SELECT;
        if (StringUtils.isEmpty(extractSegment(attributeAndBinningSet)) && StringUtils.isEmpty(extractSegment(aggregationSet))) {
            throw new RuntimeException("please choose at least one attribute");
        } else {
            if (!StringUtils.isEmpty(extractSegment(attributeAndBinningSet))) {
                SQL = SQL + extractSegment(attributeAndBinningSet);
                GROUP_BY_ARGS = SQLConstants.GROUP_BY + extractAttribute(attributeAndBinningSet);
            } if (!StringUtils.isEmpty(extractSegment(aggregationSet))) {
                if (!StringUtils.isEmpty(extractSegment(attributeAndBinningSet))) {
                    SQL = SQL + SQLConstants.COMMA + extractSegment(aggregationSet);
                } else {
                    SQL = SQL + extractSegment(aggregationSet);
                }
            } if (!StringUtils.isEmpty(extractSegment(orderSet))) {
                ORDER_BY_ARGS = SQLConstants.ORDER_BY + extractSegment(orderSet);
            } else {
                ORDER_BY_ARGS = SQLConstants.ORDER_BY + extractAttribute(attributeAndBinningSet);
            }
        }

        return SQL + Communal.BLANK + SQLConstants.FROM + description.getDataSource() + Communal.BLANK + GROUP_BY_ARGS
                + Communal.BLANK + ORDER_BY_ARGS
                + Communal.BLANK + SQLConstants.LIMIT + SQLConstants.POINTS;
    }

    /**
     * 从各个集合中提取成String类型的片段
     */
    private String extractSegment(Set<String> set) {
        StringBuilder sb = new StringBuilder();
        List<String> list = new ArrayList<>(set);
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i != list.size() - 1) {
                    sb.append(list.get(i)).append(SQLConstants.COMMA);
                } else {
                    sb.append(list.get(i));
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 从attributeAndBinningSet集合提取属性用于group by和默认的order by
     */
    private String extractAttribute(Set<String> set) {
        StringBuilder sb = new StringBuilder();
        List<String> list = new ArrayList<>(set);
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                String[] s = list.get(i).split(Communal.BLANK);
                if (i != list.size() - 1) {
                    if (s.length > 1) {
                        sb.append(s[1]).append(SQLConstants.COMMA);
                    } else {
                        sb.append(list.get(i)).append(SQLConstants.COMMA);
                    }
                } else {
                    if (s.length > 1) {
                        sb.append(s[1]);
                    } else {
                        sb.append(list.get(i));
                    }
                }
            }
            return sb.toString();
        }
        return "";
    }

    /**
     * 如果菜单不为空，分析菜单中的选项，加到对应选项（纯属性和分箱，聚合函数，排序）集合
     */
    private void analyzeMenu(Menu menu) {
        if (menu != null) {
            //先看是否是单纯的属性，或者是属性聚合或者是属性分箱
            if (menu.getBinning().equalsIgnoreCase(Communal.NONE) && menu.getAggregation().equalsIgnoreCase(Communal.NONE)) {
                if (!StringUtils.isEmpty(menu.getAttribute()) && !menu.getAttribute().equalsIgnoreCase(Communal.NONE)) {
                    attributeAndBinningSet.add(menu.getAttribute());
                    setOrder(menu, menu.getAttribute());
                }
            } else {
                //菜单不止包含属性
                StringBuilder sb1 = new StringBuilder();
                StringBuilder sb2 = new StringBuilder();
                if (!StringUtils.isEmpty(menu.getBinning()) && !menu.getBinning().equalsIgnoreCase(Communal.NONE)) {
                    //TODO 数据分箱菜单
                    switch (menu.getBinning()) {
                        case BinningConstants.ALPHABETIC_BINNING:
                            sb1.append(SQLConstants.SELECT).append(SQLConstants.DISTINCT).append(menu.getAttribute())
                                    .append(Communal.BLANK).append(SQLConstants.FROM).append(datasource);
                            int index = AlphabeticBinning.commonIndex(clickHouseJdbcUtils.queryForStrList(sb1.toString()));
                            sb1.delete(0,sb1.length());
                            sb1.append(SQLConstants.SUBSTRING).append(SQLConstants.LEFT_BRACKET).append(menu.getAttribute()).append(SQLConstants.COMMA)
                                    .append(1).append(SQLConstants.COMMA).append(index).append(SQLConstants.RIGHT_BRACKET).append(Communal.BLANK)
                                    .append(menu.getAttribute()).append(Communal.UNDER_CROSS).append(BinningConstants.BIN);
                            sb2.append(menu.getAttribute()).append(Communal.UNDER_CROSS).append(BinningConstants.BIN);
                            attributeAndBinningSet.add(sb1.toString());
                            setOrder(menu, sb2.toString());
                            break;
                        case BinningConstants.NOMINAL_BINNING:
                            break;
                        default:
                            throw new RuntimeException("wrong binning type!");
                    }

                } else if (!StringUtils.isEmpty(menu.getAggregation()) && !menu.getAggregation().equalsIgnoreCase(Communal.NONE)) {
                    switch (menu.getAggregation()) {
                        case AggregationConstants.COUNT:
                            sb1.append(AggregationConstants.COUNT).append(SQLConstants.LEFT_BRACKET).append(menu.getAttribute()).append(SQLConstants.RIGHT_BRACKET)
                                    .append(Communal.BLANK).append(menu.getAttribute()).append(Communal.UNDER_CROSS).append(AggregationConstants.COUNT);
                            sb2.append(menu.getAttribute()).append(Communal.UNDER_CROSS).append(AggregationConstants.COUNT);
                            aggregationSet.add(sb1.toString());
                            setOrder(menu, sb2.toString());
                            break;
                        case AggregationConstants.DISTINCT_COUNT:
                            sb1.append(AggregationConstants.COUNT).append(SQLConstants.LEFT_BRACKET).append(AggregationConstants.DISTINCT).append(Communal.BLANK)
                                    .append(menu.getAttribute()).append(SQLConstants.RIGHT_BRACKET).append(Communal.BLANK).append(menu.getAttribute()).append(Communal.UNDER_CROSS)
                                    .append(AggregationConstants.DISTINCT).append(Communal.UNDER_CROSS).append(AggregationConstants.COUNT);
                            sb2.append(menu.getAttribute()).append(Communal.UNDER_CROSS).append(AggregationConstants.DISTINCT).append(Communal.UNDER_CROSS).append(AggregationConstants.COUNT);
                            aggregationSet.add(sb1.toString());
                            setOrder(menu, sb2.toString());
                            break;
                        case AggregationConstants.AVERAGE:
                            sb1.append(AggregationConstants.AVG).append(SQLConstants.LEFT_BRACKET).append(menu.getAttribute()).append(SQLConstants.RIGHT_BRACKET).append(Communal.BLANK)
                                    .append(menu.getAttribute()).append(Communal.UNDER_CROSS).append(AggregationConstants.AVERAGE);
                            sb2.append(menu.getAttribute()).append(Communal.UNDER_CROSS).append(AggregationConstants.AVERAGE);
                            aggregationSet.add(sb1.toString());
                            setOrder(menu, sb2.toString());
                            break;
                        case AggregationConstants.SUM:
                            sb1.append(AggregationConstants.SUM).append(SQLConstants.LEFT_BRACKET).append(menu.getAttribute()).append(SQLConstants.RIGHT_BRACKET).append(Communal.BLANK)
                                    .append(menu.getAttribute()).append(Communal.UNDER_CROSS).append(AggregationConstants.SUM);
                            sb2.append(menu.getAttribute()).append(Communal.UNDER_CROSS).append(AggregationConstants.SUM);
                            aggregationSet.add(sb1.toString());
                            setOrder(menu, sb2.toString());
                            break;
                        case AggregationConstants.MIN:
                            sb1.append(AggregationConstants.MIN).append(SQLConstants.LEFT_BRACKET).append(menu.getAttribute()).append(SQLConstants.RIGHT_BRACKET).append(Communal.BLANK)
                                    .append(menu.getAttribute()).append(Communal.UNDER_CROSS).append(AggregationConstants.MIN);
                            sb2.append(menu.getAttribute()).append(Communal.UNDER_CROSS).append(AggregationConstants.MIN);
                            aggregationSet.add(sb1.toString());
                            setOrder(menu, sb2.toString());
                            break;
                        case AggregationConstants.MAX:
                            sb1.append(AggregationConstants.MAX).append(SQLConstants.LEFT_BRACKET).append(menu.getAttribute()).append(SQLConstants.RIGHT_BRACKET).append(Communal.BLANK)
                                    .append(menu.getAttribute()).append(Communal.UNDER_CROSS).append(AggregationConstants.MAX);
                            sb2.append(menu.getAttribute()).append(Communal.UNDER_CROSS).append(AggregationConstants.MAX);
                            aggregationSet.add(sb1.toString());
                            setOrder(menu, sb2.toString());
                            break;
                        case AggregationConstants.STANDARD_DEV:
                            sb1.append(AggregationConstants.STDDEVPOP).append(SQLConstants.LEFT_BRACKET).append(menu.getAttribute()).append(SQLConstants.RIGHT_BRACKET).append(Communal.BLANK)
                                    .append(menu.getAttribute()).append(Communal.UNDER_CROSS).append(AggregationConstants.STD);
                            sb2.append(menu.getAttribute()).append(Communal.UNDER_CROSS).append(AggregationConstants.STD);
                            aggregationSet.add(sb1.toString());
                            setOrder(menu, sb2.toString());
                            break;
                        default:
                            throw new RuntimeException("wrong aggregation type!");
                    }
                }
            }
        }
    }

    /**
     * 设置排序集合，前提是菜单中的排序字段不为空，且不为none
     */
    private void setOrder(Menu menu, String attribute) {
        if (!StringUtils.isEmpty(menu.getSort()) && !menu.getSort().equalsIgnoreCase(Communal.NONE)) {
            StringBuilder sb = new StringBuilder();
            switch (menu.getSort()) {
                case SQLConstants.ASC:
                    sb.append(attribute).append(Communal.BLANK).append(SQLConstants.ASC);
                    orderSet.add(sb.toString());
                    break;
                case SQLConstants.DESC:
                    sb.append(attribute).append(Communal.BLANK).append(SQLConstants.DESC);
                    orderSet.add(sb.toString());
                    break;
                default:
                    throw new RuntimeException("wrong sort type!");
            }
        }
    }
}
