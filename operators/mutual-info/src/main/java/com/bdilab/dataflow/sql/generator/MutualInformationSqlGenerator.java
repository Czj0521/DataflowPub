package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.dto.jobdescription.MutualInformationDescription;
import com.bdilab.dataflow.operator.dto.jobdescription.SqlGeneratorBase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.text.TextStringBuilder;

import java.text.MessageFormat;

/**
 * @author: Guo Yongiqang
 * @date: 2021/12/19 21:34
 * @version:
 */
@Slf4j
public class MutualInformationSqlGenerator extends SqlGeneratorBase {
  private final MutualInformationDescription mutualInformationDescription;

  public MutualInformationSqlGenerator(MutualInformationDescription description) {
    mutualInformationDescription = description;
    String dataSource = description.getDataSource()[0];

    // give it a table name if datasource is a statement
    if (dataSource.lastIndexOf(")") >= 0 &&
        !dataSource.substring(dataSource.lastIndexOf(")")).contains("AS")) {
      dataSource += " AS MUTUAL_INFORMATION_" + DigestUtils.sha1Hex(dataSource);
      mutualInformationDescription.setDataSource(new String[] {dataSource});
    }
  }

  @Override
  public String generate() {
    log.debug("Generated SQL by MutualInformationSqlGenerator: {}",
        select() + "FROM " + mutualInformationDescription.getDataSource()[0]);
    return select() + "FROM " + mutualInformationDescription.getDataSource()[0];
  }

  private String mutalInformationDiscrete(String target, String feature) {
    String dataSource = mutualInformationDescription.getDataSource()[0];
    if (dataSource.lastIndexOf(")") >= 0 &&
        dataSource.substring(dataSource.lastIndexOf(")")).contains("AS")) {
      dataSource = dataSource.substring(dataSource.lastIndexOf("AS") + 3);
    }
    log.debug("DataSource: {}", dataSource);
    String targetFqn = dataSource + "." + target;
    String featureFqn = dataSource + "." + feature;
    String mi = "entropy({0}) + entropy({1}) - entropy(tuple({0}, {1}))";
    String hx = "entropy({0})";
    return MessageFormat.format(
        "tuple({0}, {1}) AS {2}",
        MessageFormat.format(mi, targetFqn, featureFqn),
        MessageFormat.format(hx, targetFqn),
        feature
    );
  }

  private String select() {
    TextStringBuilder b = new TextStringBuilder();
    String target = mutualInformationDescription.getTarget();
    b.append("SELECT").appendSeparator(" ").append(mutalInformationDiscrete(target, target));

    for (String feature: mutualInformationDescription.getFeatures()) {
      b.appendSeparator(", ").append(mutalInformationDiscrete(target, feature));
    }

    return b.appendSeparator(" ").toString();
  }
}
