package com.bdilab.dataflow.dto.jobdescription;

import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: wh
 * @create: 2021-10-25
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterDescription extends JobDescription {
    String filter;
}
