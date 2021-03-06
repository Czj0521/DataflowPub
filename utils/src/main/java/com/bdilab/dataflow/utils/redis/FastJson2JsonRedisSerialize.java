package com.bdilab.dataflow.utils.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * FastJson2JsonRedisSerialize for redis serialize.
 *
 * @author wh
 * @version 1.0
 * @date 2021/10/12
 */
public class FastJson2JsonRedisSerialize<T> implements RedisSerializer<T> {

  public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

  private Class<T> clazz;

  static {
    ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
  }

  public FastJson2JsonRedisSerialize(Class clazz) {
    super();
    this.clazz = clazz;
  }


  /**
   * Serialization.
   */
  @Override
  public byte[] serialize(T t) throws SerializationException {
    if (null == t) {
      return new byte[0];
    }
    return JSON.toJSONString(t, SerializerFeature.WriteClassName).getBytes(DEFAULT_CHARSET);
  }

  /**
   * des serialization.
   */
  @Override
  public T deserialize(byte[] bytes) throws SerializationException {
    if (null == bytes || bytes.length <= 0) {
      return null;
    }
    String str = new String(bytes, DEFAULT_CHARSET);
    return (T) JSON.parseObject(str, clazz);
  }
}