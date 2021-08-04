package com.ywrain.cache.redis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lambdaworks.redis.pubsub.RedisPubSubListener;

/**
 * 订阅回调
 *
 * @author xuguangming@ywrain.com
 * @date create in 2019/3/11
 */
public class DefaultRedisSubcribeListener implements RedisPubSubListener<String, String> {
    private Logger LOGGER = LoggerFactory.getLogger(DefaultRedisSubcribeListener.class);
    @Override
    public void message(String channel, String message) {
        LOGGER.info("收到订阅消息：{} -》{}", channel, message);
    }

    @Override
    public void message(String pattern, String channel, String message) {
        LOGGER.info("收到订阅消息：{} {} -》{}", pattern, channel, message);
    }

    @Override
    public void subscribed(String channel, long count) {
        LOGGER.info("已订阅：{} -》{}", channel, count);
    }

    @Override
    public void psubscribed(String pattern, long count) {
        LOGGER.info("已订阅pattern：{} -》{}", pattern, count);
    }

    @Override
    public void unsubscribed(String channel, long count) {
        LOGGER.info("取消订阅：{} -》{}", channel, count);
    }

    @Override
    public void punsubscribed(String pattern, long count) {
        LOGGER.info("取消订阅pattern：{} -》{}", pattern, count);
    }
}
