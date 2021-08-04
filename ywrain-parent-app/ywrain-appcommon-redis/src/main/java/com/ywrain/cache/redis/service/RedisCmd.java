package com.ywrain.cache.redis.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis的接口
 *
 * @author xuguangming@ywrain.com
 * @date create in 2019/3/14
 */
public interface RedisCmd {

    /**
     * 移除一组键
     * <br>
     * 时间复杂度：O(N)， N 为被删除的 key 的数量，其中删除单个字符串类型的 key ，时间复杂度为O(1)；删除单个列表、集合、有序集合或哈希表类型的 key ，时间复杂度为O(M)， M 为以上数据结构内的元素数量。
     *
     * @param keys 键
     * @return 移除的数量，类型：long
     */
    long remove(String... keys);

    /**
     * 移除字符串键值，如果存在且值相等
     * <br>请注意改方法针对redis集群模式可能会失效，需要重新测试
     * <br>scripts = "if cache.call('type', KEYS[1]).ok == 'string' then if cache.call('get', KEYS[1]) == ARGV[1] then return cache.call('del',
     * KEYS[1]) end end return 0";
     *
     * @param keys 键
     * @return 移除的数量，如果KEY不是字符串类型，则返回0
     */
    Long removeStrKeyIfEquals(String key, String val);

    /**
     * 设置指定键的过期时间；设置成功返回 true
     * <br>当 key 不存在或者不能为 key 设置生存时间时(比如在低于 2.1.3 版本的 Redis 中你尝试更新 key 的生存时间)，返回 false 。
     *
     * @param key 键
     * @param expire 过期秒数
     * @return true 成功 | false 失败
     */
    Boolean expire(String key, long expireCd);

    /**
     * 设置指定键的过期时间，使用Unix timestamp
     * <br>当 key 不存在或者不能为 key 设置生存时间时(比如在低于 2.1.3 版本的 Redis 中你尝试更新 key 的生存时间)，返回 false 。
     *
     * @param timestamp 过期截止时间戳 java.util.Date
     */
    Boolean expireAt(String key, Date timestamp);

    /**
     * 判断键是否存在
     *
     * @param keys 一组键
     * @return TRUE | FALSE
     */
    boolean exists(String... keys);

    /**
     * 判断一组键是否任意存在一个
     *
     * @param keys 一组键
     * @return TRUE | FALSE
     */
    boolean existsAny(String... keys);

    /**
     * 获取键的过期剩余秒数
     *
     * @param key 键
     * @return 剩余秒数，0表示键不存在或出错
     */
    long ttl(String key);

    /**
     * 获取Redis server当前系统时间戳
     * <br> 如果访问失败，返回当前系统时间戳
     *
     * @return 秒数
     */
    long unixtime();

    /**
     * 获取Redis server当前系统时间戳，毫秒数
     * <br> 如果访问失败，返回当前系统时间戳
     *
     * @return 毫秒数
     */
    long unixtimeMs();

    // -------------------------------------------------------
    // get/set 键值 操作
    // -------------------------------------------------------

    /**
     * 获取键值；键不存在时，返回null
     *
     * @param key 键
     * @return Value字符串
     */
    String get(String key);

    /**
     * 获取键值，并转换为指定类型的对象；键不存在时，返回null
     *
     * @param key 键
     * @return Value字符串
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 设置键值
     *
     * @param key 键
     * @param value 值对象，通过gson转换为String存储
     * @Return TRUE | FALSE
     */
    <T> boolean set(String key, T value);

    /**
     * 设置键值，同时设置过期时间
     *
     * @param key 键
     * @param value 值
     * @param expireSeconds 过期时间，单位：秒
     * @Return TRUE | FALSE
     */
    <T> boolean setEx(String key, T value, long expireSeconds);

    /**
     * 设置键值，同时设置过期时间(毫秒)
     *
     * @param key 键
     * @param value 值
     * @param expireMilliseconds 过期时间，单位：毫秒
     * @Return TRUE | FALSE
     */
    <T> boolean setPx(String key, T value, long expireMilliseconds);

    /**
     * 设置键值，如果键不存在且设置成功，返回TRUE，否则返回FALSE
     *
     * @param key 键
     * @param value 值
     * @return TRUE | FALSE
     */
    <T> boolean setNx(String key, T value);

    /**
     * 设置键值，如果键不存在且设置成功，返回TRUE，否则返回FALSE。设置成功后，同时添加过期时间
     * <p>SET key value [EX seconds] [PX milliseconds] [NX|XX]</p>
     *
     * @param key 键
     * @param value 值
     * @param expireSeconds 过期时间，单位：秒
     */
    <T> boolean setExNx(String key, T value, long expireSeconds);

    /**
     * 设置键值，如果键不存在且设置成功，返回TRUE，否则返回FALSE。设置成功后，同时添加过期时间
     * <p>SET key value [EX seconds] [PX milliseconds] [NX|XX]</p>
     *
     * @param key 键
     * @param value 值
     * @param expireMilliseconds 过期时间，单位：毫秒
     */
    <T> boolean setPxNx(String key, T value, long expireMilliseconds);

    /**
     * 设置键值，并返回原来的值，如果key不存在，则返回null
     *
     * @param key 键
     * @param value 值
     */
    String getSet(String key, String value);

    /**
     * 自增键值+1
     * <br>
     * 如果键不存在，默认创建该键，并初始化值0，自增后返回1
     *
     * @param key 键
     * @return 自增后的值
     */
    Long inc(String key);

    /**
     * 自增键值+N
     * <br>
     * 如果键不存在，默认创建该键，并初始化值0，自增后返回N。N可为负数，实现自减的效果
     *
     * @param key 键
     * @return 自增后的值
     */
    Long incBy(String key, long inc);

    // -------------------------------------------------------
    // Hash Table 操作
    // -------------------------------------------------------

    /**
     * 查看哈希表 key 中，给定域 field 是否存在。
     * <br>
     * 可用版本： >= 2.0.0
     * <br>
     * 时间复杂度： O(1)
     * <br>
     * 返回值： 如果哈希表含有给定域，返回 1 。 如果哈希表不含有给定域，或 key 不存在，返回 0 。
     *
     * @param key 键
     * @param field 域
     * @return TRUE 存在 | FALSE 不存在
     */
    boolean hexists(String key, String field);

    /**
     * 返回哈希表 key 中的所有域。
     * <br>
     * 可用版本： >= 2.0.0
     * <br>
     * 时间复杂度： O(N)， N 为哈希表的大小。
     * <br>
     * 返回值： 一个包含哈希表中所有域的表。 当 key 不存在时，返回一个空表。
     *
     * @param key 键
     * @return 字段列表
     */
    Set<String> hkeys(String key);

    /**
     * 返回哈希表 key 中所有域的值。
     * <br>
     * 可用版本： >= 2.0.0
     * <br>
     * 时间复杂度： O(N)， N 为哈希表的大小。
     * <br>
     * 返回值： 一个包含哈希表中所有值的表。 当 key 不存在时，返回一个空表。
     *
     * @param key 键
     * @return 列表
     */
    List<String> hvals(String key);

    /**
     * 删除哈希表 key 中的一个或多个指定域，不存在的域将被忽略。
     * <br>
     * 可用版本： >= 2.4.0
     * <br>
     * 时间复杂度: O(N)， N 为要删除的域的数量。
     * <br>
     * 返回值: 被成功移除的域的数量，不包括被忽略的域。
     *
     * @param key 键
     * @param fields 域
     * @return 成功移除的数量，不包括不存在的的域
     */
    Long hdel(String key, String... fields);

    /**
     * 返回哈希表 key 中给定域 field 的值。
     * <br>
     * 可用版本： >= 2.0.0
     * <br>
     * 时间复杂度： O(1)
     * <br>
     * 返回值： 给定域的值。 当给定域不存在或是给定 key 不存在时，返回 nil 。
     *
     * @param key 键
     * @param field 域
     * @return 字符串值， 当给定域不存在或是给定 key 不存在时，返回 null
     */
    String hget(String key, String field);

    /**
     * 返回哈希表 key 中给定域 field 的值，并转换为指定对象 参见{@link #hget(String, String)}
     *
     * @param key 键
     * @param field 域
     * @return 字符串值， 当给定域不存在或是给定 key 不存在时，返回 null
     */
    <T> T hget(String key, String field, Class<T> clazz);

    /**
     * 返回哈希表 key 中，所有的域和值映射表。
     * <br>
     * 在返回值里，紧跟每个域名(field name)之后是域的值(value)，所以返回值的长度是哈希表大小的两倍。
     * <br>
     * 可用版本： >= 2.0.0
     * <br>
     * 时间复杂度： O(N)， N 为哈希表的大小。
     * <br>
     * 返回值： 以列表形式返回哈希表的域和域的值。 若 key 不存在，返回空列表。
     *
     * @param key 键
     * @return 返回MAP，若 key 不存在，返回空集合
     */
    Map<String, String> hgetall(String key);

    /**
     * 将哈希表 key 中的域 field 的值设为 value 。
     * <br>
     * 如果 key 不存在，一个新的哈希表被创建并进行 HSET 操作。
     * <br>
     * 如果域 field 已经存在于哈希表中，旧值将被覆盖。
     * <br>
     * 可用版本： >= 2.0.0
     * <br>
     * 时间复杂度： O(1)
     * <br>
     * 返回值： 如果 field 是哈希表中的一个新建域，并且值设置成功，返回 TRUE 。 如果哈希表中域 field 已经存在且旧值已被新值覆盖，返回 FALSE 。
     *
     * @param key 键
     * @param field 域
     * @param value 值
     * @return TRUE | FALSE
     */
    <T> Boolean hset(String key, String field, T value);

    /**
     * 将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在。
     * <br>
     * 若域 field 已经存在，该操作无效。
     * <br>
     * 如果 key 不存在，一个新哈希表被创建并执行 HSETNX 命令。
     * <br>
     * 可用版本： >= 2.0.0
     * <br>
     * 时间复杂度： O(1)
     * <br>
     * 返回值： 设置成功，返回 TRUE 。 如果给定域已经存在且没有操作被执行，返回 FALSE 。
     *
     * @param key 键
     * @param field 域
     * @param value 值
     * @param <T> 值对象指定类型
     * @return TRUE | FALSE
     */
    <T> Boolean hsetnx(String key, String field, T value);


    /**
     * 返回哈希表 key 中域的数量。
     * <br>
     * 时间复杂度： O(1)
     * <br>
     * 返回值： 哈希表中域的数量。 当 key 不存在时，返回 0 。
     *
     * @param key 键
     * @return 哈希表中域的数量。
     */
    Long hlen(String key);

    /**
     * 为哈希表 key 中的域 field 的值加上增量 increment 。
     * <br>
     * 增量也可以为负数，相当于对给定域进行减法操作。
     * <br>
     * 如果 key 不存在，一个新的哈希表被创建并执行 HINCRBY 命令。
     * <br>
     * 如果域 field 不存在，那么在执行命令前，域的值被初始化为 0 。
     * <br>
     * 对一个储存字符串值的域 field 执行 HINCRBY 命令将造成一个错误。
     * <br>
     * 本操作的值被限制在 64 位(bit)有符号数字表示之内。
     * <br>
     * 可用版本： >= 2.0.0
     * <br>
     * 时间复杂度： O(1)
     * <br>
     * 返回值： 执行 HINCRBY 命令之后，哈希表 key 中域 field 的值。
     *
     * @param key 键
     * @param field 字段
     * @param incVal 增量
     * @return 执行 HINCRBY 命令之后，哈希表 key 中域 field 的值。
     */
    Long hincrby(String key, String field, long incVal);

    /**
     * 为哈希表 key 中插入一组阈映射键值
     * <br>
     * 如果 key 不存在，那么新建一个 key，并新增指定的阈；如果 key 已存在但是类型不一致，则抛出异常RedisCommandExecutionException
     * <br>
     * 时间复杂度：O(N)， N 为给定域的数量。
     *
     * @param key 哈希表 key
     * @param maps 映射键值
     * @return 全部成功 TRUE | FALSE
     */
    boolean hmset(String key, Map<String, String> maps);

    /**
     * 返回哈希表 key 中，指定的域和值映射表。
     * <br>
     * 如果 key 已存在但是类型不一致，则抛出异常RedisCommandExecutionException
     *
     * <br>
     * 可用版本： >= 2.0.0
     * <br>
     * 时间复杂度： O(N)， N 为哈希表的大小。
     * <br>
     * 返回值： 以列表形式返回哈希表的域和域的值。 若 key 不存在，返回空列表。
     *
     * @param key 键
     * @return 返回MAP，若 key 不存在，返回空集合
     */
    Map<String, String> hmget(String key, String... fields);

    // -------------------------------------------------------
    // List 数组操作
    // -------------------------------------------------------

    /**
     * 返回列表 key 的长度。
     * <br>
     * 如果 key 不存在，则 key 被解释为一个空列表，返回 0 .
     * <br>
     * 如果 key 不是列表类型，返回一个错误。
     * <br>
     * 可用版本： >= 1.0.0
     * <br>
     * 时间复杂度： O(1)
     * <br>
     * 返回值： 列表 key 的长度。
     *
     * @param key 键
     * @return 列表长度
     */
    Long llen(String key);

    /**
     * 返回列表 key 中，下标为 index 的元素。
     * <br>
     * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
     * <br>
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * <br>
     * 如果 key 不是列表类型，返回一个错误。
     * <br>
     * 可用版本： >= 1.0.0
     * <br>
     * 时间复杂度： O(N)， N 为到达下标 index 过程中经过的元素数量。 因此，对列表的头元素和尾元素执行 LINDEX 命令，复杂度为O(1)。
     * <br>
     * 返回值: 列表中下标为 index 的元素。 如果 index 参数的值不在列表的区间范围内(out of range)，返回 null 。
     *
     * @param key 键
     * @param idx 下标
     * @return 元素值
     */
    String lget(String key, int idx);

    /**
     * 返回列表 key 中，下标为 index 的元素，并转换指定类型的对象 {@link #lget(String, int)}
     *
     * @param key 键
     * @param idx 下标
     * @return 列表中下标为 index 的元素转换对象
     */
    <T> T lget(String key, int idx, Class<T> clazz);


    /**
     * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。
     * <br>
     * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
     * <br>
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。start允许为负数同时换换为0。
     * <br>
     * 注意LRANGE命令和编程语言区间函数的区别
     * <br>
     * 假如你有一个包含一百个元素的列表，对该列表执行 LRANGE list 0 10 ，结果是一个包含11个元素的列表，这表明 stop 下标也在 LRANGE 命令的取值范围之内(闭区间)，这和某些语言的区间函数可能不一致，比如Ruby的 Range.new 、 Array#slice
     * 和Python的 range() 函数。
     * <br>
     * 超出范围的下标
     * <br>
     * 超出范围的下标值不会引起错误。
     * <br>
     * 如果 start 下标比列表的最大下标 end ( LLEN list 减去 1 )还要大，那么 LRANGE 返回一个空列表。
     * <br>
     * 如果 stop 下标比 end 下标还要大，Redis将 stop 的值设置为 end 。
     * <br>
     * 可用版本： >= 1.0.0
     * <br>
     * 时间复杂度: O(S+N)， S 为偏移量 start ， N 为指定区间内元素的数量。
     *
     * @param key 键
     * @param start 起始下标
     * @param end 结束下标
     * @return 一个列表，包含指定区间内的元素。
     */
    List<String> lrange(String key, int start, int end);

    /**
     * 返回列表 key 中指定区间内的元素，区间以偏移量 start 和 stop 指定。并转换为指定类型的对象列表
     * <br>
     * 参见 {@link #lrange(String, int, int)}
     *
     * @param key 键
     * @param start 起始下标
     * @param end 结束下标
     * @param clazz 转换类
     * @param <T> 转换类型
     * @return 对象列表，包含指定区间内的元素。
     */
    <T> List<T> lrange(String key, int start, int end, Class<T> clazz);

    /**
     * 将列表 key 下标为 index 的元素的值设置为 value 。
     * <br>
     * 当 index 参数超出范围，或对一个空列表( key 不存在)进行 LSET 时，返回一个错误。
     * <br>
     * 关于列表下标的更多信息，请参考 LINDEX 命令。
     * <br>
     * 可用版本： >= 1.0.0
     * <br>
     * 时间复杂度： 对头元素或尾元素进行 LSET 操作，复杂度为 O(1)。 其他情况下，为 O(N)， N 为列表的长度。。
     *
     * @param key 键
     * @param idx 下标
     * @param value 设置的值
     * @return TRUE | FALSE
     */
    <T> boolean lset(String key, long idx, T value);

    /**
     * 将一个或多个值 value 插入到列表 key 的表头
     * <br>
     * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表头： 比如说，对空列表 mylist 执行命令 LPUSH mylist a b c ，列表的值将是 c b a ，这等同于原子性地执行 LPUSH mylist a 、 LPUSH mylist b 和
     * LPUSH mylist c 三个命令。
     * <br>
     * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
     * <br>
     * 可用版本： >= 1.0.0
     * <br>
     * 时间复杂度： O(1)
     *
     * @param key 键
     * @param values 设置的值
     * @return 执行 LPUSH 命令后，列表的长度。KEY不存在时，返回0
     */
    <T> Long llpush(String key, List<T> values);

    /**
     * 将一个或多个值 value 插入到列表 key 的表头
     * <br>
     * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表头： 比如说，对空列表 mylist 执行命令 LPUSH mylist a b c ，列表的值将是 c b a ，这等同于原子性地执行 LPUSH mylist a 、 LPUSH mylist b 和
     * LPUSH mylist c 三个命令。
     * <br>
     * 如果 key 不存在，一个空列表会被创建并执行 LPUSH 操作。
     * <br>
     * 可用版本： >= 1.0.0
     * <br>
     * 时间复杂度： O(1)
     *
     * @param key 键
     * @param values 设置的值
     * @return 执行 LPUSH 命令后，列表的长度。KEY不存在时，返回0
     */
    <T> Long llpush(String key, T... values);

    /**
     * 将一个或一组值 value 插入到列表 key 的表头，当且仅当 key 存在并且是一个列表。
     * <br>
     * 和 LPUSH 命令相反，当 key 不存在时， LPUSHX 命令什么也不做。
     * <br>
     * 可用版本： >= 2.2.0
     * <br>
     * 时间复杂度： O(1)
     *
     * @param key 键
     * @param values 设置的值
     * @return 执行 LPUSHX 命令后，列表的长度。
     */
    <T> Long llpushx(String key, List<T> values);

    /**
     * 将一个或一组值 value 插入到列表 key 的表头，当且仅当 key 存在并且是一个列表。
     * <br>
     * 和 LPUSH 命令相反，当 key 不存在时， LPUSHX 命令什么也不做。
     * <br>
     * 可用版本： >= 2.2.0
     * <br>
     * 时间复杂度： O(1)
     *
     * @param key 键
     * @param values 设置的值
     * @return 执行 LPUSHX 命令后，列表的长度。
     */
    <T> Long llpushx(String key, T... values);

    /**
     * 移除并返回列表 key 的头元素。
     * <br>
     * 可用版本： >= 1.0.0
     * <br>
     * 时间复杂度： O(1)
     *
     * @param key 键
     * @return 列表的头元素，当 key 不存在时，返回 null 。
     */
    String llpop(String key);

    /**
     * 将一个或多个值 value 插入到列表 key 的表尾(最右边)。
     * <br>
     * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表尾：比如对一个空列表 mylist 执行 RPUSH mylist a b c ，得出的结果列表为 a b c ，等同于执行命令 RPUSH mylist a 、 RPUSH mylist b 、
     * RPUSH mylist c 。
     * <br>
     * 如果 key 不存在，一个空列表会被创建并执行 RPUSH 操作。
     *
     * @param key 键
     * @param values 设置的值
     * @return 执行 RPUSH 命令后，列表的长度。KEY不存在时，返回0
     */
    <T> Long lrpush(String key, List<T> values);

    /**
     * 将一个或多个值 value 插入到列表 key 的表尾(最右边)。
     * <br>
     * 如果有多个 value 值，那么各个 value 值按从左到右的顺序依次插入到表尾：比如对一个空列表 mylist 执行 RPUSH mylist a b c ，得出的结果列表为 a b c ，等同于执行命令 RPUSH mylist a 、 RPUSH mylist b 、
     * RPUSH mylist c 。
     * <br>
     * 如果 key 不存在，一个空列表会被创建并执行 RPUSH 操作。
     *
     * @param key 键
     * @param values 设置的值
     * @return 执行 RPUSH 命令后，列表的长度。KEY不存在时，返回0
     */
    <T> Long lrpush(String key, T... values);

    /**
     * 将一个或多个值 value 插入到列表 key 的表尾，当且仅当 key 存在并且是一个列表。
     * <br>
     * 和 RPUSH 命令相反，当 key 不存在时， RPUSHX 命令什么也不做。
     * <br>
     * 可用版本： >= 2.2.0
     * <br>
     * 时间复杂度： O(1)
     *
     * @param key 键
     * @param values 设置的值
     * @return 执行 RPUSHX 命令后，列表的长度。
     */
    <T> Long lrpushx(String key, List<T> values);

    /**
     * 将一个或多个值 value 插入到列表 key 的表尾，当且仅当 key 存在并且是一个列表。
     * <br>
     * 和 RPUSH 命令相反，当 key 不存在时， RPUSHX 命令什么也不做。
     * <br>
     * 可用版本： >= 2.2.0
     * <br>
     * 时间复杂度： O(1)
     *
     * @param key 键
     * @param values 设置的值
     * @return 执行 RPUSHX 命令后，列表的长度。
     */
    <T> Long lrpushx(String key, T... values);

    /**
     * 移除并返回列表 key 的尾元素。
     * <br>
     * 可用版本： >= 1.0.0
     * <br>
     * 时间复杂度： O(1)
     *
     * @param key 键
     * @return 列表的尾元素，当 key 不存在时，返回 null 。
     */
    String lrpop(String key);

    /**
     * 根据参数 count 的值，移除列表中与参数 value 相等的元素。
     * <br>
     * count 的值可以是以下几种：
     * <pre>
     * count > 0 : 从表头开始向表尾搜索，移除与 value 相等的元素，数量为 count 。
     * count < 0 : 从表尾开始向表头搜索，移除与 value 相等的元素，数量为 count 的绝对值。
     * count = 0 : 移除表中所有与 value 相等的值。
     * </pre>
     * 可用版本： >= 1.0.0
     * <br>
     * 时间复杂度： O(N)， N 为列表的长度。
     *
     * @param key 键
     * @param count 数量
     * @param value 元素值
     * @return 被移除元素的数量。 因为不存在的 key 被视作空表(empty list)，所以当 key 不存在时， LREM 命令总是返回 0 。
     */
    <T> Long lrem(String key, int count, T value);

    /**
     * 对一个列表进行修剪(trim)，就是说，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。
     * <br>
     * 举个例子，执行命令 LTRIM list 0 2 ，表示只保留列表 list 的前三个元素，其余元素全部删除。
     * <br>
     * 下标(index)参数 start 和 stop 都以 0 为底，也就是说，以 0 表示列表的第一个元素，以 1 表示列表的第二个元素，以此类推。
     * <br>
     * 你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
     * <br>
     * 超出范围的下标值不会引起错误。
     * <br>
     * 如果 start 下标比列表的最大下标 end ( LLEN list 减去 1 )还要大，或者 start > stop ， LTRIM 返回一个空列表(因为 LTRIM 已经将整个列表清空)。
     * <br>
     * 如果 stop 下标比 end 下标还要大，Redis将 stop 的值设置为 end 。
     * <br>
     * 可用版本： >= 1.0.0
     * <br>
     * 时间复杂度: O(N)， N 为被移除的元素的数量。
     *
     * @param key 键
     * @param start 起始下标
     * @param end 结束下标
     * @return TRUE | FALSE
     */
    boolean ltrim(String key, long start, long end);

    boolean ltrim(String key, int start, int end);

    // -------------------------------------------------------
    // Set 集合操作
    // -------------------------------------------------------

    /**
     * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。
     * <br>
     * 假如 key 不存在，则创建一个只包含 member 元素作成员的集合。
     * <br>
     * 可用版本： >= 1.0.0
     * <br>
     * 时间复杂度: O(N)， N 是被添加的元素的数量。
     *
     * @param key 键
     * @param values String元素列表
     * @return 被添加到集合中的新元素的数量，不包括被忽略的元素。当 key 不是集合类型时，返回0
     */
    <T> Long sadd(String key, T... values);

    /**
     * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。
     * <br>
     * 假如 key 不存在，则创建一个只包含 member 元素作成员的集合。
     * <br>
     * 可用版本： >= 1.0.0
     * <br>
     * 时间复杂度: O(N)， N 是被添加的元素的数量。
     *
     * @param key 键
     * @param values String元素列表
     * @return 被添加到集合中的新元素的数量，不包括被忽略的元素。当 key 不是集合类型时，返回0
     */
    <T> Long sadd(String key, List<T> values);

    /**
     * 返回集合 key 的基数(集合中元素的数量)。
     * <br>
     * 可用版本： >= 1.0.0
     * <br>
     * 时间复杂度: O(1)
     *
     * @param key 键
     * @return 集合的基数。 当 key 不存在时，返回 0 。
     */
    Long slen(String key);

    /**
     * 返回集合 key 中的所有成员。
     * <br>
     * 不存在的 key 被视为空集合。
     * <br>
     * 可用版本： >= 1.0.0
     * <br>
     * 时间复杂度: O(N)， N 为集合的基数。
     *
     * @param key 键
     * @return 集合中的所有成员
     */
    Set<String> smembers(String key);

    /**
     * 判断 member 元素是否集合 key 的成员。
     * <br>
     * 可用版本： >= 1.0.0
     * <br>
     * 时间复杂度: O(1)
     *
     * @param key 键
     * @return 如果 member 元素是集合的成员，返回 TRUE 。 如果 member 元素不是集合的成员，或 key 不存在，返回 FALSE 。
     */
    Boolean sismember(String key, String value);

    /**
     * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。
     * <br>
     * 当 key 不是集合类型，返回一个错误。
     * <br>
     * 可用版本： >= 1.0.0
     * <br>
     * 时间复杂度: O(N)， N 为给定 member 元素的数量。
     * <br>
     * 返回值: 被成功移除的元素的数量，不包括被忽略的元素。
     *
     * @param key 键
     * @param fields 字段集合
     * @return 移除数量
     */
    Long sremove(String key, List<String> fields);

    /**
     * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。
     * <br>
     * 当 key 不是集合类型，返回一个错误。
     * <br>
     * 可用版本： >= 1.0.0
     * <br>
     * 时间复杂度: O(N)， N 为给定 member 元素的数量。
     * <br>
     * 返回值: 被成功移除的元素的数量，不包括被忽略的元素。
     *
     * @param key 键
     * @param fields 字段数组
     * @return 移除数量
     */
    Long sremove(String key, String... fields);

    // -------------------------------------------------------
    // 有序集合sorted set 操作
    // -------------------------------------------------------

    /**
     * 返回有序集 key 的基数。
     * <br>
     * 可用版本： >= 1.2.0
     * <br>
     * 时间复杂度: O(1)
     *
     * @param key 键
     * @return 当 key 存在且是有序集类型时，返回有序集的基数。 当 key 不存在时，返回 0 。
     */
    Long zlen(String key);

    /**
     * 返回有序集 key 中， score 值在 min 和 max 之间(默认包括 score 值等于 min 或 max )的成员的数量。若key不存在，返回0
     * <br>
     * 时间复杂度: O(log(N))， N 为有序集的基数。
     *
     * @param min 区间分数最小值 >= min
     * @param max 区间分数最大值 <= max
     * @return score 值在 min 和 max 之间的成员的数量。
     */
    Long zcount(String key, int min, int max);

    /**
     * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中。
     * <br>
     * 如果某个 member 已经是有序集的成员，那么更新这个 member 的 score 值，并通过重新插入这个 member 元素，来保证该 member 在正确的位置上。
     * <br>
     * score 值可以是整数值或双精度浮点数。
     * <br>
     * 如果 key 不存在，则创建一个空的有序集并执行 ZADD 操作。
     * <br>
     * 时间复杂度: O(M*log(N))， N 是有序集的基数， M 为成功添加的新成员的数量。
     *
     * @param key 键
     * @param value 元素
     * @param score long分数值
     * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
     */
    Long zadd(String key, String value, long score);

    /**
     * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中。
     * <br>
     * 如果某个 member 已经是有序集的成员，那么更新这个 member 的 score 值，并通过重新插入这个 member 元素，来保证该 member 在正确的位置上。
     * <br>
     * score 值可以是整数值或双精度浮点数。
     * <br>
     * 如果 key 不存在，则创建一个空的有序集并执行 ZADD 操作。
     * <br>
     * 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
     *
     * @param key 键
     * @param value 元素
     * @param score double分数值
     * @return 被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。
     */
    Long zadd(String key, String value, double score);

    /**
     * 为有序集 key 的成员 member 的 score 值加上增量 increment 。
     * <br>
     * 可以通过传递一个负数值 increment ，让 score 减去相应的值，比如 ZINCRBY key -5 member ，就是让 member 的 score 值减去 5 。
     * <br>
     * 当 key 不存在，或 member 不是 key 的成员时， ZINCRBY key increment member 等同于 ZADD key increment member 。
     * <br>
     * score 值可以是整数值或双精度浮点数。
     * <br>
     * 可用版本： >= 1.2.0
     * <br>
     * 时间复杂度: O(log(N))
     *
     * @return member 成员的新 score 值，当 key 不是有序集类型时，返回null。
     */
    Double zinc(String key, String value, int incVal);

    /**
     * 返回有序集 key 中，指定区间内的成员。 其中成员的位置按 score 值递增(从小到大)来排序。
     * <br>
     * 具有相同 score 值的成员按字典序(lexicographical order )来排列。
     * <br>
     * 如果你需要成员按 score 值递减(从大到小)来排列，请使用 ZREVRANGE 命令。
     * <br>
     * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
     * <br>
     * 超出范围的下标并不会引起错误。 比如说，当 start 的值比有序集的最大下标还要大，或是 start > stop 时， ZRANGE 命令只是简单地返回一个空列表。 另一方面，假如 stop 参数的值比有序集的最大下标还要大，那么 Redis 将 stop 当作最大下标来处理。
     * <br>
     * 可以通过使用 WITHSCORES 选项，来让成员和它的 score 值一并返回，返回列表以 value1,score1, ..., valueN,scoreN 的格式表示。 客户端库可能会返回一些更复杂的数据类型，比如数组、元组等。
     * <br>
     * 时间复杂度: O(log(N)+M)， N 为有序集的基数，而 M 为结果集的基数。
     *
     * @param key 键
     * @param start 起始位置
     * @param end 终止位置
     * @param isDesc 是否按score降序排列
     * @return 指定区间内的有序集成员的列表。
     */
    List<String> zrange(String key, long start, long end);

    /**
     * 返回有序集 key 中，指定区间内的成员。 其中成员的位置按 score 值递增(从小到大)来排序。
     * <br>
     * 具有相同 score 值的成员按字典序(lexicographical order )来排列。
     * <br>
     * 如果你需要成员按 score 值递减(从大到小)来排列，请使用 ZREVRANGE 命令。
     * <br>
     * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
     * <br>
     * 超出范围的下标并不会引起错误。 比如说，当 start 的值比有序集的最大下标还要大，或是 start > stop 时， ZRANGE 命令只是简单地返回一个空列表。 另一方面，假如 stop 参数的值比有序集的最大下标还要大，那么 Redis 将 stop 当作最大下标来处理。
     * <br>
     * 可以通过使用 WITHSCORES 选项，来让成员和它的 score 值一并返回，返回列表以 value1,score1, ..., valueN,scoreN 的格式表示。 客户端库可能会返回一些更复杂的数据类型，比如数组、元组等。
     * <br>
     * 时间复杂度: O(log(N)+M)， N 为有序集的基数，而 M 为结果集的基数。
     *
     * @param key 键
     * @param start 起始位置
     * @param end 终止位置
     * @param isDesc 是否按score降序排列
     * @return 指定区间内的有序集成员的列表。
     */
    List<String> zrange(String key, long start, long end, boolean isDesc);

    /**
     * 返回有序集 key 中，指定区间内的成员。其中成员的位置按 score 值递增(从小到大)来排序。
     * <br>
     * 具有相同 score 值的成员按字典序(lexicographical order )来排列。
     * <br>
     * 如果你需要成员按 score 值递减(从大到小)来排列，请使用 ZREVRANGE 命令。
     * <br>
     * 下标参数 start 和 stop 都以 0 为底，也就是说，以 0 表示有序集第一个成员，以 1 表示有序集第二个成员，以此类推。 你也可以使用负数下标，以 -1 表示最后一个成员， -2 表示倒数第二个成员，以此类推。
     * <br>
     * 超出范围的下标并不会引起错误。 比如说，当 start 的值比有序集的最大下标还要大，或是 start > stop 时， ZRANGE 命令只是简单地返回一个空列表。 另一方面，假如 stop 参数的值比有序集的最大下标还要大，那么 Redis 将 stop 当作最大下标来处理。
     * <br>
     * 可以通过使用 WITHSCORES 选项，来让成员和它的 score 值一并返回，返回列表以 value1,score1, ..., valueN,scoreN 的格式表示。 客户端库可能会返回一些更复杂的数据类型，比如数组、元组等。
     * <br>
     * 时间复杂度: O(log(N)+M)， N 为有序集的基数，而 M 为结果集的基数。
     *
     * @param key 键
     * @param start 起始位置
     * @param end 终止位置
     * @param isDesc 是否按score降序排列
     * @return 指定区间内的带有 score 值的有序集成员的MAP映射表
     */
    Map<String, Double> zrangeWithScores(String key, long start, long end, boolean isDesc);

    /**
     * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score 值递增(从小到大)次序排列。
     * <br>
     * 具有相同 score 值的成员按字典序(lexicographical order)来排列(该属性是有序集提供的，不需要额外的计算)。
     * <br>
     * 可选的 LIMIT 参数指定返回结果的数量及区间(就像SQL中的 SELECT LIMIT offset, count )，注意当 offset 很大时，定位 offset 的操作可能需要遍历整个有序集，此过程最坏复杂度为 O(N) 时间。
     * <br>
     * 可选的 WITHSCORES 参数决定结果集是单单返回有序集的成员，还是将有序集成员及其 score 值一起返回。 该选项自 Redis 2.0 版本起可用。 区间及无限
     * <br>
     * min 和 max 可以是 -inf 和 +inf ，这样一来，你就可以在不知道有序集的最低和最高 score 值的情况下，使用 ZRANGEBYSCORE 这类命令。
     * <br>
     * 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)，你也可以通过给参数前增加 ( 符号来使用可选的开区间 (小于或大于)。
     * <br>
     * 举个例子：
     * <br>
     * ZRANGEBYSCORE zset (1 5 返回所有符合条件 1 < score <= 5 的成员，而
     * <br>
     * ZRANGEBYSCORE zset (5 (10 则返回所有符合条件 5 < score < 10 的成员。
     * <br>
     * 时间复杂度: O(log(N)+M)， N 为有序集的基数， M 为被结果集的基数。
     *
     * @param key 键
     * @param min score的最小值
     * @param max score的最大值
     * @return 指定区间内的列表。
     */
    List<String> zrangeByScore(String key, long min, long max);

    /**
     * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score 值指定的排序规则排列。
     * <br>
     * 具有相同 score 值的成员按字典序(lexicographical order)来排列(该属性是有序集提供的，不需要额外的计算)。
     * <br>
     * 可选的 LIMIT 参数指定返回结果的数量及区间(就像SQL中的 SELECT LIMIT offset, count )，注意当 offset 很大时，定位 offset 的操作可能需要遍历整个有序集，此过程最坏复杂度为 O(N) 时间。
     * <br>
     * 可选的 WITHSCORES 参数决定结果集是单单返回有序集的成员，还是将有序集成员及其 score 值一起返回。 该选项自 Redis 2.0 版本起可用。 区间及无限
     * <br>
     * min 和 max 可以是 -inf 和 +inf ，这样一来，你就可以在不知道有序集的最低和最高 score 值的情况下，使用 ZRANGEBYSCORE 这类命令。
     * <br>
     * 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)，你也可以通过给参数前增加 ( 符号来使用可选的开区间 (小于或大于)。
     * <br>
     * 举个例子：
     * <br>
     * ZRANGEBYSCORE zset (1 5 返回所有符合条件 1 < score <= 5 的成员，而
     * <br>
     * ZRANGEBYSCORE zset (5 (10 则返回所有符合条件 5 < score < 10 的成员。
     * <br>
     * 时间复杂度: O(log(N)+M)， N 为有序集的基数， M 为被结果集的基数。
     *
     * @param key 键
     * @param min score的最小值
     * @param max score的最大值
     * @param isDesc 是否按score降序排列
     * @return 指定区间内成员的列表。
     */
    List<String> zrangeByScore(String key, long min, long max, boolean isDesc);

    /**
     * h 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score 值指定的排序规则排列。
     * <br>
     * 具有相同 score 值的成员按字典序(lexicographical order)来排列(该属性是有序集提供的，不需要额外的计算)。
     * <br>
     * 可选的 LIMIT 参数指定返回结果的数量及区间(就像SQL中的 SELECT LIMIT offset, count )，注意当 offset 很大时，定位 offset 的操作可能需要遍历整个有序集，此过程最坏复杂度为 O(N) 时间。
     * <br>
     * 可选的 WITHSCORES 参数决定结果集是单单返回有序集的成员，还是将有序集成员及其 score 值一起返回。 该选项自 Redis 2.0 版本起可用。 区间及无限
     * <br>
     * min 和 max 可以是 -inf 和 +inf ，这样一来，你就可以在不知道有序集的最低和最高 score 值的情况下，使用 ZRANGEBYSCORE 这类命令。
     * <br>
     * 默认情况下，区间的取值使用闭区间 (小于等于或大于等于)，你也可以通过给参数前增加 ( 符号来使用可选的开区间 (小于或大于)。
     * <br>
     * 举个例子：
     * <br>
     * ZRANGEBYSCORE zset (1 5 返回所有符合条件 1 < score <= 5 的成员，而
     * <br>
     * ZRANGEBYSCORE zset (5 (10 则返回所有符合条件 5 < score < 10 的成员。
     * <br>
     * 时间复杂度: O(log(N)+M)， N 为有序集的基数， M 为被结果集的基数。
     *
     * @param key 键
     * @param min score的最小值
     * @param max score的最大值
     * @param isDesc 是否按score降序排列
     * @return 指定区间内，带有 score 值(可选)的有序集成员的列表。
     */
    Map<String, Double> zrangeByScoreWithScores(String key, long min, long max, boolean isDesc);

    /**
     * 返回有序集 key 中成员 member 的排名。其中有序集成员按 score 值递增(从小到大)顺序排列。
     * <br>
     * 排名以 0 为底，也就是说， score 值最小的成员排名为 0 。
     * <br>
     * 使用 ZREVRANK 命令可以获得成员按 score 值递减(从大到小)排列的排名。
     * <br>
     * 可用版本： >= 2.0.0
     * <br>
     * 时间复杂度: O(log(N))
     *
     * @param key 键
     * @return 如果 member 是有序集 key 的成员，返回 member 的排名。 如果 member 不是有序集 key 的成员，返回 0。
     */
    long zrank(String key, String member);

    /**
     * 返回有序集 key 中成员 member 的排名。指定排序规则。
     * <br>
     * 排名以 0 为底，也就是说， score 值最小的成员排名为 0 。
     * <br>
     * 使用 ZREVRANK 命令可以获得成员按 score 值递减(从大到小)排列的排名。
     * <br>
     * 可用版本： >= 2.0.0
     * <br>
     * 时间复杂度: O(log(N))
     *
     * @param key 键
     * @return 如果 member 是有序集 key 的成员，返回 member 的排名。 如果 member 不是有序集 key 的成员，返回 0。
     */
    long zrank(String key, String member, boolean isDesc);

    /**
     * 移除有序集 key 中的一个或多个成员，不存在的成员将被忽略。
     *
     * @param key 键
     * @param members 移除的成员数组
     * @return 被成功移除的成员的数量，不包括被忽略的成员。
     */
    Long zremove(String key, String... members);

    /**
     * 返回有序集 key 中，成员 member 的 score 值。
     *
     * @param key 键
     * @param member 移除的成员
     * @return member 成员的 score 值，以字符串形式表示。
     */
    Double zscore(String key, String member);

}