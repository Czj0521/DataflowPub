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
 * @date 2021/09/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("file")
public class File {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("file_uuid")
    private Long  fileUuid;

    @TableField("file_name")
    private String fileName;

    @TableField("file_dir")
    private String fileDir;

    @TableField("file_type")
    private String fileType;

    @TableField("table_id")
    private String tableId;

    @JsonFormat(pattern = DATETIME_FORMAT,timezone="GMT+8")
    @TableField("create_time")
    private Date createTime;
}
