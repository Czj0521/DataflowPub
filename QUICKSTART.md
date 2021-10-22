## DataFlow环境安装及部署（后端）

### 版本修订记录

| 序号 | 时间       | 版本 |
| ---- | ---------- | ---- |
| 1    | 2021/10/22 |      |



### 一、基础环境

#### 1.1 硬件环境

#### 1.2 软件环境

| 名称       | 组件与版本 | 形式 |
| ---------- | ---------- | ---- |
| 系统       | CentOS7.6  |      |
| Clickhouse | v21.10     | 集群 |
| Flink      | v1.13.3    | 集群 |
| Mysql      | v5.6       | 单点 |
| Redis      | v4.0.8     | 单点 |

### 二、环境安装说明

技术栈部署服务器ip：47.104.202.153（公）172.31.121.149（私有）
所有密码全为：bdilab@1308

目前所有软件都为单点部署。

### 三、平台部署说明

#### 3.1 本地测试使用

##### 0）基本说明

```
#项目地址
https://github.com/xdsselab/DataFlow
```

BackGround为后端项目代码，其中包含两个模块：DataFlowBackGround和FlinkJob。

- DataFlowBackGround为基于SpringBoot框架的核心程序模块；
- FlinkJob为使用flink为计算引擎的操作符的单独程序模块，分离出来用于轻量化flink job任务（目前使用rest api 进行任务提交）。

##### 1）git拉取项目

```
#git地址：
https://github.com/xdsselab/DataFlow.git
```

##### 2）修改配置文件

修改配置文件 DataFlow/BackGround/DataFlowBackGround/src/main/resources/application.yml 中的技术栈地址和数据库名称

##### 3）运行核心程序

运行启动类 DataFlow/BackGround/DataFlowBackGround/src/main/java/com/bdilab/dataflow/DataFlowApplication.java

##### 4）测试使用

目前版本通过 http://127.0.0.1:8080/swagger-ui.html 访问测试程序接口界面

###### ① 数据集创建

**注：因平台功能尚未完善，clickhouse（数据集）的插入需手动进行。（可不测试，服务器已存在数据集）**

数据集以air.csv为例，手动向clickhouse插入数据集：

```
create table databoard_gluttony.airuuid
(
    time              Date,
    city              String,
    AQI               UInt8,
    PM2_5             UInt8,
    PM10              UInt8,
    SO2               UInt8,
    NO2               UInt8,
    CO                Float64,
    O3                UInt8,
    primary_pollutant String
)
    engine = MergeTree PARTITION BY toYYYYMM(time)
        ORDER BY (time, city)
        SETTINGS index_granularity = 8192;
```

数据集统计数据插入mysql数据库，调用接口/savastatistic

![image-20211022181145811](https://gitee.com/thitve/for-pic-go/raw/master/PicGo_img/20211022181145.png)

###### ② 以clickhouse为计算引擎的操作符

**测试一**：使用PivotChart

![image-20211022181318013](https://gitee.com/thitve/for-pic-go/raw/master/PicGo_img/20211022181318.png)

```
#/api/v1/gluttony/job/pivotChart接口输入
{
  "job": "start_job",
  "jobType": "PivotChart",
  "jobDescription": {
    "dataSource": "airuuid",
    "axisDimension": "1",
    "mark": "bar",
    "operations": [
      {
      "type": "x-axis",
      "operation": {  "attribute": "AQI",
                      "binning": "none",
                      "aggregation": "count",
                      "sort": "none"
                      }
      }
    ],
    "output": "createView"
  },
  "jobId":"1212",
  "workspaceId": "4f5s4f25s4g8z5eg"
}
```

输出坐标轴信息和行高即为运行成功。

**测试二**：使用Profiler

![image-20211022182458984](https://gitee.com/thitve/for-pic-go/raw/master/PicGo_img/20211022182459.png)

```
#/api/v1/gluttony/job/profiler接口输入
{
  "job": "start_job",
  "jobType": "ProfilerChart",
  "jobDescription": {
    "input": "airuuid",
    "column":"NO2"
  },
  "jobId":"1212",
  "workspaceId": "4f5s4f25s4g8z5eg"
}
```

输出column的各种信息即为运行成功。

**测试三**：使用Table

![image-20211022184615962](https://gitee.com/thitve/for-pic-go/raw/master/PicGo_img/20211022184616.png)

```
{
     "filter":"(age>30 AND name = 'jack' AND startWith(street,"czk"))",
     "project":["name","age","max(age)"],
     "group":["name","age"],
     "limit":2000
}
```

输出table的数据即为运行成功。

###### ③ 以flink为计算引擎的操作符（暂不建议使用）

**（功能不完善，仅有一个测试demo且操作复杂暂时不要使用，可自行编写测试demo，或可联系作者进行了解）**

**对于flink的使用，因未完成接口代码编写，仅可通过测试类进行测试。Flink Job测试demo jar包和REST API封装已完成，可进行调用。**

测试四：

demo jar包来源于FlinkJob模块打包程序。本测试demo需用使用redis保存中间结果，redis数据来源于clickhouse计算的结果。demo jar包输入参数包括：

```
-entry-class com/bdilab/dataflow/flink/job/Mutual.java
-datasource airuuid
-column1 city
-type1 String
-column2 AQI
-type2 Integer
-jobid1 jobid1     #对应redis key值
```

#### 3.2 服务器部署

## DataFlow环境安装及部署（前端）

### 一、基础环境

#### 1.1 硬件环境

#### 1.2 软件环境

| 名称       | 组件与版本 |
| ---------- | ---------- |
| React      | v17.0.2    | 
| antv x6    | v1.24.4    | 
| ant design | v4.14.1    | 
| Echarts    | v5.0.2     | 
