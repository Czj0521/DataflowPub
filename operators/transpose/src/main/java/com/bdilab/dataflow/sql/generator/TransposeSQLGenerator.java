package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.TransposeDescription;
import com.bdilab.dataflow.operator.link.LinkSqlGenerator;
import com.bdilab.dataflow.utils.SQLParseUtils;
import org.apache.commons.text.StringSubstitutor;
import com.bdilab.dataflow.operator.dto.jobdescription.SQLGeneratorBase;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-23
 * @description:
 **/
public class TransposeSQLGenerator extends SQLGeneratorBase implements LinkSqlGenerator {
    private TransposeDescription transposeDescription;
    private List<String> columnValues;
    private static final String GROUP_BY = " GROUP BY ";
    private static final String GROUP_FUNCTION = "groupFunction";
    private static final String ATTRIBUTE = "attribute";
    private static final String COLUMN = "column";
    private static final String COLUMN_NAME = "columnName";
    private static final String COLUMN_VALUE = "columnValue";
    private static final String STRING_TEMPLATE = "${groupFunction}(${attribute},${column} = '${columnValue}' )  AS `${columnName}` ";
    private static final String NUMERIC_TEMPLATE = "${groupFunction}(${attribute},${column}  = ${columnValue} )  AS `${columnName}` ";

    public TransposeSQLGenerator(@Valid TransposeDescription transposeDescription, List<String> columnValues) {
        super(transposeDescription);
        this.transposeDescription = transposeDescription;
        this.columnValues = columnValues;
    }

    /**
     * generate sql like select ** ,** means columns
     * @return sql
     */
    @Override
    public String project() {
        StringBuilder stringBuilder = new StringBuilder("SELECT ");
        appendGroupColumns(stringBuilder);
        appendColumns(stringBuilder);
        return stringBuilder.toString();
    }


    private void appendColumns(StringBuilder stringBuilder) {
        String template = transposeDescription.isColumnIsNumeric() ? NUMERIC_TEMPLATE : STRING_TEMPLATE;
        for (String columnValue : columnValues) {
            for (Map.Entry<String, String> entry : transposeDescription.getAttributeWithAggregationMap().entrySet()) {
                Map<String,String> valueMap = new HashMap<>();
                valueMap.put(GROUP_FUNCTION,entry.getValue() + "If");
                valueMap.put(ATTRIBUTE,entry.getKey());
                valueMap.put(COLUMN,transposeDescription.getColumn());
                // columnName = "100KG_avg_age" 180KG means weight
                valueMap.put(COLUMN_NAME,columnValue +"_"+entry.getValue()+"_"+entry.getKey());
                valueMap.put(COLUMN_VALUE,columnValue);
                StringSubstitutor sub = new StringSubstitutor(valueMap);
                stringBuilder.append(sub.replace(template)).append(",");
            }
        }
        // delete ,
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
    }

    /**
     * append group column names
     */
    private void appendGroupColumns(StringBuilder stringBuilder) {
        stringBuilder.append(SQLParseUtils.combineWithSeparator(transposeDescription.getGroupBy(), ",")).append(",");
    }


    @Override
    public String group() {
        StringBuilder stringBuilder = new StringBuilder(GROUP_BY);
        return stringBuilder.append(SQLParseUtils.combineWithSeparator(transposeDescription.getGroupBy(),",")).toString();

    }


    @Override
    public String generate() {
        return project() + super.datasource() + group() + super.limit();
    }

    @Override
    public String generateDataSourceSql() {
        return project() + super.datasource() + group();
    }
}
