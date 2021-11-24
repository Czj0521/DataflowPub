package com.bdilab.dataflow.utils;

import com.bdilab.dataflow.dto.jobdescription.Menu;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import static com.bdilab.dataflow.constants.BinningConstants.*;
import static com.bdilab.dataflow.sql.generator.PivotChartSqlGenerator.ROWS;

/**
 * @author: Guo Yongqiang
 * @date: 2021/11/22 19:45
 * @version:
 */
public class DatetimeBinning {
  private String column = null;
  private String binning = null;

  public DatetimeBinning(Menu menu) {
    if (menu == null || menu.getAttribute() == null || menu.getBinning() == null) {
      throw new RuntimeException(MessageFormat.format("Invalid Pivot Chart menu: {0}", menu));
    }


    // evaluate binning.
    switch (menu.getBinning()) {
      case SECOND:
      case MINUTE:
      case HOUR:
      case DAY:
      case MONTH:
      case YEAR:
        binning = menu.getBinning();
        break;
      default:
        throw new RuntimeException(MessageFormat.format("Unsupported DatetimeBinning: {0}.", binning));
    }
    column = menu.getAttribute();
  }



  public List<String> binningAttributes() {
    String left = MessageFormat.format("{0} AS {1}", left(), rename());
    String right = MessageFormat.format("{0} AS {1}", right(), rename() + "_RIGHT");
//    String count = MessageFormat.format("count(*) AS {0}", column + "_COUNT");
//    String percent = MessageFormat.format("count(*) / {0} * 100 AS {1}", ROWS, column + "_PERCENTAGE");

//    return Arrays.asList(left, right, count, percent);
    return Arrays.asList(left, right);
  }

  public String rename() {
    return column + "_" + BIN;
  }

  private String left() {
    return MessageFormat.format("date_trunc(''{0}'', {1})", binning, column);
  }

  private String right() {
    return MessageFormat.format("date_add({0}, 1, {1})", binning, left());
  }

}
