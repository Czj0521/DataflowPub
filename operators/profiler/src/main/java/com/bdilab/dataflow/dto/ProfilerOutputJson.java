package com.bdilab.dataflow.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

/**
 * Profiler output

 * @author: Yushaochao
 * @create: 2021-11-11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfilerOutputJson {
  private List<Map<String, Object>> outputs;
}
