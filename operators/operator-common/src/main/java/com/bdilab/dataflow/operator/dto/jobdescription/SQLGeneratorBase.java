package com.bdilab.dataflow.operator.dto.jobdescription;

import com.bdilab.dataflow.common.enums.ExceptionMsgEnum;
import com.bdilab.dataflow.common.exception.UncheckException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Zunjing Chen
 * @create: 2021-09-18
 * @description:
 **/
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
