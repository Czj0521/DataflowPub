package com.bdilab.dataflow.flink.job;

import com.alibaba.fastjson.JSON;
import com.bdilab.dataflow.flink.utils.Utils;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.io.jdbc.JDBCInputFormat;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.core.fs.FileSystem;
import org.apache.flink.types.Row;
import redis.clients.jedis.Jedis;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;

/**
 * @author wh
 * @version 1.0
 * @date 2021/10/14
 */
public class Mutual {
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static void main(String[] args) throws Exception {
        String driverName = "ru.yandex.clickhouse.ClickHouseDriver";
        String url = "jdbc:clickhouse://47.104.202.153:8123/dataflow";
        StringBuilder query1 = new StringBuilder();
        StringBuilder query2 = new StringBuilder();
        ParameterTool parameterTool = ParameterTool.fromArgs(args);
        String datasource = parameterTool.has("datasource") ? parameterTool.get("datasource") : "airuuid";
        String column1 = parameterTool.has("column1") ? parameterTool.get("column1") : "city";
        String type1 = parameterTool.has("type1") ? parameterTool.get("type1") : "String";
        String column2 = parameterTool.has("column2") ? parameterTool.get("column2") : "AQI";
        String type2 = parameterTool.has("type2") ? parameterTool.get("type2") : "Integer";
        String jobId = parameterTool.has("jobId") ? parameterTool.get("jobId") : "jobid1";
        query1.append("select ").append(column1).append(" from ").append(datasource).append(" limit 100");
        query2.append("select ").append(column2).append(" from ").append(datasource).append(" limit 100");
        String queryStr1 = new String(query1);
        String queryStr2 = new String(query2);
        RowTypeInfo rowTypeInfo1 = Utils.getRowTypeInfo(type1);
        RowTypeInfo rowTypeInfo2 = Utils.getRowTypeInfo(type2);

        System.out.println(column2);
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
//        ExecutionEnvironment env =
//                ExecutionEnvironment.createRemoteEnvironment("192.168.0.53",
//                        8081,
//                        "D:\\JetBrains\\local\\DataFlow\\FlinkJob\\target\\FlinkJob-1.0-SNAPSHOT-jar-with-dependencies.jar");
        env.getConfig().setGlobalJobParameters(parameterTool);
        DataSource<Row> input1 = env.createInput(
                JDBCInputFormat.buildJDBCInputFormat()
                        .setDrivername(driverName)
                        .setDBUrl(url)
                        .setQuery(queryStr1)
                        .setRowTypeInfo(rowTypeInfo1)
                        .finish()).setParallelism(1);
        DataSet<Row> input2 = env.createInput(
                JDBCInputFormat.buildJDBCInputFormat()
                        .setDrivername(driverName)
                        .setDBUrl(url)
                        .setQuery(queryStr2)
                        .setRowTypeInfo(rowTypeInfo2)
                        .finish()).setParallelism(1);
        input1.cross(input2).setParallelism(4)
                .map(new MyMapFunction()).setParallelism(2)
                .reduce(new MyReduce())
                .writeAsText("file:///root/result/output.txt", FileSystem.WriteMode.OVERWRITE);
        env.execute(jobId + ": MutualJob");
    }
    public static class MyMapFunction extends RichMapFunction<Tuple2<Row, Row>, String> {
        private Jedis jedis;
        private HashMap<String, BigInteger> xMap;
        private HashMap<String, BigInteger> yMap;
        private HashMap<String, BigInteger> xyMap;
        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            jedis = new Jedis("47.104.202.153", 6379);
            jedis.auth("bdilab@1308");
//            ParameterTool.fromPropertiesFile("/home/DataFlow/FlinkJob/conf/args.properties");
            ParameterTool parameterTool = (ParameterTool) getRuntimeContext().getExecutionConfig().getGlobalJobParameters();
//            String jobId = parameterTool.has("jobId") ? parameterTool.get("jobId") : "jobid1";
            String jobId;
            if(parameterTool.has("jobId")){
                jobId = parameterTool.get("jobId");
                xMap = deserialize(jedis.get(jobId+"_x").getBytes(StandardCharsets.UTF_8));
                System.out.println(xMap);
            }
//                throw new RuntimeException("JobId is null!");


//            yMap = deserialize(jedis.get("jobid1_y").getBytes(StandardCharsets.UTF_8));
//            System.out.println(yMap);
//            xyMap = deserialize(jedis.get("jobid1_xy").getBytes(StandardCharsets.UTF_8));
//            System.out.println(xyMap);
        }

        @Override
        public void close() throws Exception {
            super.close();
            if(jedis!=null){
                jedis.close();
            }
        }

        @Override
        public String map(Tuple2<Row, Row> row) throws Exception {
//            System.out.println(((Row) row.getField(1)).getField(0));
//            System.out.println(((Row) row.getField(0)).getField(0));
//            String s = row.getField(0).toString().substring(2) + " - " + row.getField(1);
            //            System.out.println(s);
            String f0 = String.valueOf( ((Row) row.getField(0)).getField(0) );
//            System.out.println(xMap.get(f0));
//            System.out.println("m " + Thread.currentThread().getName());
            Object field = ((Row) row.getField(1)).getField(0);
            if (field instanceof Date){
//                System.out.println(String.valueOf(((Date) field).getTime()));
            }
            return String.valueOf(field);
        }

        @SuppressWarnings("unchecked")
        public HashMap<String, BigInteger> deserialize(byte[] bytes) {
            if (null == bytes || bytes.length <= 0) {
                return null;
            }
            String str = new String(bytes, DEFAULT_CHARSET);
            return JSON.parseObject(str, HashMap.class);
        }
    }

    public static class MyReduce implements ReduceFunction<String>{
        @Override
        public String reduce(String aDouble, String t1) {
            aDouble += t1;
//            System.out.println("r " + Thread.currentThread().getName());
//            System.out.println(aDouble);
            return aDouble;
        }
    }

}
