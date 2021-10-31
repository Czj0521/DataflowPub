package com.bdilab.dataflow.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static com.bdilab.dataflow.common.consts.CommonConstants.DATETIME_FORMAT;

/**
 * @author wh
 * @version 1.0
 * @date 2021/09/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("datasource_statistic")
public class DataSourceStatistic {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("datasource")
    private String datasource;

    @TableField("table_count")
    private Long  tableCount;

    @TableField("column_name_type")
    private String columnNameType;

    @TableField("column_min")
    private String columnMin;

    @TableField("column_max")
    private String columnMax;

    @JsonFormat(pattern = DATETIME_FORMAT,timezone="GMT+8")
    @TableField("create_time")
    private Date createTime;

    @JsonFormat(pattern = DATETIME_FORMAT,timezone="GMT+8")
    @TableField("update_time")
    private Date updateTime;
}
