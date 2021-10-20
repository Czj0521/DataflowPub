package com.bdilab.dataflow.dto.profilerjson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author wjh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobDescription {
    private String input;
    private String column;
}
