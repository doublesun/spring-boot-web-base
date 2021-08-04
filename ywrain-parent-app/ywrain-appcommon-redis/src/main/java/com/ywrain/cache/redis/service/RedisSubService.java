package com.ywrain.cache.redis.service;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import com.lambdaworks.redis.pubsub.RedisPubSubListener;
import com.lambdaworks.redis.pubsub.api.sync.RedisPubSubCommands;


/**
 * Redis Publish/Subcribe
 *
 * @author xuguangming@ywrain.com
 * @date create in 2019/1/9
 */
@Component
@ConditionalOnBean(name = "RedisSubCommands")
public class RedisSubService {

    @Resource(name = "RedisSubCommands")
    private RedisPubSubCommands<String, String> redisSubCommands;

    /**
     * 订阅指定通道，并添加监听器
     */
    public void subcribe(String channel) {
        subcribe(channel, new DefaultRedisSubcribeListener());
    }

    /**
     * 订阅指定通道，并添加监听器
     * <br> publish和subcribe不能同时使用
     */
    public void subcribe(String channel, RedisPubSubListener<String, String> listener) {
        redisSubCommands.getStatefulConnection().addListener(listener);
        redisSubCommands.subscribe(channel);
    }
}
