package com.ywrain.cache.redis.service;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import com.lambdaworks.redis.pubsub.api.sync.RedisPubSubCommands;


/**
 * Redis Publish/Subcribe
 *
 * @author xuguangming@ywrain.com
 * @date create in 2019/1/9
 */
@Component
@ConditionalOnBean(name = "RedisPubCommands")
public class RedisPubService {

    @Resource(name = "RedisPubCommands")
    private RedisPubSubCommands<String, String> redisPubCommands;

    /**
     * 发送消息到指定通道
     * <br> publish和subcribe不能同时使用
     */
    public void publish(String channel, String msg) {
        redisPubCommands.publish(channel, msg);
    }
}
