package com.bdilab.dataflow.dto.jobdescription;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author : [zhangpeiliang]
 * @description : [菜单对象]
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Menu {
    /**
     * 菜单名称，例如xAxis表示菜单为x轴
     */
    @ApiModelProperty(value = "菜单名称", required = true, example = "x-axis")
    private String menu;

    /**
     * 当前菜单选择的属性值
     */
    @ApiModelProperty(value = "属性值", required = true, example = "Model")
    private String attribute;

    /**
     * 为属性选择的数据分箱
     */
    @ApiModelProperty(value = "分箱策略", required = true, example = "AlphabeticBinning")
    private String binning;

    /**
     * 为属性选择的聚合函数
     */
    @ApiModelProperty(value = "聚合函数", required = true, example = "none")
    private String aggregation;

    /**
     * 为属性选择的排序方式
     */
    @ApiModelProperty(value = "排序方式", required = true, example = "None")
    private String sort;
}
