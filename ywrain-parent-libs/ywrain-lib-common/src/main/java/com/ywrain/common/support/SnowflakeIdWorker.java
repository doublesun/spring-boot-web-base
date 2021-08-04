package com.ywrain.common.support;

/**
 * 提供全局的分布式唯一ID生成服务, 在Twitter的Snowflake算法基础上改进
 * <pre>
 * 满足目标：
 *  1、全局唯一
 *  2、分布式生成，按模块区分
 *  3、趋势递增，可用于排序
 *  4、使用long整型值，对DB索引友好
 * </pre>
 * <br> 0 - 0000000000 0000000000 0000000000 0000000000 0 - 0000 0000 0000 - 0000 000000
 * <br> 最高1位是long型的符号位
 * <br> 41位毫秒时间戳，默认从2017年开始
 * <br> 12位机器识别码(取值范围：0~4096)
 * <br> 10位序列，毫秒内的计数，设置为跨毫秒不重置，毫秒内取随机值(取值范围：0~1023)
 *
 * @author weipengfei@youcheyihou.com
 */
public class SnowflakeIdWorker {

    /**
     * 开始时间戳(2017-01-01 00:00:00)
     */
    private final long tsepoch = 1483200000000L;

    /**
     * 机器识别码位数
     */
    private final long machineIdBits = 12L;

    /**
     * 序列号位数
     */
    private final long seqBits = 10L;

    /**
     * 机器标识最大值
     */
    private final long maxMachineId = -1L ^ (-1L << machineIdBits);

    /**
     * 机器标识移位操作数
     */
    private final long machineIdShift = seqBits;

    /**
     * 时间戳移位操作数
     */
    private final long timeShift = machineIdBits + machineIdShift;

    /**
     * 序列生成掩码
     */
    private final long seqMask = -1L ^ (-1L << seqBits);

    // -----------------------------
    // 当前机器标识ID
    private long machineId;
    // 当前序列号
    private long seqId = 0L;
    // 上一个生成时的时间戳
    private long lastTs = 0L;

    public SnowflakeIdWorker(long machineId) {
        if (machineId > maxMachineId || machineId < 0) {
            throw new IllegalArgumentException(String.format("machinedId Id can't be greater than %d or less than 0", maxMachineId));
        }
        this.machineId = machineId;
    }

    /**
     * 获取下一个ID
     * <br> 使用同步锁
     *
     * @return long ID
     */
    public synchronized long nextId() {
        long ts = getNowTs();

        // 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (ts < lastTs) {
            throw new RuntimeException(String.format("system timestamp was changeed, lasttimestamp %d ", lastTs));
        }

        // 同一毫秒，序列号递增
        if (ts == lastTs) {
            seqId = (seqId + 1) & seqMask;
            if (seqId == 0) {
                // 溢出，阻塞到下一毫秒
                try {
                    ts = utilNextMillis(lastTs);
                } catch (InterruptedException e) {
                    throw new CommonException(e);
                }
            }
        }

        if (ts > lastTs) {
            // seqId = 0L;
            // 暂时不立即重置，尾数递增，保证分布随机性
            seqId = (seqId + 1) & seqMask;
            // 序列计数溢出时，重置
            if (seqId >= 10) {
                seqId = 0L;
            }
        }

        lastTs = ts;

        return ((ts - tsepoch) << timeShift) | (machineId << machineIdShift) | seqId;
    }

    /**
     * 获取当前毫秒时间戳
     */
    public long getNowTs() {
        return System.currentTimeMillis();
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long utilNextMillis(long lastTs) throws InterruptedException {
        long ts = getNowTs();
        while (ts <= lastTs) {
            Thread.sleep(1);
            ts = getNowTs();
        }
        return ts;
    }
}

