package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.TransformationDesc;
import com.bdilab.dataflow.operator.dto.jobdescription.SqlGeneratorBase;
import com.bdilab.dataflow.operator.link.LinkSqlGenerator;
import java.util.List;

/**
 * Transformation operator sql logic.
 *
 * @author Zunjing Chen
 * @date 2021-12-09
 **/
public class TransformationSqlGenerator extends SqlGeneratorBase implements LinkSqlGenerator {

  private final TransformationDesc transformationDesc;

  public TransformationSqlGenerator(TransformationDesc transformationDesc) {
    super(transformationDesc);
    this.transformationDesc = transformationDesc;
  }

  @Override
  public String generate() {
    return generateDataSourceSql() + limit();
  }

  @Override
  public String generateDataSourceSql() {
    return "SELECT * ," + transformationSql() + super.datasource(0);
  }

  private String transformationSql() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder
        .append(transformation(transformationDesc.getExpressions()))
        .append(transformation(transformationDesc.getFindReplaces()))
        .append(transformation(transformationDesc.getBinarizers()))
        .append(transformation(transformationDesc.getDataTypes()))
        .append(transformation(transformationDesc.getCustomBinnings()))
        .deleteCharAt(stringBuilder.length() - 1); //delete ,
    return stringBuilder.toString();
  }

  private String transformation(List<?> transformations) {
    if(transformations == null){
      return "";
    }
    StringBuilder stringBuilder = new StringBuilder();
    for (Object transformation : transformations) {
      stringBuilder.append(transformation).append(",");
    }
    return stringBuilder.toString();
  }
}
