package com.bdilab.dataflow.utils.clickhouse;

import com.bdilab.dataflow.common.enums.ExceptionMsgEnum;
import com.bdilab.dataflow.common.exception.UncheckException;
import java.io.IOException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * ClickHouse Http Utils.

 * @author: Zunjing Chen
 * @create: 2021-09-17
 * @description: clickhouse http 接口
 **/
public class ClickHouseHttpUtils {
  /**
   * rest get.

   * @param url url
   * @return response
   */
  public static String sendGet(String url) {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpGet httpGet = new HttpGet(url);
    try {
      CloseableHttpResponse response = httpclient.execute(httpGet);
      if (response.getStatusLine().getStatusCode() == 200) {
        return EntityUtils.toString(response.getEntity(), "UTF-8");
      }
      System.out.println(EntityUtils.toString(response.getEntity(), "UTF-8"));
      throw new UncheckException(
          ExceptionMsgEnum.CLICKHOUSE_HTTP_ERROR.getMsg() + "\n" + response.toString());
    } catch (IOException e) {
      throw new UncheckException(e.getMessage());
    }
  }

  /**
   * rest post.

   * @param url url
   * @return response
   */
  public static String sendPost(String url) {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost(url);
    try {
      CloseableHttpResponse response = httpclient.execute(httpPost);
      if (response.getStatusLine().getStatusCode() == 200) {
        return EntityUtils.toString(response.getEntity(), "UTF-8");
      }
      throw new UncheckException(
          ExceptionMsgEnum.CLICKHOUSE_HTTP_ERROR.getMsg() + "\n" + response.toString());
    } catch (IOException e) {
      throw new UncheckException(e.getMessage());
    }
  }
}
