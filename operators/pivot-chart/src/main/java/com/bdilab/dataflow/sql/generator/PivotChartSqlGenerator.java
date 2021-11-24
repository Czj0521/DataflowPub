package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.constants.AggregationConstants;
import com.bdilab.dataflow.constants.BinningConstants;
import com.bdilab.dataflow.constants.Communal;
import com.bdilab.dataflow.constants.SQLConstants;
import com.bdilab.dataflow.dto.jobdescription.Menu;
import com.bdilab.dataflow.dto.jobdescription.PivotChartDescription;
import com.bdilab.dataflow.operator.dto.jobdescription.SqlGeneratorBase;
import com.bdilab.dataflow.utils.AlphabeticBinning;
import com.bdilab.dataflow.utils.DatetimeBinning;
import com.bdilab.dataflow.utils.SpringUtil;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import org.springframework.util.StringUtils;

import java.text.MessageFormat;
import java.util.*;

/**
 * @author : [zhangpeiliang]
 * @description : [透视图SQL解析类]
 */
public class PivotChartSqlGenerator extends SqlGeneratorBase {

    public final static String ROWS = "ROWS";

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
     * 分组字段集合
     */
    Set<String> groupSet;

    // whether there is any aggregation that requires group operation.
    // no need to group if there is no aggregation.
    private Boolean aggregatable;

    public PivotChartSqlGenerator(PivotChartDescription description) {
        super(description);
        this.description = description;
        this.datasource = description.getDataSource();
        this.clickHouseJdbcUtils = SpringUtil.getBean(ClickHouseJdbcUtils.class);
        this.attributeAndBinningSet = new LinkedHashSet<>();
        this.aggregationSet = new LinkedHashSet<>();
        this.orderSet = new LinkedHashSet<>();
        this.groupSet = new LinkedHashSet<>();
        aggregatable = Arrays.stream(description.getMenus()).anyMatch(Menu::hasAggregation);
        System.out.println(MessageFormat.format("Aggregatable: {0}", aggregatable));
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

        String SQL = totalRows() + Communal.BLANK + SQLConstants.SELECT;
        String groupBy;
        String orderBy;
        if (StringUtils.isEmpty(extractSegment(attributeAndBinningSet)) && StringUtils.isEmpty(extractSegment(aggregationSet))) {
            throw new RuntimeException("please choose at least one attribute");
        } else {

            if (!StringUtils.isEmpty(extractSegment(attributeAndBinningSet))) {
                SQL = SQL + extractSegment(attributeAndBinningSet);
            }
            groupBy = groupSet.isEmpty()? "": SQLConstants.GROUP_BY + extractSegment(groupSet);
            if (!StringUtils.isEmpty(extractSegment(aggregationSet))) {
                if (!StringUtils.isEmpty(extractSegment(attributeAndBinningSet))) {
                    SQL = SQL + SQLConstants.COMMA + extractSegment(aggregationSet);
                } else {
                    SQL = SQL + extractSegment(aggregationSet);
                }
            }
            // orderSet is always not empty now
            orderBy = SQLConstants.ORDER_BY + extractSegment(orderSet);

        }


        return SQL + Communal.BLANK + SQLConstants.FROM + description.getDataSource() + Communal.BLANK + groupBy
                + Communal.BLANK + orderBy
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
        if (menu == null) {
            return;
        }

        // 先看是否是单纯的属性，或者是属性聚合或者是属性分箱
        if (!menu.hasAggregation() && !menu.hasBinning()) {
            if (!StringUtils.isEmpty(menu.getAttribute()) && !menu.getAttribute().equalsIgnoreCase(Communal.NONE)) {
                attributeAndBinningSet.add(menu.getAttribute());
                setOrder(menu, menu.getAttribute());
                if (aggregatable) {
                    addGroup(menu.getAttributeRenaming());
                }
            }
            return;
        }
        // 菜单不止包含属性
        StringBuilder b = new StringBuilder();
        // handle binning
        if (menu.hasBinning()) {
            // TODO 数据分箱菜单
            switch (menu.getBinning()) {
                case BinningConstants.ALPHABETIC_BINNING:
                    b.append(SQLConstants.SELECT).append(SQLConstants.DISTINCT).append(menu.getAttribute())
                        .append(Communal.BLANK).append(SQLConstants.FROM).append(datasource);
                    int index = AlphabeticBinning.commonIndex(clickHouseJdbcUtils.queryForStrList(b.toString()));
                    b.delete(0,b.length());
                    b.append(SQLConstants.SUBSTRING).append(SQLConstants.LEFT_BRACKET).append(menu.getAttribute()).append(SQLConstants.COMMA)
                        .append(1).append(SQLConstants.COMMA).append(index).append(SQLConstants.RIGHT_BRACKET).append(Communal.BLANK)
                        .append(menu.getAttributeRenaming());

                    attributeAndBinningSet.add(b.toString());

                    break;
                case BinningConstants.NOMINAL_BINNING:
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
                    throw new RuntimeException("wrong binning type!");
            }
            if (aggregatable) {
                addGroup(menu.getAttributeRenaming());
            }

        } else if (menu.hasAggregation()) {
            // handle aggregation
            switch (menu.getAggregation()) {
                case AggregationConstants.COUNT:
                    b.append(AggregationConstants.COUNT).append(SQLConstants.LEFT_BRACKET).append(menu.getAttribute()).append(SQLConstants.RIGHT_BRACKET)
                        .append(Communal.BLANK).append(menu.getAttributeRenaming());
                    break;
                case AggregationConstants.DISTINCT_COUNT:
                    b.append(AggregationConstants.COUNT).append(SQLConstants.LEFT_BRACKET).append(AggregationConstants.DISTINCT).append(Communal.BLANK)
                        .append(menu.getAttribute()).append(SQLConstants.RIGHT_BRACKET).append(Communal.BLANK).append(menu.getAttribute()).append(Communal.UNDER_CROSS)
                        .append(AggregationConstants.DISTINCT).append(Communal.UNDER_CROSS).append(AggregationConstants.COUNT);
                    break;
                case AggregationConstants.AVERAGE:
                    b.append(AggregationConstants.AVG).append(SQLConstants.LEFT_BRACKET).append(menu.getAttribute()).append(SQLConstants.RIGHT_BRACKET).append(Communal.BLANK)
                        .append(menu.getAttributeRenaming());
                    break;
                case AggregationConstants.SUM:
                    b.append(AggregationConstants.SUM).append(SQLConstants.LEFT_BRACKET).append(menu.getAttribute()).append(SQLConstants.RIGHT_BRACKET).append(Communal.BLANK)
                        .append(menu.getAttributeRenaming());
                    break;
                case AggregationConstants.MIN:
                    b.append(AggregationConstants.MIN).append(SQLConstants.LEFT_BRACKET).append(menu.getAttribute()).append(SQLConstants.RIGHT_BRACKET).append(Communal.BLANK)
                        .append(menu.getAttributeRenaming());
                    break;
                case AggregationConstants.MAX:
                    b.append(AggregationConstants.MAX).append(SQLConstants.LEFT_BRACKET).append(menu.getAttribute()).append(SQLConstants.RIGHT_BRACKET).append(Communal.BLANK)
                        .append(menu.getAttributeRenaming());
                    break;
                case AggregationConstants.STANDARD_DEV:
                    b.append(AggregationConstants.STDDEVPOP).append(SQLConstants.LEFT_BRACKET).append(menu.getAttribute()).append(SQLConstants.RIGHT_BRACKET).append(Communal.BLANK)
                        .append(menu.getAttributeRenaming());
                    break;
                default:
                    throw new RuntimeException("wrong aggregation type!");
            }
            aggregationSet.add(b.toString());
        }
        // set ordering for both case
        setOrder(menu, menu.getAttributeRenaming());
    }

    /**
     * 设置排序集合，前提是菜单中的排序字段不为空，且不为none
     */
    private void setOrder(Menu menu, String attribute) {
        StringBuilder sb = new StringBuilder();
        sb.append(attribute).append(Communal.BLANK);
        if (! menu.hasSort()) {
            sb.append(SQLConstants.ASC);
        }
        else if (menu.getSort().equalsIgnoreCase(SQLConstants.ASC) || menu.getSort().equalsIgnoreCase(SQLConstants.DESC)) {
            sb.append(menu.getSort());
        }
        else {
            throw new RuntimeException("Wrong sort type!");
        }
        orderSet.add(sb.toString());
    }

    private void addGroup(String attr) {
        groupSet.add(attr);
    }


    /**
     * handle datetime binning series.
     * @param menu
     */
    private void handleDatetimeBinning(Menu menu) {
        DatetimeBinning binning = new DatetimeBinning(menu);
        attributeAndBinningSet.addAll(binning.binningAttributes());
//        System.out.println(binning.rename());
//        System.out.println(GROUP_BY_ARGS);
    }


    /**
     * A WITH subquery returning row number in total.
     * @return
     */
    private String totalRows() {
        return MessageFormat.format("WITH (select count(*) from {0}) AS {1}", datasource, ROWS);
    }
}
