package com.ywrain.cache.redis.service;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.common.collect.Sets;
import com.lambdaworks.redis.RedisException;
import com.lambdaworks.redis.ScriptOutputType;
import com.lambdaworks.redis.SetArgs;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.ywrain.cache.redis.configs.RedisConfig;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * redis命令--字节存储操作
 *
 * @author xuguangming@ywrain.com
 * @date create in 2019/3/14
 */
@Component
public class CustomBytesRedisCmdService implements CustomBytesRedisCmd {

    @Resource(name = "DefaultCustomBytesRedisCommands")
    private RedisCommands<String, byte[]> redisCommands;

    // 使用Protostuff转换
    private byte[] toProtostuffBytes(String obj) {
        return obj.getBytes();
    }

    private <T extends Object> byte[] toBytes(T obj) {
        if (obj == null) {
            throw new RedisException("the object cant be null");
        }
        if (obj instanceof byte[]) {
            return (byte[]) obj;
        }
        if (obj instanceof String) {
            return ((String) obj).getBytes(RedisConfig.UTF8);
        }
        return toProtostuffBytes(obj, LinkedBuffer.DEFAULT_BUFFER_SIZE);
    }

    private <T> byte[] toProtostuffBytes(T obj, int DEFAULT_BUFFER_SIZE) {
        Class<T> clazz = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = RuntimeSchema.getSchema(clazz);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new RedisException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    private String decodeToString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return new String(bytes, RedisConfig.UTF8);
    }

    private <T> T decodeToObject(byte[] bytes, Class<T> clazz) {
        if (bytes == null) {
            return null;
        }
        if (clazz.equals(String.class)) {
            return (T) decodeToString(bytes);
        }
        if (clazz.isInterface() || clazz.isPrimitive() || clazz.isArray() || Modifier.isAbstract(clazz.getModifiers())) {
            throw new RedisException("the object clazz cant be an Primitive class nor an abstract class / interface / array: " + clazz.getName());
        }
        Schema<T> schema = RuntimeSchema.getSchema(clazz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, obj, schema);
        return obj;
    }

    private <T> byte[][] toArray(T... values) {
        if (values.length <= 0) {
            throw new RedisException("the object clazz must be array");
        }
        Class<?> clazz = values[0].getClass().getSuperclass();
        if (clazz.isInterface() || clazz.isArray() || Modifier.isAbstract(clazz.getModifiers())) {
            throw new RedisException("the object super clazz cant be an abstract class / interface / array: " + clazz.getName());
        }
        byte[][] sarray = new byte[values.length][];
        for (int i = 0; i < values.length; i++) {
            sarray[i] = toBytes(values[i]);
        }
        return sarray;
    }

    @Override
    public long remove(String... keys) {
        return redisCommands.del(keys);
    }

    @Override
    public Long removeStrKeyIfEquals(String key, String val) {
        String scripts = "if redis.call('type', KEYS[1]).ok == 'string' then if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) end end return 0";
        return redisCommands.eval(scripts, ScriptOutputType.INTEGER, new String[]{key}, val.getBytes());
    }

    @Override
    public Boolean expire(String key, long expireCd) {
        return redisCommands.expire(key, expireCd);
    }

    @Override
    public Boolean expireAt(String key, Date timestamp) {
        return redisCommands.expireat(key, timestamp);
    }

    /**
     * 判断指定的键是否都存在
     *
     * @param keys 一组键
     * @return TRUE | FALSE
     */
    public boolean exists(String... keys) {
        Long ret = redisCommands.exists(keys);
        return ret != null && ret == keys.length;
    }

    /**
     * 判断一组键是否任意存在一个
     *
     * @param keys 一组键
     * @return TRUE | FALSE
     */
    public boolean existsAny(String... keys) {
        Long ret = redisCommands.exists(keys);
        return ret != null && ret >= 1;
    }

    @Override
    public long ttl(String key) {
        Long ttl = redisCommands.ttl(key);
        if (ttl != null && ttl.longValue() > 0) {
            return ttl.longValue();
        }
        return 0;
    }

    @Override
    public String get(String key) {
        byte[] bytes = redisCommands.get(key);
        return decodeToString(bytes);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
            throw new RedisException("the object clazz cant be an abstract class nor interface: " + clazz.getName());
        }
        byte[] bytes = redisCommands.get(key);
        return decodeToObject(bytes, clazz);
    }

    @Override
    public <T> boolean set(String key, T value) {
        String str = redisCommands.set(key, toBytes(value));
        return "OK".equalsIgnoreCase(str);
    }

    @Override
    public <T> boolean setEx(String key, T value, long expireSeconds) {
        if (expireSeconds < 0) {
            throw new RedisException("expireSeconds cant less than 0 : " + expireSeconds);
        }
        return "OK".equalsIgnoreCase(redisCommands.setex(key, expireSeconds, toBytes(value)));
    }

    @Override
    public <T> boolean setPx(String key, T value, long expireMilliseconds) {
        if (expireMilliseconds < 0) {
            throw new RedisException("expireMilliseconds cant less than 0 : " + expireMilliseconds);
        }
        return "OK".equalsIgnoreCase(redisCommands.psetex(key, expireMilliseconds, toBytes(value)));
    }

    @Override
    public <T> boolean setNx(String key, T value) {
        return redisCommands.setnx(key, toBytes(value));
    }

    @Override
    public <T> boolean setExNx(String key, T value, long expireSeconds) {
        SetArgs args = new SetArgs();
        args.ex(expireSeconds).nx();
        return "OK".equalsIgnoreCase(redisCommands.set(key, toBytes(value), new SetArgs().ex(expireSeconds).nx()));
    }

    @Override
    public <T> boolean setPxNx(String key, T value, long expireMilliseconds) {
        SetArgs args = new SetArgs();
        args.ex(expireMilliseconds).nx();
        return "OK".equalsIgnoreCase(redisCommands.set(key, toBytes(value), new SetArgs().nx().px(expireMilliseconds)));
    }

    @Override
    public String getSet(String key, String value) {
        byte[] bytes = redisCommands.getset(key, toBytes(value));
        return decodeToString(bytes);
    }

    @Override
    public Long inc(String key) {
        return redisCommands.incr(key);
    }

    @Override
    public Long incBy(String key, long inc) {
        return redisCommands.incrby(key, inc);
    }

    @Override
    public boolean hexists(String key, String field) {
        return redisCommands.hexists(key, field);
    }

    @Override
    public Set<String> hkeys(String key) {
        List<String> keys = redisCommands.hkeys(key);
        return Sets.newHashSet(keys);
    }

    @Override
    public List<String> hvals(String key) {
        List<byte[]> values = redisCommands.hvals(key);
        List<String> vals = new ArrayList<>();
        for (int i = 0; i < values.size(); i++) {
            vals.add(new String(values.get(i), RedisConfig.UTF8));
        }
        return vals;
    }

    @Override
    public Long hdel(String key, String... fields) {
        return redisCommands.hdel(key, fields);
    }

    @Override
    public String hget(String key, String field) {
        byte[] bytes = redisCommands.hget(key, field);
        return decodeToString(bytes);
    }

    @Override
    public <T> T hget(String key, String field, Class<T> clazz) {
        byte[] bytes = redisCommands.hget(key, field);
        return decodeToObject(bytes, clazz);
    }

    @Override
    public Map<String, String> hgetall(String key) {
        Map<String, byte[]> maps = redisCommands.hgetall(key);
        Map<String, String> tomaps = new HashMap<>();
        for (String s : maps.keySet()) {
            tomaps.put(s, decodeToString(maps.get(s)));
        }
        return tomaps;
    }

    @Override
    public <T> Map<String, T> hgetall(String key, Class<T> clazz) {
        Map<String, byte[]> maps = redisCommands.hgetall(key);
        Map<String, T> tomaps = new HashMap<>();
        for (String s : maps.keySet()) {
            tomaps.put(s, decodeToObject(maps.get(s), clazz));
        }
        return tomaps;
    }

    @Override
    public <T> Boolean hset(String key, String field, T value) {
        return redisCommands.hset(key, field, toBytes(value));
    }

    @Override
    public <T> Boolean hsetnx(String key, String field, T value) {
        return redisCommands.hsetnx(key, field, toBytes(value));
    }

    @Override
    public Long hlen(String key) {
        return redisCommands.hlen(key);
    }

    @Override
    public Long hincrby(String key, String field, long incVal) {
        return redisCommands.hincrby(key, field, incVal);
    }

    @Override
    public Long llen(String key) {
        try {
            return redisCommands.llen(key);
        } catch (RedisException e) {
            e.printStackTrace();
        }
        return 0L;
    }

    @Override
    public String lget(String key, int idx) {
        try {
            return decodeToString(redisCommands.lindex(key, idx));
        } catch (RedisException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T lget(String key, int idx, Class<T> clazz) {
        try {
            return decodeToObject(redisCommands.lindex(key, idx), clazz);
        } catch (RedisException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> lrange(String key, int start, int end) {
        if (start < 0) {
            start = 0;
        }
        List<byte[]> vals = redisCommands.lrange(key, start, end);
        List<String> list = new ArrayList<>();
        for (byte[] val : vals) {
            list.add(decodeToString(val));
        }
        return list;
    }

    @Override
    public <T> List<T> lrange(String key, int start, int end, Class<T> clazz) {
        if (start < 0) {
            start = 0;
        }
        List<byte[]> vals = redisCommands.lrange(key, start, end);
        List<T> list = new ArrayList<>();
        for (byte[] val : vals) {
            list.add(decodeToObject(val, clazz));
        }
        return list;
    }

    @Override
    public <T> boolean lset(String key, long idx, T value) {
        return "OK".equalsIgnoreCase(redisCommands.lset(key, idx, toBytes(value)));
    }

    @Override
    public <T> Long llpush(String key, T... values) {
        return redisCommands.lpush(key, toArray(values));
    }

    @Override
    public <T> Long llpushx(String key, T... values) {
        return redisCommands.lpushx(key, toArray(values));
    }

    @Override
    public String llpop(String key) {
        byte[] bytes = redisCommands.lpop(key);
        return decodeToString(bytes);
    }

    @Override
    public <T> T llpop(String key, Class<T> clazz) {
        byte[] bytes = redisCommands.lpop(key);
        return decodeToObject(bytes, clazz);
    }

    @Override
    public <T> Long lrpush(String key, T... values) {
        return redisCommands.rpush(key, toArray(values));
    }

    @Override
    public <T> Long lrpushx(String key, T... values) {
        return redisCommands.rpush(key, toArray(values));
    }

    @Override
    public String lrpop(String key) {
        return decodeToString(redisCommands.rpop(key));
    }

    @Override
    public <T> Long lrem(String key, int count, T value) {
        return redisCommands.lrem(key, count, toBytes(value));
    }

    @Override
    public boolean ltrim(String key, long start, long end) {
        if (start < 0 || end < 0) {
            throw new RedisException("index should be a positive integer: " + start + " " + end);
        }
        try {
            String ret = redisCommands.ltrim(key, start, end);
            return "OK".equalsIgnoreCase(ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean ltrim(String key, int start, int end) {
        if (start < 0 || end < 0) {
            throw new RedisException("index should be a positive integer: " + start + " " + end);
        }
        try {
            String ret = redisCommands.ltrim(key, Integer.toUnsignedLong(start), Integer.toUnsignedLong(end));
            return "OK".equalsIgnoreCase(ret);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
