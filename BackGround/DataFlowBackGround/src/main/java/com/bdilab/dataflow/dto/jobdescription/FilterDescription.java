package com.bdilab.dataflow.dto.jobdescription;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.consts.JobTypeConstants;
import lombok.Data;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-24
 * @description:
 **/
@Data
public class FilterDescription  extends  JobDescription{
    String filter;

    public FilterDescription(String jobType, String dataSource, Integer limit, String filter) {
        super(jobType, dataSource, limit);
        this.filter = filter;
    }

    public static FilterDescription generateFromJson(JSONObject json){
        String filter = json.getString("filter");
        Integer limit =  json.getInteger("limit");
        return  new FilterDescription(JobTypeConstants.FILTER_JOB,"",limit,filter);
    }
}
