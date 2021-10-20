package com.bdilab.dataflow.flink.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static com.bdilab.dataflow.common.consts.WebConstants.FLINK_HTTP_CHARSETS;

/**
 * @author wh
 * @version 1.0
 * @date 2021/10/15
 *
 */
@Data
public class FlinkJobRunParametersMutual extends FlinkJobRunParameters {
    private String datasource;
    private String column1;
    private String type1;
    private String column2;
    private String type2;
    private String jobId;

    public FlinkJobRunParametersMutual() {
    }

    public FlinkJobRunParametersMutual(String entryClass, String datasource, String column1, String type1, String column2, String type2, String jobId) {
        super(entryClass);
        this.datasource = datasource;
        this.column1 = column1;
        this.type1 = type1;
        this.column2 = column2;
        this.type2 = type2;
        this.jobId = jobId;
    }

    @Override
    public String toUrlParameter() {
        if(datasource!=null && column1!=null && type1!=null && column2!=null && type2!=null && jobId!=null){
            StringBuilder sb = new StringBuilder();
            try {
                sb.append(super.toUrlParameter())
                        .append("&datasource=").append(datasource)
                        .append("&column1=").append(URLEncoder.encode(column1, FLINK_HTTP_CHARSETS))
                        .append("&type1=").append(type1)
                        .append("&column2=").append(URLEncoder.encode(column2, FLINK_HTTP_CHARSETS))
                        .append("&type2=").append(type2)
                        .append("&jobId=").append(jobId);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return new String(sb);
        } else {
            throw new RuntimeException("Parameters can not be null!");
        }

    }
}
