package com.bdilab.dataflow.dto.jobdescription;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.consts.JobTypeConstants;
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
