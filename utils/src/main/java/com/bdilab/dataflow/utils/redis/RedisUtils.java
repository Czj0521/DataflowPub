package com.bdilab.dataflow.utils.redis;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * Redis API Utils.
 *
 * @author wh
 * @version 1.0
 * @date 2021/10/12
 */
@Component
public class RedisUtils {
  @Resource
  private RedisTemplate<String, Object> redisTemplate;

  /**
   * Specify cache expiration time.
   *
   * @param key  key
   * @param time time(second)
   */
  public boolean expire(String key, long time) {
    if (time > 0) {
      redisTemplate.expire(key, time, TimeUnit.SECONDS);
      return true;
    } else {
      throw new RuntimeException("超时时间小于0");
    }
  }

  /**
   * Obtain the expiration time by key.

   * @param key The key cannot be null
   * @return time(second) Return 0 for permanent
   */
  public long getExpire(String key) {
    Long expire = redisTemplate.getExpire(key, TimeUnit.SECONDS);
    return Objects.isNull(expire) ? -1 : expire;
  }

  /**
   * Check whether the key exists.

   * @param key key
   * @return true is exists, false is not exist
   */
  public boolean hasKey(String key) {
    Boolean res = redisTemplate.hasKey(key);
    return !Objects.isNull(res) && res;
  }

  /**
   * Delete the cache.

   * @param key One or more values can be passed
   */
  @SuppressWarnings("unchecked")
  public void del(String... key) {
    if (key != null && key.length > 0) {
      if (key.length == 1) {
        redisTemplate.delete(key[0]);
      } else {
        redisTemplate.delete(CollectionUtils.arrayToList(key));
      }
    }
  }

  // ============================String=============================

  /**
   * Normal cache fetch.

   * @param key key
   */
  public Object get(String key) {
    return key == null ? null : redisTemplate.opsForValue().get(key);
  }

  /**
   * Normal cache drop.

   * @param key  key
   * @param value value
   * @return Rue is success, false is failure
   */
  public boolean set(String key, Object value) {
    redisTemplate.opsForValue().set(key, value);
    return true;
  }


  /**
   * Normal cache is placed and the time is set.

   * @param key   key
   * @param value value
   * @param time  Time Time (s) Time must be greater than 0.
   *              If time is smaller than or equal to 0, the value is set indefinitely.
   * @return True is success, false is failure
   */
  public boolean set(String key, Object value, long time) {
    if (time > 0) {
      redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    } else {
      this.set(key, value);
    }
    return true;
  }


  /**
   * increasing.

   * @param key   key
   * @param delta How many to add (greater than 0)
   */
  public long incr(String key, long delta) {
    if (delta < 0) {
      throw new RuntimeException("递增因子必须大于0");
    }
    Long increment = redisTemplate.opsForValue().increment(key, delta);
    return Objects.isNull(increment) ? -1 : increment;
  }

  /**
   * diminishing.

   * @param key key
   * @param delta How much are we going to subtract (greater than 0)
   */
  public long decr(String key, long delta) {
    if (delta < 0) {
      throw new RuntimeException("递减因子必须大于0");
    }
    Long increment = redisTemplate.opsForValue().increment(key, -delta);
    return Objects.isNull(increment) ? -1 : increment;
  }

  // ================================Map=================================

  /**
   * HashGet.
   *
   * @param key  key
   * @param item item Cannot be null
   */
  public Object hget(String key, String item) {
    return redisTemplate.opsForHash().get(key, item);
  }


  /**
   * Gets all keys corresponding to the hashKey.

   * @param key key
   * @return Corresponding to multiple key values.
   */
  public Map<Object, Object> hmget(String key) {
    return redisTemplate.opsForHash().entries(key);
  }

  /**
   * HashSet.

   * @param key key
   * @param map Corresponding to multiple key values
   * @return True is success, false is failure
   */
  public boolean hmset(String key, Map<?, ?> map) {
    redisTemplate.opsForHash().putAll(key, map);
    return true;
  }

  /**
   * HashSet and set the time.
   *
   * @param key  key
   * @param map  Corresponding to multiple key values
   * @param time time(s)
   * @return True is success, false is failure
   */
  public boolean hmset(String key, Map<String, Object> map, long time) {
    redisTemplate.opsForHash().putAll(key, map);
    if (time > 0) {
      expire(key, time);
    }
    return true;
  }

  /**
   * Puts data into a hash table, or creates it if it doesn't exist.

   * @param key   key
   * @param item  item
   * @param value value
   * @return True is success, false is failure
   */
  public boolean hset(String key, String item, Object value) {
    redisTemplate.opsForHash().put(key, item, value);
    return true;
  }

  /**
   * Puts data into a hash table, or creates it if it doesn't exist.
   *
   * @param key   key
   * @param item  item
   * @param value value
   * @param time  time(s)  Note: If an existing hash table has a time,
   *              this will replace the original time.
   * @return True is success, false is failure
   */
  public boolean hset(String key, String item, Object value, long time) {
    redisTemplate.opsForHash().put(key, item, value);
    if (time > 0) {
      expire(key, time);
    }
    return true;
  }

  /**
   * Delete a value from the hash table.
   *
   * @param key  key, Cannot be null
   * @param item item, Can enable multiple, cannot be null.
   */
  public void hdel(String key, Object... item) {
    redisTemplate.opsForHash().delete(key, item);
  }

  /**
   * Determines whether the value of the item exists in the hash table.
   *
   * @param key  key, Cannot be null
   * @param item item, Cannot be null
   * @return True is present, false is absent.
   */
  public boolean hashHasKey(String key, String item) {
    return redisTemplate.opsForHash().hasKey(key, item);
  }

  /**
   * If the hash increment does not exist, it creates one and returns the new value.

   * @param key  key
   * @param item item
   * @param by   How many to add (greater than 0).
   */
  public double hincr(String key, String item, double by) {
    return redisTemplate.opsForHash().increment(key, item, by);
  }

  /**
   * A hash of diminishing.
   *
   * @param key  key
   * @param item item
   * @param by   How much to subtract (less than 0).
   */
  public double hdecr(String key, String item, double by) {
    return redisTemplate.opsForHash().increment(key, item, -by);
  }

  // ============================set=============================

  /**
   * Gets all values in the Set based on key.

   * @param key key
   */
  public Set<Object> setGet(String key) {
    return redisTemplate.opsForSet().members(key);
  }

  /**
   * Query a set by value to see if it exists.

   * @param key   key
   * @param value value
   * @return True is present, false is absent.
   */
  public boolean setHasKey(String key, Object value) {
    Boolean hasKey = redisTemplate.opsForSet().isMember(key, value);
    return !Objects.isNull(hasKey) && hasKey;
  }

  /**
   * Put the data into the set cache.
   *
   * @param key    key
   * @param values values. It can be multiple.
   * @return The number of successful.
   */
  public long setSet(String key, Object... values) {
    Long size = redisTemplate.opsForSet().add(key, values);
    return Objects.isNull(size) ? -1 : size;
  }

  /**
   * Put the set data into the cache.

   * @param key    key
   * @param time   time(s)
   * @param values values
   * @return The number of successful.
   */
  public long setSetAndTime(String key, long time, Object... values) {
    final Long count = redisTemplate.opsForSet().add(key, values);
    if (time > 0) {
      expire(key, time);
    }
    return Objects.isNull(count) ? -1 : count;
  }

  /**
   * Gets the length of the set cache.

   * @param key key
   */
  public long setGetSetSize(String key) {
    Long size = redisTemplate.opsForSet().size(key);
    return Objects.isNull(size) ? -1 : size;
  }

  /**
   * Remove values of value.

   * @param key    key
   * @param values values. It can be multiple.
   * @return Number of removals.
   */
  public long setRemove(String key, Object... values) {
    final Long count = redisTemplate.opsForSet().remove(key, values);
    return Objects.isNull(count) ? -1 : count;
  }

  // ===============================list=================================

  /**
   * Gets the contents of the list cache.

   * @param key   key
   * @param start start
   * @param end   end. 0 to -1 represent all values.
   */
  public List<Object> listGet(String key, long start, long end) {
    return redisTemplate.opsForList().range(key, start, end);
  }

  /**
   * Gets the length of the list cache.

   * @param key key
   */
  public long listGetListSize(String key) {
    Long size = redisTemplate.opsForList().size(key);
    return Objects.isNull(size) ? -1 : size;
  }

  /**
   * Get the values in the list by index.

   * @param key   key
   * @param index Index >=0, 0, 1, second element, and so on;
   *             When index<0, -1, the end of the table, the next-to-last element of -2, and so on.
   */
  public Object listGetIndex(String key, long index) {
    return redisTemplate.opsForList().index(key, index);
  }

  /**
   * Put the list in the cache.

   * @param key   key
   * @param value value
   */
  public boolean listSet(String key, Object value) {
    redisTemplate.opsForList().rightPush(key, value);
    return true;
  }

  /**
   * Put the list in the cache.

   * @param key   key
   * @param value value
   * @param time  time(s)
   */
  public boolean listSet(String key, Object value, long time) {
    redisTemplate.opsForList().rightPush(key, value);
    if (time > 0) {
      expire(key, time);
    }
    return true;
  }

  /**
   * Put the list in the cache.

   * @param key   key
   * @param value value
   */
  public boolean listSetList(String key, List<Object> value) {
    redisTemplate.opsForList().rightPushAll(key, value);
    return true;
  }

  /**
   * Put the list in the cache.

   * @param key   key
   * @param value value
   */
  public boolean listSetList(String key, List<Object> value, long time) {
    redisTemplate.opsForList().rightPushAll(key, value);
    if (time > 0) {
      expire(key, time);
    }
    return true;
  }

  /**
   * Modify an item in the list based on the index.

   * @param key   key
   * @param index index
   * @param value value
   */
  public boolean listUpdateIndex(String key, long index, Object value) {
    redisTemplate.opsForList().set(key, index, value);
    return true;
  }

}
