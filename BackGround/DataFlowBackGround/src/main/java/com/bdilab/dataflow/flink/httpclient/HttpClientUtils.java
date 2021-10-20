package com.bdilab.dataflow.flink.httpclient;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static com.bdilab.dataflow.common.consts.WebConstants.FLINK_HTTP_CHARSETS;

/**
 * @author wh
 * @version 1.0
 * @date 2021/10/17
 *
 */
@Slf4j
@Component
public class HttpClientUtils {
    @Resource
    private CloseableHttpClient httpClient;
    @Resource
    private RequestConfig requestConfig;

    /**
     * get请求
     * @param url 地址
     * @return response结果
     */
    public String doGet(String url) {
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            response = httpClient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return new String(EntityUtils.toString(response.getEntity()).getBytes(FLINK_HTTP_CHARSETS), FLINK_HTTP_CHARSETS);
            } else {
                log.error(new String(EntityUtils.toString(response.getEntity()).getBytes(FLINK_HTTP_CHARSETS), FLINK_HTTP_CHARSETS));
            }
        } catch (IOException e) {
            log.error("http调用出错:{}",e.getMessage());
        } finally {
            try {
                if (response != null){
                    response.close();
                }
            }catch (IOException e){
                log.error("关闭链接出错:{}",e.getMessage());
            }
        }
        return null;
    }

    /**
     * post请求
     * @param url 地址
     * @param httpEntity 请求体
     * @return response结果
     */
    public String doPost(String url, HttpEntity httpEntity) {
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(httpEntity);
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return new String(EntityUtils.toString(response.getEntity()).getBytes(FLINK_HTTP_CHARSETS), FLINK_HTTP_CHARSETS);
            } else {
                log.error(new String(EntityUtils.toString(response.getEntity()).getBytes(FLINK_HTTP_CHARSETS), FLINK_HTTP_CHARSETS));
            }
        } catch (IOException e) {
            log.error("http调用出错:{}",e.getMessage());
        } finally {
            try {
                if (response != null){
                    response.close();
                }
            }catch (IOException e){
                log.error("关闭链接出错:{}",e.getMessage());
            }
        }
        return null;
    }
}
