package com.bdilab.dataflow.dto.jobdescription;

import com.bdilab.dataflow.dto.Binarizer;
import com.bdilab.dataflow.dto.CustomBinning;
import com.bdilab.dataflow.dto.DataType;
import com.bdilab.dataflow.dto.Expression;
import com.bdilab.dataflow.dto.FindReplace;
import com.bdilab.dataflow.operator.dto.jobdescription.JobDescription;
import java.util.List;
import lombok.Data;

/**
 * @author Zunjing Chen
 * @date 2021-12-09
 **/
@Data
public class TransformationDesc extends JobDescription {

  private List<Expression> expressions;
  private List<FindReplace> findReplaces;
  private List<Binarizer> binarizers;
  private List<DataType> dataTypes;
  private List<CustomBinning> customBinnings;


}
