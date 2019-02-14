package com.dynask.id.snowflake;

/**
 * 雪花算法的默认配置
 */
public interface DefaultSnowflakeConfig {

    long workerId = 0L;
    long dataCenterId = 0L;
    long startTime = 1514736000000L;
    long workerIdBits = 5L;
    long dataCenterIdBits = 5L;
    long sequenceBits = 12L;

}
