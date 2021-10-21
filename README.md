![logo](https://user-images.githubusercontent.com/36458292/138196624-ec608ac5-45db-44e3-b25e-0647647daa41.png)


# DataFlow
&emsp; &emsp; DataFlow是一个交互式数据科学平台，可以使用户可以在不具有软件编程能力和机器学习专业知识的情况下，也能构建复杂的数据科学工作流程。不需要技术背景，用户可以通过DataFlow直观的用户界面，前沿的自动化机器学习系统和最新的数据挖掘工具执行高级分析。DataFlow可以通过处理复杂以及重复的任务，帮助各个领域内的专家轻松地对他们的数据进行分析以得出他们需要的结论。

## 相关概念

## 数据分析
&emsp; &emsp; DataFlow的核心功能是数据分析，可以细分为数据操纵（Data Manipulation）、描述性分析（Descriptive Analytics）、预测性分析（Predictive Analytics）、建议性分析（Prescriptive Analytics）和用户自定义分析（User-Defined Analytics）5类，上述分析能力均以对应的分析操作符（operator）实现，具体的操作符列举如下表所示。
| 操作符类别 | 操作符名称 | 操作符描述 | 实现方式|
| :-----:| :----: | :---- |:----:|
| 数据操纵 | Transpose | 用来计算在特定筛选条件下的表格数据 | ClickHouse|
|  | Transformation | 对表格某一列进行转换操作（替换、二元化、自定义分类等），生成新的一列 | ClickHouse |
|  | Python | 将表格转换成python的dataframe对象，编写python代码直接对dataframe对象进行操作 | 尚不确定如何实现 |
|  | Join | 两个表格进行join操作 | ClickHouse |
|  | Filter | 对表格进行过滤操作 | ClickHouse |
|  | Materialize | 对表格进行保存操作 | ClickHouse |
| 描述性分析 | Statistical Test | 能够对给定的两个属性进行统计检验，主要用于检验两个数据集分布情况的差异 | Flink |
|  | Correlation | 对自变量的相关性进行分析。他的输入是用户的自变量，输出是各个自变量之间的相关性 | Flink |
|  | Profiler | 分析数据集中每个属性的分布与特点（最大值、最小值、平均值、标准差等） | ClickHouse |
|  | Key Driver Analysis（KDA） | 关键驱动因素分析能够自动发现数据中的基本模式/驱动因素，可以用来表征单个群体或者找到两个群体之间的差异 | Flink |
|  | Pivot Chart | 生成一个数据集合的数据透视图（即各类数据分析图） | ClickHouse |
|  | Table | 对表格进行展示，可以添加过滤、聚合、转换操作 | ClickHouse |
| 预测性分析 | TextFeaturizer | 用于获得单词或句子的向量表示 | Flink |
|  | Mutual Information | 求解出一个数据表格中每两个属性之间的互信息大小 | Flink+ClickHouse |
|  | Forecast | 用于执行时间序列预测，给定一个特定时间范围内记录的数据组成的数据集，生成模型来预测未来时间点的数据值 | 尚不确定如何实现 |
|  | AutoML | 使用户能够以交互的方式构建预测模型，可以自动评估各种机器学习模型和预处理程序，从而构建出最能解决用户问题的管道。 | 尚不确定如何实现 |
| 建议性分析 | What If | 允许用户观察自定义变量在指定的各种场景下的行为，用户可以更好地理解从一系列可能的决定中选择什么对他们最有效 | ClickHouse |
| 用户定义操作符 | UDO | 组件无法满足用户的使用需求时，用户可以通过创建一个自定义的操作符（UDO）来内置自定义操作符 | 尚不确定如何实现 |


## 采用开源软件情况
- ClickHouse：Apache 2.0
- Redis：Three clause BSD license：https://redis.io/topics/license
- MySQL：https://www.mysql.com/cn/about/legal/licensing/oem/
- Flink：Apache 2.0
- SpringBoot: Apache License 2.0

## 关键技术
- 数据分析
- 数据联动
- 渐进式计算
- 机器学习
