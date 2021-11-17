package com.bdilab.dataflow.dto;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Profiler output.

 * @author: Yushaochao
 * @create: 2021-11-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfilerOutputJson {
  private List<Map<String, Object>> outputs;
}
