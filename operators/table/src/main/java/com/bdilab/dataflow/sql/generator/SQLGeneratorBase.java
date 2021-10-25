package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.common.enums.ExceptionMsgEnum;
import com.bdilab.dataflow.common.exception.UncheckException;
import com.bdilab.dataflow.dto.jobdescription.JobDescription;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-23
 * @description: transform the job description to sql (as a datasource or a compute task)
 **/
public abstract class SQLGeneratorBase {
    private JobDescription jobDescription;

    public SQLGeneratorBase(JobDescription jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String datasource() {
        // todo 数据源检查
        if (jobDescription.getDataSource() == null) {
            throw new UncheckException(ExceptionMsgEnum.TABLE_SQL_PARSE_ERROR.getMsg());
        }
        return " FROM " + jobDescription.getDataSource();
    }

    public String limit() {
        return jobDescription.getLimit() == null ? " LIMIT 2000" : " LIMIT " + jobDescription.getLimit();
    }

    public String project() {
        return "";
    }


    public String filter() {
        return "";
    }

    public String group() {
        return "";
    }

    public abstract String generate();
}
