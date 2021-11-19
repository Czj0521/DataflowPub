package com.bdilab.dataflow.utils.dag;

import com.bdilab.dataflow.utils.redis.RedisUtils;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * Records the filter statement of the filter type node in the DAG.
 *
 * @author wh
 * @date 2021/11/19
 */
@Component
public class DagFilterManager {
  private static final String KEY_SUFFIX = "_filter";

  @Resource
  RedisUtils redisUtils;

  /**
   * Add or update one filter statement information.
   *
   * @param workspaceId workspace ID
   * @param nodeId node ID
   * @param newFilter new node filter info
   */
  public void addOrUpdateFilter(String workspaceId, String nodeId, String newFilter) {
    workspaceId = workspaceId + KEY_SUFFIX;
    redisUtils.hset(workspaceId, nodeId, newFilter);
  }

  /**
   * Delete one filter statement information.
   *
   * @param workspaceId workspace ID
   * @param nodeId node ID
   */
  public void deleteFilter(String workspaceId, String nodeId) {
    workspaceId = workspaceId + KEY_SUFFIX;
    redisUtils.hdel(workspaceId, nodeId);
  }

  /**
   * Get one filter statement information.
   *
   * @param workspaceId workspace ID
   * @param nodeId node ID
   * @return filter info
   */
  public String getFilter(String workspaceId, String nodeId) {
    workspaceId = workspaceId + KEY_SUFFIX;
    return (String) redisUtils.hget(workspaceId, nodeId);
  }

  /**
   * Get all filter statement information.
   *
   * @param workspaceId workspace ID
   * @return all node filter info
   */
  public Map<String, String> getAllFilter(String workspaceId) {
    workspaceId = workspaceId + KEY_SUFFIX;
    Map<String, String> allFilterMap = new HashMap<>();
    redisUtils.hmget(workspaceId).forEach((k, v) -> {
      allFilterMap.put((String) k, (String) v);
    });
    return allFilterMap;
  }

  /**
   * Overwrite and update all filter statement information.
   *
   * @param workspaceId workspace ID
   * @param newFilter new node filter info
   */
  public void updateAllFilter(String workspaceId, Map<String, String> newFilter) {
    workspaceId = workspaceId + KEY_SUFFIX;
    Map<Object, Object> temp = new HashMap<>();
    newFilter.forEach(temp::put);
    redisUtils.del(workspaceId);
    redisUtils.hmset(workspaceId, temp);
  }
}

