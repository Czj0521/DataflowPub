package com.bdilab.dataflow.dto;

import java.util.List;
import lombok.Data;

/**
 * Independent variable base class.
 *
 * @author Zunjing Chen
 * @date 2021-12-19
 **/
@Data
public abstract class IndependentVariableBase {

  protected String columnName;
  protected String defaultValue;
  protected String type;
  protected String expression;
  protected List<String> possibleValues;

  public abstract List<String> possibleValues();
}
