package com.dynask.id;

import lombok.Data;

@Data
public class Id {
    private Long workerId;
    private Long dataCenterId;
    private Long startTime;
    private Long workerIdBits;
    private Long dataCenterIdBits;
    private Long sequenceBits;
    private Long timestamp;
}
