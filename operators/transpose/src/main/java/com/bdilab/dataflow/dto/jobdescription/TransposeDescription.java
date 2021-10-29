package com.bdilab.dataflow.dto.jobdescription;

import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-23
 * @description:
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransposeDescription extends JobDescription {
    @NotEmpty
    private String column;
    @NotNull
    private boolean columnIsNumeric;
    @NotNull
    private String[] groupBy;
    @NotNull
    private Map<String, String> attributeWithAggregationMap;
    private int topTransposedValuesNum;

    public TransposeDescription(String jobType, String dataSource, int limit, int topTransposedValuesNum) {
        super(jobType, dataSource, limit);
        this.topTransposedValuesNum = topTransposedValuesNum;
    }

    public TransposeDescription(String jobType, String dataSource, int limit) {
        super(jobType, dataSource, limit);
        topTransposedValuesNum = 20;
    }
}
