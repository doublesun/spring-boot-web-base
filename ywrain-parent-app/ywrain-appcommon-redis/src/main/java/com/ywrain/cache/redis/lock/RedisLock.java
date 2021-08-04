package com.ywrain.cache.redis.lock;

import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sun.corba.se.spi.ior.ObjectId;
import com.ywrain.cache.redis.service.RedisCmdService;
import com.ywrain.common.utils.IdUtil;

/**
 * 利用Redis实现的分布式锁
 * <br>不建议使用在耗时较大的业务场景，如定时统计类任务
 * <pre>
 *     默认锁的持有超时时间是2000ms
 *     默认锁的等待超时是0ms
 *     默认锁的等待轮询cd是1ms
 *     注意：
 *     利用lua-scripts（要求RedisServer版本>2.8）实现lock和unlock的原子操作
 *
 *     类型上存在两种特性:
 *     1、互斥（悲观）
 *     2、自旋（不可重入）
 *
 * </pre>
 *
 * @author xuguangming@ywrain.com
 * @Date 2018-01-15
 */
@Component
public class RedisLock {
    private Logger LOGGER = LoggerFactory.getLogger(RedisLock.class);

    @Autowired
    private RedisCmdService redisService;

    private int DEFAULT_EXPIRE_MS = 2000;
    private int DEFAULT_WAIT_MS = 0; // 500;
    private int DEFAULT_WAIT_CD = 1; // 避免没有传值，导致轮询过快

    private Integer _genmachine;

    @FunctionalInterface
    public interface IRedisLockHandler {
        void run();
    }

    private int genMachineId() {
        if (_genmachine != null) {
            return _genmachine;
        }
        int machinePiece;
        try {
            StringBuilder sb = new StringBuilder();
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            while (e.hasMoreElements()) {
                NetworkInterface ni = e.nextElement();
                sb.append(ni.toString());
            }
            machinePiece = sb.toString().hashCode() << 16;
        } catch (Throwable e) {
            // exception sometimes happens with IBM JVM, use random
            machinePiece = (new Random().nextInt()) << 16;
        }
        final int processPiece;
        {
            int processId = new java.util.Random().nextInt();
            try {
                processId = java.lang.management.ManagementFactory.getRuntimeMXBean().getName().hashCode();
            } catch (Throwable t) {
            }

            ClassLoader loader = ObjectId.class.getClassLoader();
            int loaderId = loader != null ? System.identityHashCode(loader) : 0;

            StringBuilder sb = new StringBuilder();
            sb.append(Integer.toHexString(processId));
            sb.append(Integer.toHexString(loaderId));
            processPiece = (short) sb.toString().hashCode();
        }
        _genmachine = machinePiece | processPiece;
        LOGGER.debug("[RedisLock]get machineid: {}", _genmachine);
        return _genmachine;
    }

    // 获取一个尽可能随机的值
    private String getRandomLockVal() {
        return IdUtil.getUUID();
//        StringBuilder sb = new StringBuilder();
//        sb.append(System.currentTimeMillis()).append(".").append(DEFAULT_EXPIRE_MS).append(genMachineId());
//        LOGGER.debug("[RedisLock]get lockRun random val: {}", sb.toString());
//        return sb.toString();
    }

    private boolean getMutex(String key, String val, int expireMilliseconds) {
        if (expireMilliseconds <= 0) {
            return redisService.setNx(key, val);
        } else {
            return redisService.setPxNx(key, val, expireMilliseconds);
        }
    }

    private synchronized boolean tryLock(String key, String lockVal, int expireMilliseconds, int waitMilliseconds, int waitCd)
        throws InterruptedException {
        // 先拿锁
        if (getMutex(key, lockVal, expireMilliseconds)) {
            return true;
        }
        waitMilliseconds = expireMilliseconds < waitMilliseconds ? expireMilliseconds : waitMilliseconds;
        while (waitMilliseconds > 0 && waitCd > 0) {
            if (getMutex(key, lockVal, expireMilliseconds)) {
                return true;
            } else {
                // 睡眠等待
                Thread.sleep(waitMilliseconds <= waitCd ? waitMilliseconds : waitCd);
                waitMilliseconds -= waitCd;
            }
        }
        return false;
    }

    public boolean lockRun(String lockKey, IRedisLockHandler handler) {
        String lockVal = getRandomLockVal();
        return lockRun(lockKey, lockVal, DEFAULT_EXPIRE_MS, DEFAULT_WAIT_MS, DEFAULT_WAIT_CD, handler);
    }

    public boolean lockRun(String lockKey, String lockVal, IRedisLockHandler handler) {
        return lockRun(lockKey, lockVal, DEFAULT_EXPIRE_MS, DEFAULT_WAIT_MS, DEFAULT_WAIT_CD, handler);
    }

    public boolean lockRun(String lockKey, String lockVal, int expireMilliseconds, IRedisLockHandler handler) {
        return lockRun(lockKey, lockVal, expireMilliseconds, DEFAULT_WAIT_MS, DEFAULT_WAIT_CD, handler);
    }

    public boolean lockRun(String lockKey, String lockVal, int expireMilliseconds, int waitMilliseconds, IRedisLockHandler handler) {
        return lockRun(lockKey, lockVal, expireMilliseconds, waitMilliseconds, DEFAULT_WAIT_CD, handler);
    }

    /**
     * 获取锁并执行回到任务
     * <br>通过expireMilliseconds指定过期时间，过期后获得的锁将失效
     * <pre>
     *     流程如下：
     *
     *                                        +-------------+     +---------+     +--------+     +------------+
     *   +----------------------------------> |  Lock KEY   | --> |   Run   | --> | FAILED | --> | Unlock KEY |
     *   |                                    +-------------+     +---------+     +--------+     +------------+
     *   |                                      |                   |                              ^
     *   | LOOP waitCd Limit waitMilliseconds   |                   |                              |
     *   |                                      v                   v                              |
     *   |                                    +-------------+     +---------+                      |
     *   +----------------------------------- | Lock Failed |     | SUCCESS | ---------------------+
     *                                        +-------------+     +---------+
     * </pre>
     *
     * @param handle 执行方法实现
     * @param lockKey 锁键
     * @param lockVal 锁设置值，可用于标识并校验锁的持有者，建议使用毫秒时间戳等有一定业务随机和唯一性的值，防止锁的竞争覆盖
     * @param expireMilliseconds 锁过期时间（豪秒）：0表示不过期
     * @param waitMilliseconds 等待取锁超时时间（豪秒），超过后将放弃: 0表示不等待
     * @param waitCd 锁等待取检测间隔时间（豪秒），默认1ms
     * @return TRUE-获取锁并执行成功 | FALSE
     */
    public boolean lockRun(String lockKey, String lockVal, int expireMilliseconds, int waitMilliseconds, int waitCd, IRedisLockHandler handler) {
        boolean isGetLock = false;
        if (expireMilliseconds < 0 || waitMilliseconds < 0) {
            throw new RedisLockException("[RedisLock]timeout cant be negative value ");
        }
        if (waitCd <= 0) {
            throw new RedisLockException("[RedisLock]waitCd cant be negative value ");
        }
        try {
            if (tryLock(lockKey, lockVal, expireMilliseconds, waitMilliseconds, waitCd)) {
                isGetLock = true;
                handler.run();
                unlock(lockKey, lockVal);
            }
        } catch (Exception e) {
            LOGGER.error("[RedisLock]key ({}) run exception", lockKey, e);
            // 这里直接移除要考虑有风险的情况:
            // 不在finally区块执行的原因：尽可能降低耗时对锁释放的干扰
            unlock(lockKey, lockVal);
        }
        return isGetLock;
    }

    /**
     * 释放锁
     *
     * @param lockKey 锁键
     * @param lockVal 锁设置值，可用于标识并校验锁的持有者，建议使用毫秒时间戳等有一定业务随机和唯一性的值，防止锁的竞争覆盖
     */
    public void unlock(String lockKey, String lockVal) {
        try {
            // Redis Server可能不支持EVAL执行脚本，需要重新考虑实现
            Long ret = redisService.removeStrKeyIfEquals(lockKey, lockVal);
            if (ret <= 0) {
                LOGGER.error("[RedisLock]unlock error, key: {} val: {}", lockKey, lockVal);
            }
        } catch (Exception e) {
            LOGGER.info("[RedisLock]unlock exception，KEY: {} LockVal: {} e: {}", lockKey, lockVal, e);
            throw new RedisLockException("[RedisLock] run exception", e);
        }
    }

    public static class RedisLockException extends RuntimeException {

        public RedisLockException(String msg, Exception e) {
            super(msg, e);
        }

        public RedisLockException(String msg) {
            super(msg);
        }
    }
}
