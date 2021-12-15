package com.bdilab.dataflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PythonRequest {
  private PythonDescription pythonDescription;
  private String operatorId;
}
