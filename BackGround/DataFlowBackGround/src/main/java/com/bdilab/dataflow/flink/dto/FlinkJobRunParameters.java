package com.bdilab.dataflow.flink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wh
 * @version 1.0
 * @date 2021/10/17
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlinkJobRunParameters {
    private String entryClass;
    public String toUrlParameter(){
        StringBuilder sb = new StringBuilder();
        if(entryClass!=null){
            sb.append("?entry-class=").append(entryClass);
            return new String(sb);
        } else {
            throw new RuntimeException("Parameters can not be null!");
        }
    }
}
