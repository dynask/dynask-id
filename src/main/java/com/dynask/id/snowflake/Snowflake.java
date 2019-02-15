package com.dynask.id.snowflake;

import com.dynask.id.Id;

/**
 * Twitter Snowflake<br>
 * SnowFlake的结构如下(每部分用-分开):<br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值，这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下面程序IdWorker类的startTime属性）。41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId<br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
 * 加起来刚好64位，为一个Long型。<br>
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 */
public class Snowflake {

    /**
     * 开始时间截 (2018-01-01)
     */
    private long startTime = 1514736000000L;

    /**
     * 机器id所占的位数
     */
    private long workerIdBits = 5L;

    /**
     * 数据标识id所占的位数
     */
    private long dataCenterIdBits = 5L;

    /**
     * 序列在id中占的位数
     */
    private long sequenceBits = 12L;

    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private long maxWorkerId = -1L ^ (-1L << workerIdBits);

    /**
     * 支持的最大数据标识id，结果是31
     */
    private long maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);

    /**
     * 机器ID向左移12位
     */
    private long workerIdShift = sequenceBits;

    /**
     * 数据标识id向左移17位(12+5)
     */
    private long dataCenterIdShift = sequenceBits + workerIdBits;

    /**
     * 时间截向左移22位(5+5+12)
     */
    private long timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;

    /**
     * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**
     * 工作机器ID(0~31)
     */
    private long workerId = 0L;

    /**
     * 数据中心ID(0~31)
     */
    private long dataCenterId = 0L;

    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence = 0L;

    /**
     * 上次生成ID的时间截
     */
    private long lastTimestamp = -1L;

    public Snowflake(){
        SnowflakeConfig snowflakeConfig = new SnowflakeConfig();
        initIdWorker(snowflakeConfig.getWorkerId(), snowflakeConfig.getDataCenterId(), snowflakeConfig.getStartTime(), snowflakeConfig.getWorkerIdBits(), snowflakeConfig.getDataCenterIdBits(), snowflakeConfig.getSequenceBits());
    }

    public Snowflake(SnowflakeConfig snowflakeConfig){
        initIdWorker(snowflakeConfig.getWorkerId(), snowflakeConfig.getDataCenterId(), snowflakeConfig.getStartTime(), snowflakeConfig.getWorkerIdBits(), snowflakeConfig.getDataCenterIdBits(), snowflakeConfig.getSequenceBits());
    }

    private void initIdWorker(long workerId, long dataCenterId, long startTime, long workerIdBits, long dataCenterIdBits, long sequenceBits) {

        /**
         * 开始时间截 (2018-01-01)
         */
        this.startTime = startTime;

        /**
         * 机器id所占的位数
         */
        this.workerIdBits = workerIdBits;

        /**
         * 数据标识id所占的位数
         */
        this.dataCenterIdBits = dataCenterIdBits;

        /**
         * 序列在id中占的位数
         */
        this.sequenceBits = sequenceBits;

        /**
         * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
         */
        this.maxWorkerId = -1L ^ (-1L << workerIdBits);

        /**
         * 支持的最大数据标识id，结果是31
         */
        this.maxDataCenterId = -1L ^ (-1L << dataCenterIdBits);

        /**
         * 机器ID向左移12位
         */
        this.workerIdShift = sequenceBits;

        /**
         * 数据标识id向左移17位(12+5)
         */
        this.dataCenterIdShift = sequenceBits + workerIdBits;

        /**
         * 时间截向左移22位(5+5+12)
         */
        this.timestampLeftShift = sequenceBits + workerIdBits + dataCenterIdBits;

        /**
         * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
         */
        this.sequenceMask = -1L ^ (-1L << sequenceBits);

        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker id can't be greater than %d or less than 0", maxWorkerId));
        }
        if (dataCenterId > maxDataCenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("data center Id can't be greater than %d or less than 0", maxDataCenterId));
        }
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
    }

    /**
     * 获得下一个ID (该方法是线程安全的)
     *
     * @return SnowflakeId
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        //如果是同一时间生成的，则进行毫秒内序列
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            //毫秒内序列溢出
            if (sequence == 0) {
                //阻塞到下一个毫秒,获得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            //时间戳改变，毫秒内序列重置
            sequence = 0L;
        }

        //上次生成ID的时间截
        lastTimestamp = timestamp;

//        System.out.println("sequence = " + sequence);
//        System.out.println("workerId = " + workerId);
//        System.out.println("dataCenterId = " + dataCenterId);
//        System.out.println("当前时间 = " + timestamp);
//        System.out.println("timestamp = " + (timestamp - startTime));

        //移位并通过或运算拼到一起组成64位的ID
        return ((timestamp - startTime) << timestampLeftShift) //
                | (dataCenterId << dataCenterIdShift) //
                | (workerId << workerIdShift) //
                | sequence;
    }

    public Id getId(long id){
        Id myId = new Id();
        myId.setWorkerId(workerId);
        myId.setDataCenterId(dataCenterId);
//        long sequence = id&(long)(Math.pow(2, sequenceBits)-1);
//        System.out.println("sequence = " + sequence);
//        long workerId1 = (id & (workerId << workerIdShift)) >> workerIdShift;
//        System.out.println("workerId = " + workerId1);
//        long dataCenterId1 = (id & (dataCenterId << dataCenterIdShift)) >> dataCenterIdShift;
//        System.out.println("dataCenterId = " + dataCenterId1);
        long timestamp = id  >> timestampLeftShift;
//        System.out.println("timestamp = " + timestamp);
        myId.setTimestamp(startTime+timestamp);
        return myId;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTimestamp 上次生成ID的时间截
     * @return 当前时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     *
     * @return 当前时间(毫秒)
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }


    /**
     * 测试
     */
    public static void main(String[] args) {
         Snowflake snowflake = new Snowflake();
        long id = snowflake.nextId();
        System.out.println("id = " + id);
//        SnowflakeWorker idWorker = new SnowflakeWorker(1, 1, 1514736000000L, 7, 6, 10);
//        for (int i = 0; i < 1000; i++) {
//            long id = idWorker.nextId();
//            System.out.println(id);
//        }

        // System.out.println(Long.valueOf(StringToA("1ZZZZ1ZZZ")));
        System.out.println(snowflake.getId(id));
        long a = 5l;
        long b = 7l;
        System.out.println(a&b);
    }

}
