package com.bdilab.dataflow.operator.dto.jobdescription;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: czj
 * @create: 2021-10-27
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class JobDescription {
    String jobType;
    String dataSource;
    Integer limit;
}
