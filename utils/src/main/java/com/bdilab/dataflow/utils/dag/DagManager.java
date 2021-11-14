package com.bdilab.dataflow.utils.dag;

import com.bdilab.dataflow.utils.redis.RedisUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DagManager {
  @Resource
  private RedisUtils redisUtils;

//  public void createDag(String workspaceId) {
//    if (!containsWorkspaceId(workspaceId)) {
//      redisUtils.hmset(workspaceId, new HashMap<>(Dag.MAX_SIZE));
//    } else {
//      throw new RuntimeException("This workspace id already exists !");
//    }
//  }

  public void deleteDag(String workspaceId) {
    redisUtils.del(workspaceId);
  }

  public boolean containsWorkspaceId(String workspaceId) {
    return redisUtils.hasKey(workspaceId);
  }
}
