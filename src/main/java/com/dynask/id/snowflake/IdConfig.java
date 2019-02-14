package com.dynask.id.snowflake;

import lombok.Data;

/**
 * 雪花算法的基本配置
 */
@Data
public class IdConfig {

    private long workerId;
    private long dataCenterId;
    private long startTime;
    private long workerIdBits;
    private long dataCenterIdBits;
    private long sequenceBits;

    public IdConfig(){
        this(DefaultIdConfig.workerId, DefaultIdConfig.dataCenterId);
    }

    public IdConfig(long workerId, long dataCenterId){
        this(workerId, dataCenterId, DefaultIdConfig.startTime, DefaultIdConfig.workerIdBits, DefaultIdConfig.dataCenterIdBits, DefaultIdConfig.sequenceBits);
    }

    public IdConfig(long workerId, long dataCenterId, long startTime, long workerIdBits, long dataCenterIdBits, long sequenceBits){
        this.workerId = workerId;
        this.dataCenterId = dataCenterId;
        this.startTime = startTime;
        this.workerIdBits = workerIdBits;
        this.dataCenterIdBits = dataCenterIdBits;
        this.sequenceBits = sequenceBits;
    }
}
