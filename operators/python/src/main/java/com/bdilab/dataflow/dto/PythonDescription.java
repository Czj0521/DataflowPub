package com.bdilab.dataflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Repository
public class PythonDescription {
  private String jobType;
  private String[] dataSource;
  private String codeStr;
}
