package com.bdilab.dataflow.service.Impl;


import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.common.enums.ExceptionMsgEnum;
import com.bdilab.dataflow.common.exception.UncheckException;
import com.bdilab.dataflow.dto.PythonDescription;
import com.bdilab.dataflow.dto.PythonRequest;
import com.bdilab.dataflow.service.PythonService;
import com.bdilab.dataflow.utils.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.utils.dag.DagNode;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.python.core.PyCode;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Service
public class PythonServiceImpl implements PythonService {
  @Autowired
  ClickHouseJdbcUtils clickHouseJdbcUtils;
  @Value("${python.url}")
  private String pythonServerUrl;


  @Override
  public List<Map<String, Object>> saveToClickHouse(DagNode dagNode) {
    //设置http请求体
    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost(pythonServerUrl);

    PythonRequest pythonRequest = new PythonRequest();
    JSONObject nodeDescription = (JSONObject) dagNode.getNodeDescription();
    PythonDescription pythonDescription = JSONObject.toJavaObject(nodeDescription, PythonDescription.class);
    pythonRequest.setPythonDescription(pythonDescription);
    pythonRequest.setOperatorId(dagNode.getNodeId());

    httpPost.setEntity(new StringEntity(JSONObject.toJSONString(pythonRequest), ContentType.create("application/json", "utf-8")));
    httpPost.setHeader("Content-Type", "application/json");

    //发请求
    try {
      CloseableHttpResponse response = httpclient.execute(httpPost);
      if (response.getStatusLine().getStatusCode() == 200) {
        JSONObject jsonObject = JSONObject.parseObject(EntityUtils.toString(response.getEntity(), "UTF-8"));
        if(!"JOB_FINISH".equals(jsonObject.getString("jobStatus"))){
          throw new UncheckException("python error");
        }
        return null;
      }
      throw new UncheckException(
          ExceptionMsgEnum.CLICKHOUSE_HTTP_ERROR.getMsg() + "\n" + response.toString());
    } catch (IOException e) {
      throw new UncheckException(e.getMessage());
    }
  }

}
