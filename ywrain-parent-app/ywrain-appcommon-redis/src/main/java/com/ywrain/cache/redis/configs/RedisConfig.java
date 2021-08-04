package com.ywrain.cache.redis.configs;

import java.nio.charset.Charset;
import java.util.Optional;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisException;
import com.lambdaworks.redis.RedisURI;
import com.lambdaworks.redis.api.StatefulRedisConnection;
import com.lambdaworks.redis.api.sync.RedisCommands;
import com.lambdaworks.redis.protocol.LettuceCharsets;
import com.lambdaworks.redis.pubsub.StatefulRedisPubSubConnection;
import com.lambdaworks.redis.pubsub.api.sync.RedisPubSubCommands;


/**
 * Redis的配置
 *
 * @author xuguangming@ywrain.com
 * @date create in 2018/10/29
 */
@Configuration
@PropertySource(value = {
    "classpath:redis-${spring.profiles.active}.properties",
    "file:redis-${spring.profiles.active}.properties",
    "file:configs/redis-${spring.profiles.active}.properties"
}, ignoreResourceNotFound = true)
public class RedisConfig {
    public static final Charset UTF8 = LettuceCharsets.UTF8;

    /**
     * 链接uri，支持Standalone(SSL) / Sentinel 等模式
     *
     * @see <a href=https://github.com/lettuce-io/lettuce-core/wiki/Redis-URI-and-connection-details>uri配置规则</a>
     */
    @Value("${ywrain.redis.uri:}")
    private String uri;
    @Value("${ywrain.redis.host-name:}")
    private String host;
    @Value("${ywrain.redis.password:}")
    private String password;
    @Value("${ywrain.redis.port:6379}")
    private Integer port;
    @Value("${ywrain.redis.database:0}")
    private Integer database;
    @Value("${ywrain.redis.autoConnect:true}")
    private Boolean autoConnect;

    @Value("${ywrain.redis.subcribe.topic-name:channel-default}")
    private String channel;

    private RedisClient redisClient;
    private StatefulRedisConnection<String, String> connection;
    private StatefulRedisConnection<String, byte[]> bytesConnection;
    private StatefulRedisPubSubConnection<String, String> subConnection;
    private StatefulRedisPubSubConnection<String, String> pubConnection;

    @Bean("DefaultRedisClient")
    public RedisClient redisClient() {
        if (uri == null || uri.isEmpty()) {
            RedisURI redisURI = new RedisURI();
            redisURI.setHost(host);
            redisURI.setPort(port);
            redisURI.setPassword(password);
            redisURI.setDatabase(database);
            redisClient = RedisClient.create(redisURI);
        } else {
            redisClient = RedisClient.create(uri);
        }
        return redisClient;
    }
//    private void initClient() {
//        if (redisClient == null) {
//            if (uri == null || uri.isEmpty()) {
//                RedisURI redisURI = new RedisURI();
//                redisURI.setHost(host);
//                redisURI.setPort(port);
//                redisURI.setPassword(password);
//                redisURI.setDatabase(database);
//                redisClient = RedisClient.create(redisURI);
//            } else {
//                redisClient = RedisClient.create(uri);
//            }
//        }
//    }

    @Bean("DefaultRedisCommands")
    public RedisCommands<String, String> redisCommands(@Qualifier("DefaultRedisClient") RedisClient redisClient) {
        connection = redisClient.connect();
        RedisCommands<String, String> redisCommands = Optional.of(connection.sync()).orElseThrow(() -> new RedisException("Redis链接初始化失败"));
        redisCommands.select(database);
        return redisCommands;
    }

    @Bean("DefaultCustomBytesRedisCommands")
    public RedisCommands<String, byte[]> customBytesRedisCommands(@Qualifier("DefaultRedisClient") RedisClient redisClient) {
        bytesConnection = redisClient.connect(CustomByteArrayCodec.INSTANCE);
        RedisCommands<String, byte[]> redisCommands = Optional.of(bytesConnection.sync()).orElseThrow(() -> new RedisException("Redis链接初始化失败"));
        redisCommands.select(database);
        return redisCommands;
    }

    @Bean("RedisSubCommands")
    @ConditionalOnProperty(value = "ywrain.redis.subcribe.enable")
    public RedisPubSubCommands<String, String> redisSubConn(@Qualifier("DefaultRedisClient") RedisClient redisClient) {
//        initClient();
        // 开启监听通道
        subConnection = redisClient.connectPubSub();
        RedisPubSubCommands<String, String> subCommands = Optional.of(subConnection.sync())
            .orElseThrow(() -> new RedisException("Redis链接初始化订阅通道失败"));
        subCommands.select(database);
        return subCommands;
    }

    @Bean("RedisPubCommands")
    @ConditionalOnProperty(value = "ywrain.redis.publish.enable")
    public RedisPubSubCommands<String, String> redisPubConn(@Qualifier("DefaultRedisClient") RedisClient redisClient) {
//        initClient();
        // 开启通道
        pubConnection = redisClient.connectPubSub();
        RedisPubSubCommands<String, String> pubCommands = Optional.of(pubConnection.sync())
            .orElseThrow(() -> new RedisException("Redis链接初始化推送通道失败"));
        pubCommands.select(database);
        return pubCommands;
    }

    @PreDestroy
    protected void destroy() {
        if (connection != null && connection.isOpen()) {
            connection.close();
        }
        if (subConnection != null && subConnection.isOpen()) {
            subConnection.close();
        }
        if (pubConnection != null && pubConnection.isOpen()) {
            pubConnection.close();
        }
        redisClient.shutdown();
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
