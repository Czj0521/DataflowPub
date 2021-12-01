package com.bdilab.dataflow.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.consts.CommonConstants;
import com.bdilab.dataflow.dto.MaterializeDescription;
import com.bdilab.dataflow.service.MaterializeJobService;

import javax.annotation.Resource;

import com.bdilab.dataflow.utils.clickhouse.ClickHouseManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * Materialize Job Service Impl.

 * @author: wh
 * @create: 2021-10-28
 */
@Service
@Slf4j
public class MaterializeJobServiceImpl implements MaterializeJobService {
  @Resource
  ClickHouseManager clickHouseManager;


  public JSONObject materialize(JSONObject materializeDescriptionJson) {
    MaterializeDescription materializeDescription = materializeDescriptionJson.toJavaObject(MaterializeDescription.class);
    String jobType = materializeDescription.getJobType();
    JSONObject outputs = new JSONObject();
    switch (jobType) {
      case "materialize":
        outputs = materialize(materializeDescription.getOperatorId());
        break;
      case "deleteMaterialize":
        outputs = deleteMaterialize(materializeDescription.getOperatorId());
        break;
      default:
    }
    return outputs;
  }


  @Override
  public JSONObject materialize(String operatorId) {
    String tempName = CommonConstants.CPL_TEMP_TABLE_PREFIX + operatorId;
    String materializeName = CommonConstants.CPL_MATERIALIZE_PREFIX + operatorId;
    clickHouseManager.createMaterialize(tempName, materializeName);
    log.info("Materialize job success.");

    JSONObject outputs = new JSONObject();
    outputs.put("metadata", clickHouseManager.getMetadata(materializeName));
    outputs.put("materializeId", materializeName);
    return outputs;
  }

  @Override
  public JSONObject deleteMaterialize(String materializeId) {
    JSONObject outputs = new JSONObject();
    outputs.put("jobType", "deleteMaterialize");
    try {
      clickHouseManager.deleteTable(materializeId);
      outputs.put("statue", "success");
    } catch (Exception e) {
      outputs.put("statue", "fail");
      e.printStackTrace();
    }
    return outputs;
  }
}
