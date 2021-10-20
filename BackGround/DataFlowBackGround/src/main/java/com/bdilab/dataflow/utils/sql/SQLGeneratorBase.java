package com.bdilab.dataflow.utils.sql;

import com.bdilab.dataflow.common.enums.ExceptionMsgEnum;
import com.bdilab.dataflow.common.exception.UncheckException;
import com.bdilab.dataflow.dto.jobdescription.JobDescription;

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
        if (jobDescription.getLimit() == null) {
            return " LIMIT 2000";
        }
        return " LIMIT " + jobDescription.getLimit();
    }

    public  String project(){
        return "";
    }


    public String filter(){
        return "";
    }

    public String group(){
        return "";
    }

    public abstract String generate();
}
