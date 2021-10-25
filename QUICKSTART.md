## DataFlow环境安装及部署（后端）

### 版本修订记录

| 序号 | 时间       | 版本 |
| ---- | ---------- | ---- |
| 1    | 2021/10/22 |      |
| 2    | 2021/10/25 |      |


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

#### clickhouse安装部署,导入数据表

##### 1.系统要求

ClickHouse可以在任何具有x86_64，AArch64或PowerPC64LE CPU架构的Linux，FreeBSD或Mac OS X上运行。

下面是检查当前CPU是否支持SSE 4.2的命令:

```bash
$ grep -q sse4_2 /proc/cpuinfo && echo "SSE 4.2 supported" || echo "SSE 4.2 not supported"
```

若不支持sse4.2可以通过源代码安装方式（亲测不支持也能用RPM安装）

##### 2.`RPM`安装包

clickhouse有三种安装方式，下面介绍最简单的RPM安装方式

1.添加官方存储库

```
sudo yum install yum-utils
sudo rpm --import https://repo.clickhouse.com/CLICKHOUSE-KEY.GPG
sudo yum-config-manager --add-repo https://repo.clickhouse.com/rpm/stable/x86_64
```

（若不能访问请移步到官网https://clickhouse.com/docs/zh/getting-started/install/查看最新地址）

2.运行命令安装

```
sudo yum install clickhouse-server clickhouse-client
```

##### 3.启动

```
sudo /etc/init.d/clickhouse-server start
```

##### 4.启动客户端测试clickhouse

1.启动客户端

```
clickhouse-client
```

2.运行sql

```
select 1
```

##### 5.配置远程连接

1.打开clickhouse配置文件

```
vi /etc/clickhouse-server/config.xml
```

2.修改配置文件

```
ctrl F 查找<listen_host>0.0.0.0</listen_host>
去掉<-- <listen_host>0.0.0.0</listen_host> -->两边的注释
保存退出
```

##### 6.创建表

可以直接使用远程连接的客户端软件，如Dbeaver，datagrip等

若使用clickhouse自带的命令行客户端

```
1.创建dataflow数据库
clickhouse-client --query "CREATE DATABASE IF NOT EXISTS dataflow"
2.创建table(尽量将语句写到一行，不然会报错)
clickhouse-client --query "CREATE TABLE dataflow.airuuid
(

    `time` Nullable(Date),

    `city` Nullable(String),

    `AQI` Nullable(UInt8),

    `PM2_5` Nullable(UInt8),

    `PM10` Nullable(UInt8),

    `SO2` Nullable(UInt8),

    `NO2` Nullable(UInt8),

    `CO` Nullable(Float64),

    `O3` Nullable(UInt8),

    `primary_pollutant` Nullable(String)
)
ENGINE = Memory"
(有关clickhouse引擎详见官方文档)
3.将准备好的csv文件插入到创建的表中
clickhouse-client --query "INSERT INTO tutorial.airuuid FORMAT CSV" --max_insert_block_size=100000 < airuuid.csv
4.select count(*) from [表名] 检验表中是否有数据
```

#### Flink1.12.5单机，standalone集群搭建

##### 1.下载flink1.12.5安装包

```
wget https://downloads.apache.org/flink/flink-1.13.3/flink-1.13.3-bin-scala_2.12.tgz
```

##### 2.解压

```
tar -zxvf flink-1.13.3-bin-scala_2.12.tgz
```

##### 3.修改flink-1.12.5/conf/flink-conf.xml

```
jobmanager.rpc.address: 47.104.202.153//改为master的ip，单机模式可以不改
jobmanager.rpc.port: 6123 //flink应用的端口，建议不改
taskmanager.numberOfTaskSlots: 8//flink任务槽数量，建议设置为cpu核数 
parallelism.default: 1//并行度默认值，可改可不改
```

##### 4.修改master，worker文件（单机模式跳过此步骤，standalone集群模式需要配置）,目前为单机模式

```
master文件
[ip1或主机名]:8081//写哪台机器作为主节点
worker文件
[ip1或主机名]//写哪些机器作为worker节点
[ip2或主机名]
[ip3或主机名]
```

##### 5.scp分发到集群的每个主机

```
1.配置ssh免密登录
ssh-keygen -t rsa 
2.将每台主机的/root/.ssh/id_rsa.pub的内容复制，粘贴到另外所有主机的/root/.ssh/authorized_keys中
3.scp -r flink-1.12.5/ root@[ip]/[安装路径] 分发到所有主机
```

##### 6.启动flink集群

```
flink-1.12.5/bin/start-cluster.sh//启动flink集群
flink-1.12.5/bin/stop-cluster.sh//关闭flink集群
先关闭再启动就是重启
（若没配置免密要一直输密码）
```

##### 7.去浏览器访问 47.104.202.153:8081查看flink集群当前状态

#### MySQL安装说明

MySQL使用Docker安装

##### 1.下载镜像docker pull

dockerhub中搜索MySQL，找到对应版本号，下载

```bash
docker pull mysql:5.7
```

查看镜像

```
docker images
```

##### 2.创建实例并启动

```bash
sudo docker run \
-p 3306:3306 \
--name mysql \
-v /usr/mysql/conf:/etc/mysql \
-v /usr/mysql/log:/var/log/mysql \
-v /usr/mysql/data:/var/lib/mysql \
-e MYSQL_ROOT_PASSWORD=bdilab@1308 \
-d mysql:5.7
	
# -p：表示端口映射，冒号左面的是宿主机的端口，而右侧则表示的是MySQL容器内的端口
# --name：给MySQL容器取的名字
# -d：表示后台运行
# -e MYSQL_ROOT_PASSWORD：设置root用户密码
# -v：表示挂载路径，冒号左面的表示宿主机的挂载目录，冒号右边则表示容器内部的路径。
```

##### 3.创建配置文件

```bash
vim /usr/mysql/conf/my.cnf
```

```
[client]
default-character-set=utf8

[mysql]
default-character-set=utf8

[mysqld]
character-set-server=utf8
skip-name-resolve
```

##### 4.进入mysql容器内查看

```bash
docker exec -it mysql /bin/bash
```

##### 5.重启mysql

```bash
docker restart mysql
```

##### 6.设置自动重启

```bash
docker update mysql --restart=always
```



#### Redis安装说明

Redis也使用Docker安装

##### 1.下载镜像

```bash
docker pull redis
```

##### 2.创建实例并启动

```bash
sudo docker run \
-p 6379:6379 \
--name redis \
-v /usr/redis/conf/redis.conf:/etc/redis/redis.conf \
-v /usr/redis/data:/data \
-d redis redis-server /etc/redis/redis.conf \
--requirepass bdilab@1308

# 注意，直接运行此命令会将redis.conf作为一个目录创建，故先创建redis.conf文件
```

##### 3.测试redis

```bash
docker exec -it redis redis-cli
```

##### 4.数据持久化

```shell
# redis.conf下添加
appendonly yes

# 重启容器
docker restart redis
```

##### 5.设置自动重启

```bash
docker update redis --restart=always
```






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

运行启动类 DataFlow/BackGround/DataFlowBackGround/src/main/java/com/bdilab/dataflow/DataFlowApplication.java （config模块下）

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

### 二、前端测试说明

```
#项目地址
https://github.com/xdsselab/DataFlow
```
frontend为前端项目代码

