package com.bdilab.dataflow.sql.generator;

import com.bdilab.dataflow.common.enums.ExceptionMsgEnum;
import com.bdilab.dataflow.common.exception.UncheckException;
import com.bdilab.dataflow.dto.jobdescription.JobDescription;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class SQLGeneratorBase {
    private JobDescription jobDescription;

    public String datasource() {
        // todo Check datasource
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


    public String filter(){
        return "";
    }

    public String group() {
        return "";
    }

    public abstract String generate();
}
