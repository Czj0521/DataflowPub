package com.bdilab.dataflow.utils.dag;

import com.bdilab.dataflow.utils.redis.RedisUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * No real time dag for dataflow.
 * Call method loadDagFromRedis() to load from redis at first,
 * and call method saveDagToRedis() to store at last.
 *
 * @author wh
 * @date 2021/11/11
 */
@Component
public class NoRealTimeDag extends Dag {
  @Resource
  private RedisUtils redisUtils;
  @Resource
  RedisTemplate<String, Object> redisTemplate;

  private String workspaceId;


  public void loadDagFromRedis(String workspaceId) {
    this.workspaceId = workspaceId;
    //todo 设置写锁，或乐观锁https://blog.csdn.net/huangjun0210/article/details/84386851
    super.clearDag();
    if (checkRedis()) {
      Map<Object, Object> dag = redisUtils.hmget(workspaceId);
      dag.forEach((k, v) -> super.addNode((DagNode) v));
    }
  }

  public void saveDagToRedis() {
    Map<String, DagNode> dagMap = super.getDagMap();
    Map<String, Object> redisMap = new HashMap<>(dagMap.size());
    dagMap.forEach(redisMap::put);
    redisUtils.del(workspaceId);
    redisUtils.hmset(workspaceId, redisMap);
  }

  private boolean checkRedis() {
    if (redisUtils.hasKey(workspaceId)) {
      return true;
    }
    return false;
  }
}
