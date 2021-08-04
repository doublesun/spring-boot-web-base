### 仓库说明

WEB-API项目通用配置依赖库，基于Spring框架管理。包括：协议、缓存等

| 项目名 | 版本 | 作用 | 描述 |
| :--: | :--: | :--: | :--: |
| ywrain-parent-app | 1.2.1 | 协议封装 | 包含协议打包、错误码、常量配置等内容  |
| ywrain-appcommon-proto | 1.2.0 | 协议封装 | 包含协议打包、错误码、常量配置等内容  |
| ywrain-appcommon-webapp | 1.2.1 | Web项目通用配置封装 | 包含异常拦截、基础请求参数获取工具类ReqUtil、容器配置等内容|
| ywrain-appcommon-redis | 1.2.1 | redis配置封装库 | redis操作依赖库 |
| ywrain-appcommon-configclient | 1.2.0-SNAPSHOT | - | - |

- 说明：

1. API接口统一采用JsonString返回，默认转换器是基于Gson封装，需要项目注入Bean，项目启动类参考：

```java
import com.ywrain.appcommon.converter.AppGsonHttpMessageConverter;

@SpringBootApplication
@EnableAsync(proxyTargetClass=true)
@EnableScheduling
public class App {
    private static Logger LOGGER = LoggerFactory.getLogger(App.class);

    /* gson替换spring boot默认的jackson */
    @Bean
        public HttpMessageConverters gsonHttpMessageConverters() {
            AppGsonHttpMessageConverter gsonConverter = new AppGsonHttpMessageConverter();
            return new HttpMessageConverters(gsonConverter);
        }

    public static void main(String[] args) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        ConfigurableApplicationContext ctx = SpringApplication.run(App.class, args);

        String[] activeProfiles = ctx.getEnvironment().getActiveProfiles();
        for (String profile : activeProfiles) {
            LOGGER.info("当前使用profile为: {}", profile);
        }
    }
}
```

2. 协议通用打包工具类RespUtil，常见接口返回参考

```java
    @RequestMapping(value = "/xxx")
    public Response get(String data) {
        return RespUtil.getOk(loginResult);
    }
```

3. 异常通用处理

```java
    throw ExceptionUtil.getBizException("业务处理错误")
    ...
    throw ExceptionUtil.getParamsException(ProtoConsts.ERR_MSG_PARAM)
```

4. 内部默认定义了跨域访问权限控制，代码如下:

```java
    /**
     * 跨域访问，允许所有跳转访问
     * 
     * @return CorsFilter 跨域处理Filter
     */
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        corsConfig.addAllowedOrigin("*"); // 1
        corsConfig.addAllowedHeader("*"); // 2
        corsConfig.addAllowedMethod("*"); // 3
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", corsConfig); // 4
        return new CorsFilter(configSource);
    }
```

5. web容器默认嵌入的undertow

6. 404页面或者访问method错误，统一返回参数错误(errcode=102)

`com.ywrain.appcommon.handler.AppCustomController`

7. 默认的身份验证采用JWT-token机制，实现类:

`com.ywrain.appcommon.auth.JwtApiAuth`

### 私有配置属性

- 接口日志耗时日志阈值

```
# 配置错误耗时阈值，单位：毫秒，默认1500ms
ywrain.showlog.duration.error=1500
# 配置告警耗时阈值，单位：毫秒，默认200ms
ywrain.showlog.duration.warn=200
```

- 线程池配置

通用异步线程池是默认开启，直接使用@Async 或 @Async(value = "commonExecutor")


```
# 计划任务线程池初始数
ywrain.executor.scheduled.pool-size=3

# 通用异步线程池初始数
ywrain.executor.common.pool-size=10
# 通用异步线程池最大数量
ywrain.executor.common.pool-max-size=50
# 通用异步线程池队列容量
ywrain.executor.common.queue-capacity=100
# 通用异步线程池任务是否在结束时全部执行，默认不会在context退出时优雅关闭
ywrain.executor.common.wait-for-tasks-to-complete-on-shutdown=false
# 通用异步线程池任务等待关闭的最大延时
ywrain.executor.common.await-termination-seconds=0
```

- Redis缓存默认配置

```
## Redis服务器地址
ywrain.redis.host-name=redis-test01-gz.lc
## Redis服务器连接端口
ywrain.redis.port=6379
## Redis的密码
ywrain.redis.password=youche_ywrain_2015
## 数据库编号
ywrain.redis.database=0
## 连接池最大连接数（使用负值表示没有限制）
ywrain.redis.pool.max-active=10
## 连接池最大阻塞等待时间（使用负值表示没有限制）
ywrain.redis.pool.max-wait=-1
ywrain.redis.pool.max-total=50
## 连接池中的最大空闲连接
ywrain.redis.pool.max-idle=10
## 连接池中的最小空闲连接
ywrain.redis.pool.min-idle=5
## 连接超时时间（毫秒）
ywrain.redis.timeout=200
```


### 包路径说明

- com.ywrain.appcommon

| 包名      | 描述                      |
| :----:    | :--:                      |
| jtest     | 单元测试                  |
| consts    | 项目基础常量              |
| enums     | 项目基础枚举              |
| config    | app项目配置，包括web、线程池等   |
| proto     | 协议定义                  |
| utils     | 通用web-api项目基础工具类 |
| util      | 通用web-api项目基础工具类 |
| auth      | 授权身份校验相关          |
| exception | 异常类相关                |


