package com.dynask.id.zk;

import lombok.Data;

@Data
public class ZkConfig {
    private String servers;
    private int sessionTimeout;
    private int connectionTimeout;
}
