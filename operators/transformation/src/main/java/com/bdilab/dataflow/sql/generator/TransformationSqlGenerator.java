package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.TransformationDescription;
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

  private final TransformationDescription transformationDescription;

  public TransformationSqlGenerator(TransformationDescription transformationDescription) {
    super(transformationDescription);
    this.transformationDescription = transformationDescription;
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
        .append(transformation(transformationDescription.getExpressions()))
        .append(transformation(transformationDescription.getFindReplaces()))
        .append(transformation(transformationDescription.getBinarizers()))
        .append(transformation(transformationDescription.getDataTypes()))
        .append(transformation(transformationDescription.getCustomBinnings()))
        .deleteCharAt(stringBuilder.length() - 1); //delete ,
    return stringBuilder.toString();
  }

  private String transformation(List<?> transformations) {
    if (transformations == null) {
      return "";
    }
    StringBuilder stringBuilder = new StringBuilder();
    for (Object transformation : transformations) {
      if (!transformation.toString().equals("")) {
        stringBuilder.append(transformation).append(",");
      }
    }
    return stringBuilder.toString();
  }
}
