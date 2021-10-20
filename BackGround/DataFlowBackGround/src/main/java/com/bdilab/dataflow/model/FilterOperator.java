package com.bdilab.dataflow.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wh
 * @version 1.0
 * @date 2021/09/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("filter_operator")
public class FilterOperator {
    @TableId("data_type")
    private String dataType;

    @TableField("column_type")
    private String columnType;

    @TableField("filter_operator")
    private String filterOperator;
}
