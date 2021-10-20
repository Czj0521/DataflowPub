package com.bdilab.dataflow.flink.uitls;

import com.alibaba.fastjson.JSONObject;
import com.bdilab.dataflow.flink.dto.FlinkJobRunParameters;
import com.bdilab.dataflow.flink.httpclient.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author wh
 * @version 1.0
 * @date 2021/10/15
 *
 */
@Component
@Slf4j
public class FlinkRestUtils {
    @Resource
    FlinkRestConfigUtils flinkRestConfigUtils;
    @Resource
    HttpClientUtils httpClientUtils;
    //todo
//    /**
//     * 获取所有job信息
//     * @throws Exception
//     */
//    public static void getAllJobMessage() throws Exception {
//        HttpClient httpClient = HttpClients.createDefault();
//        String url = FlinkWebUrlUtil.getProxyFlinkUrl("flink-stream");
//        HttpGet httpGet = new HttpGet(url + "jobs/overview");
//        System.out.println(url + "jobs/overview");
//        HttpResponse execute = httpClient.execute(httpGet);
//        HttpEntity entity = execute.getEntity();
//        System.out.println(entity);
//        String result = new BufferedReader(new InputStreamReader(entity.getContent()))
//                .lines().collect(Collectors.joining("\n"));
//        System.out.println(result);
//    }
//
//    //获取单个job信息
//    public void getJobMessage() throws Exception {
//        HttpClient httpClient = HttpClients.createDefault();
//        String url = FlinkWebUrlUtil.getProxyFlinkUrl("flink-stream ");
//        HttpGet httpGet = new HttpGet(url + "jobs/0f859a41e25060975719ca7ca0cfb1a9");
//        System.out.println(url + "jobs/0f859a41e25060975719ca7ca0cfb1a9");
//        HttpResponse execute = httpClient.execute(httpGet);
//        HttpEntity entity = execute.getEntity();
//        System.out.println(entity);
//        String result = new BufferedReader(new InputStreamReader(entity.getContent()))
//                .lines().collect(Collectors.joining("\n"));
//        System.out.println(result);
//    }
//
//    //取消单个job
//    public void cancelJob() throws Exception {
//        HttpClient httpClient = HttpClients.createDefault();
//        String url = FlinkWebUrlUtil.getProxyFlinkUrl("flink-stream ");
//        HttpGet httpGet = new HttpGet(url + "jobs/0f859a41e25060975719ca7ca0cfb1a9/yarn-cancel");
//        System.out.println(url + "jobs/0f859a41e25060975719ca7ca0cfb1a9/yarn-cancel");
//        HttpResponse execute = httpClient.execute(httpGet);
//        HttpEntity entity = execute.getEntity();
//        System.out.println(entity);
//        String result = new BufferedReader(new InputStreamReader(entity.getContent()))
//                .lines().collect(Collectors.joining("\n"));
//        System.out.println(result);
//    }

    /**
     * 获取所有已经上传的jar包信息
     * @return jar包信息
     */
    public String getJarsMessage() {
        String url = flinkRestConfigUtils.getFlinkHttpAddress() + "/jars";
        log.info("FlinkRest: " + url);
        return httpClientUtils.doGet(url);
    }

    /**
     * 上传flink jar
     * @param jarName job对应jar包名称
     * @return filename
     */
    public String flinkJarUpload(String jarName) {
        String url = flinkRestConfigUtils.getFlinkHttpAddress() + "/jars/upload";
        String jarPath = flinkRestConfigUtils.getFlinkJobJarPath() + jarName;
        log.info("FlinkRest: " + url);
        log.info("FlinkRest: " + jarPath);
        HttpPost uploadFile = new HttpPost(url);
        HttpEntity httpEntity = null;
        try {
            httpEntity = MultipartEntityBuilder.create().addBinaryBody(
                    "jarfile",
                    new FileInputStream(jarPath),
                    ContentType.create("application/x-java-archive"),
                    flinkRestConfigUtils.getFlinkJobJarName()
            ).build();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String result = httpClientUtils.doPost(url, httpEntity);
        log.info("FlinkRest: response= " + result);
        JSONObject resultJson = JSONObject.parseObject(result);
        if("success".equals(resultJson.getString("status"))){
            return resultJson.getString("filename");
        }
        return null;
    }

    /**
     * @param jobId jarid
     * @param parameters jar 运行传入参数
     * 运行flink jar
     */
    public void flinkJarRun(String jobId, FlinkJobRunParameters parameters) {
        String url = new String(
                (new StringBuilder()).append(flinkRestConfigUtils.getFlinkHttpAddress())
                        .append("/jars/")
                        .append(jobId)
                        .append("/run")
                        .append(parameters.toUrlParameter()));
        log.info(url);

        String result = httpClientUtils.doPost(url, null);
        System.out.println(result);
    }
}
