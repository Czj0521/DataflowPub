package com.bdilab.dataflow.service.impl;

import com.bdilab.dataflow.clickhouse.ClickHouseJdbcUtils;
import com.bdilab.dataflow.dto.jobdescription.MutualDescription;
import com.bdilab.dataflow.utils.DataTypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author:zhb
 * @createTime:2021/9/26 15:20
 */

@Slf4j
@Service
public class MutualServiceImpl {
    @Autowired
    private ClickHouseJdbcUtils clickHouseJdbcUtils;
    @Autowired
    private TableMetadataServiceImpl metadataService;

    /**
     * 互信息求解
     *
     * @param mutualDescription
     * @return
     */
    public Map<String, Double> getMutual(MutualDescription mutualDescription) {
        Map<String, Double> res = new HashMap<>();
        String dataSource = mutualDescription.getDataSource();
        String target = mutualDescription.getTarget();
        List<String> features = mutualDescription.getFeatures();
        Set<String> dataType = DataTypeUtils.getNumber();

        // 结果集初始化
        for (String feature : features) {
            res.put(feature, 0.0);
        }

        ArrayList<String> list = new ArrayList<>(features);
        list.add(target);
        String sql = generateSQL(dataSource, list);
        log.info(sql);
        Map<String, String> metadata = metadataService.metadata(sql);

        // 判断target是否为数值类型
        if (!dataType.contains(metadata.get(target))) {
            return res;
        }

        // 过滤出为数值类型的列
        List<String> collect = list.stream()
                .filter(col -> dataType.contains(metadata.get(col)))
                .collect(Collectors.toList());

        // 查询出对应列的数据
        List<Map<String, Object>> query = clickHouseJdbcUtils.queryForList(generateSQL(dataSource, collect));
        Map<String, List<Double>> datas = new HashMap<>();
        for (Map<String, Object> map : query) {
            for (String col : map.keySet()) {
                double val = Double.valueOf(map.get(col).toString());
                if (datas.containsKey(col)) {
                    datas.get(col).add(val);
                } else {
                    List<Double> vals = new ArrayList<>();
                    vals.add(val);
                    datas.put(col, vals);
                }
            }
        }

        // 计算各数值列互信息
        List<Double> yKeys = datas.get(target);
        for (String col : datas.keySet()) {
            if (!col.equals(target)) {
                res.put(col, getMutual(datas.get(col), yKeys));
            }
        }

        return res;
    }

    public String generateSQL(String dataSource, List<String> columns) {
        String columnGroup = StringUtils.join(columns, ',');
        return "select " + columnGroup + " from " + dataSource + " limit 2000 ";
    }

    /**
     * 互信息计算
     *
     * @param xKeys
     * @param yKeys
     * @return
     */
    public Double getMutual(List<Double> xKeys, List<Double> yKeys) {
        Map<Double, Integer> marginalDistributionX = getMarginalDistribution(xKeys);
        Map<Double, Integer> marginalDistributionY = getMarginalDistribution(yKeys);
        Map<Double, Map<Double, Integer>> jointDistribution = getJointDistribution(xKeys, yKeys);

        int size = xKeys.size();
        double res = 0;

        for (double x : marginalDistributionX.keySet()) {
            for (double y : marginalDistributionY.keySet()) {
                double pXY = 0;
                if (jointDistribution.containsKey(x)) {
                    Map<Double, Integer> cur = jointDistribution.get(x);
                    pXY = (double) cur.getOrDefault(y, 0) / size;
                }

                double pX = (double) marginalDistributionX.getOrDefault(x, 0) / size;

                double pY = (double) marginalDistributionY.getOrDefault(y, 0) / size;

                if (pX != 0 && pY != 0 && pXY != 0) {
                    // 以2为底
                    res += pXY * (Math.log(pXY / (pX * pY)) / Math.log(2.0));
                }

            }
        }
        // 四舍五入保留5位小数
        return new BigDecimal(res).setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 获取边缘分布
     *
     * @param keys
     * @return
     */
    public Map<Double, Integer> getMarginalDistribution(List<Double> keys) {
        Map<Double, Integer> res = new HashMap<>();
        for (Double key : keys) {
            res.put(key, res.getOrDefault(key, 0) + 1);
        }
        return res;
    }

    /**
     * 获取联合分布
     *
     * @param xKeys
     * @param yKeys
     * @return
     */
    public Map<Double, Map<Double, Integer>> getJointDistribution(List<Double> xKeys, List<Double> yKeys) {
        Map<Double, Map<Double, Integer>> res = new HashMap<>();
        for (int i = 0; i < xKeys.size(); i++) {
            double x = xKeys.get(i);
            double y = yKeys.get(i);
            Map<Double, Integer> cur;
            if (res.containsKey(x)) {
                cur = res.get(x);
                cur.put(y, cur.getOrDefault(y, 0) + 1);
            } else {
                cur = new HashMap<>();
                cur.put(y, 1);
            }
            res.put(x, cur);
        }

        return res;
    }
}
