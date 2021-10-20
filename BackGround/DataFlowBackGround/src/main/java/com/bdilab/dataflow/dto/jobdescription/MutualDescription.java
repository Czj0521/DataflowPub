package com.bdilab.dataflow.dto.jobdescription;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author:zhb
 * @createTime:2021/9/26 15:05
 */

@Data
public class MutualDescription {
    private String dataSource;
    private String target;
    private List<String> features;
}
