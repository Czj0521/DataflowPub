package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.TransposeDescription;

import javax.validation.Valid;
import java.util.List;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-23
 * @description:
 **/
public class TransposeSQLGenerator extends SQLGeneratorBase{
    private TransposeDescription transposeDescription;
    private List<String> columnValues;
    public TransposeSQLGenerator(@Valid TransposeDescription transposeDescription, List<String> columnValues) {
        super(transposeDescription);
        this.transposeDescription = transposeDescription;
        this.columnValues = columnValues;
    }

    private String project(TransposeDescription transposeDescription) {
        String attributes = transposeDescription.getAttribute();
        String column = transposeDescription.getColumn();
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(transposeDescription.getGroupCol()+",");
        String groupFunc = transposeDescription.getGroupFunc() + "If";
        String template = transposeDescription.isColumnIsNumeric() ? groupFunc + "(" + attributes + ","+column+"=#$#) AS `#$#` " : groupFunc + "(" + attributes + ","+column+"='#$#')  AS `#$#` ";
        for (String columnValue : columnValues) {
            sb.append(template.replace("#$#", columnValue)  + " ,");
        }
        return sb.substring(0,sb.length()-1);
    }


    @Override
    public String group() {
        return " GROUP BY " + transposeDescription.getGroupCol();
    }


    @Override
    public String generate() {
        return project(transposeDescription) + super.datasource() + group() + super.limit();
    }
}