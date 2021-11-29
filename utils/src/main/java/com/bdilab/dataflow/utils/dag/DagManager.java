package com.bdilab.dataflow.utils.dag;

import com.bdilab.dataflow.utils.redis.RedisUtils;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Manager all DAG.
 *
 * @author wh
 * @date 2021/11/19
 */
@Component
public class DagManager {
  @Resource
  private RedisUtils redisUtils;
  @Resource
  RealTimeDag realTimeDag;

  //  public void createDag(String workspaceId) {
  //    if (!containsWorkspaceId(workspaceId)) {
  //      redisUtils.hmset(workspaceId, new HashMap<>(Dag.MAX_SIZE));
  //    } else {
  //      throw new RuntimeException("This workspace id already exists !");
  //    }
  //  }

  public void deleteDag(String workspaceId) {
    realTimeDag.clearDag(workspaceId);
  }

  public boolean containsWorkspaceId(String workspaceId) {
    return redisUtils.hasKey(workspaceId);
  }
}
