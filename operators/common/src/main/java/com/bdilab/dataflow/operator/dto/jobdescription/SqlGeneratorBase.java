package com.bdilab.dataflow.operator.dto.jobdescription;

import com.bdilab.dataflow.common.enums.ExceptionMsgEnum;
import com.bdilab.dataflow.common.exception.UncheckException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * SQLGeneratorBase.

 * @author: Zunjing Chen
 * @create: 2021-09-18
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class SqlGeneratorBase {
  private JobDescription jobDescription;

  /**
   * get datasource sql.
   */
  public String datasource(int slotIndex) {
    // todo Check datasource
    if (jobDescription.getDataSource() == null) {
      throw new UncheckException(ExceptionMsgEnum.TABLE_SQL_PARSE_ERROR.getMsg());
    }
    return " FROM " + jobDescription.getDataSource()[slotIndex];
  }

  public String limit() {
    return jobDescription.getLimit() == null
        ? " LIMIT 2000" : " LIMIT " + jobDescription.getLimit();
  }

  public String project() {
    return "";
  }


  public String filter() {
    return "";
  }

  public String group() {
    return "";
  }

  public abstract String generate();
}
