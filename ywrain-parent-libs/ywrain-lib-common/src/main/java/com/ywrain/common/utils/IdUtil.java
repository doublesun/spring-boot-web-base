package com.ywrain.common.utils;

import java.util.UUID;

import com.ywrain.common.support.CommonException;
import com.ywrain.common.support.ObjectId;
import com.ywrain.common.support.SnowflakeIdWorker;

/**
 * ID生成工具类
 *
 * <pre>
 *     包括UUID、Snakeflake、ObjectId(MongoDB)三种生成算法，方法如下：
 *     1. UUID: {@link #getRandomUUID()}
 *     2. Snakeflake: {@link #getGlobalUniqueId()}
 *     3. MongoDB ObjectId: {@link #getGlobalUniqueIdWithString()}
 * </pre>
 *
 * @author weipengfei@youcheyihou.com
 */
public class IdUtil {
    public static SnowflakeIdWorker _idWorker = null;
    public static final int DEFAULT_MACHINE_ID = ObjectId.getMachinePiece() & 0xFFF;

    /**
     * 获取随机UUID
     *
     * @return 随机UUID字符串
     */
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getRandomUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取随机简化的UUID
     *
     * @return 简化UUID字符串，去掉“-”
     */
    public static String getSimpleUUID() {
        String uuid = UUID.randomUUID().toString();
        return StringUtil.removeAll(uuid, "-");
    }

    /**
     * 初始化全局唯一整型ID生成器
     * <pre>
     *     注意：必须要
     * </pre>
     *
     * @param machineId 初始机器ID，能区分不同机器节点的数字(范围：1~4096)
     * @throws IllegalArgumentException 参数异常
     */
    public synchronized static void init(int machineId) {
//        if (_idWorker == null) {
//        }
        if (machineId <= 0) {
            throw new CommonException("IdUtil _idWorker Args not init");
        }
        _idWorker = new SnowflakeIdWorker(machineId);
    }

    /**
     * 获取全局唯一整型值ID
     * <br> 兼容旧接口获取方式
     */
    public static long nextId() {
        return getGlobalUniqueId();
    }

    /**
     * 获取全局唯一ID
     * <br> 长整形ID：生成需要初始化机器码片段ID，默认值是当前机器网卡参数
     *
     * @return ID整型值
     */
    public static long getGlobalUniqueId() {
        if (_idWorker == null) {
            // throw new IllegalArgumentException("_idWorker Args not init");
            // 默认使用当前机器ID
            init(DEFAULT_MACHINE_ID);
        }
        return _idWorker.nextId();
    }

    /**
     * 获取全局唯一的字符串ID
     * <br> 参考Mongo.ObjectId实现
     *
     * @return ID字符串，24字节长度（数字+字母的）字符串
     */
    public static String getGlobalUniqueStringId() {
        ObjectId oid = new ObjectId();
        return oid.toString();
    }
}
