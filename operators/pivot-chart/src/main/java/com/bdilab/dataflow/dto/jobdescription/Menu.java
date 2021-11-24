package com.bdilab.dataflow.dto.jobdescription;

import com.bdilab.dataflow.constants.AggregationConstants;
import com.bdilab.dataflow.constants.BinningConstants;
import com.bdilab.dataflow.constants.Communal;
import com.bdilab.dataflow.constants.SQLConstants;
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

    public String getAttributeRenaming() {
        StringBuilder b = new StringBuilder();
        b.append(attribute);
        if (hasAggregation() || hasBinning()) {
            b.append(Communal.UNDER_CROSS);
        }
        else {
            return b.toString();
        }

        switch (aggregation) {
            case AggregationConstants.AVERAGE:
                b.append(AggregationConstants.AVG);
                break;
            case AggregationConstants.COUNT:
                b.append(AggregationConstants.COUNT);
                break;
            case AggregationConstants.DISTINCT_COUNT:
                b.append(AggregationConstants.DISTINCT).append(Communal.UNDER_CROSS).append(AggregationConstants.COUNT);
                break;
            case AggregationConstants.SUM:
                b.append(AggregationConstants.SUM);
                break;
            case AggregationConstants.MIN:
                b.append(AggregationConstants.MIN);
                break;
            case AggregationConstants.MAX:
                b.append(AggregationConstants.MAX);
                break;
            case AggregationConstants.STANDARD_DEV:
                b.append(AggregationConstants.STD);
                break;
            default:
                break;
        }
        
        if (binning != null && !binning.equalsIgnoreCase(Communal.NONE)) {
            b.append(BinningConstants.BIN);
        }
        
        return b.toString();
    }
    
    public Boolean hasBinning() {
        return binning != null && !binning.isEmpty() && !binning.equalsIgnoreCase(Communal.NONE);
    }
    
    public Boolean hasAggregation() {
        return aggregation != null && !aggregation.isEmpty() && !aggregation.equalsIgnoreCase(Communal.NONE);
    }

    public Boolean hasSort() {
        return sort != null && !sort.isEmpty() && !sort.equalsIgnoreCase(Communal.NONE);
    }
}
