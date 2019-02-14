package com.dynask.id.snowflake;

import com.dynask.id.IdService;
import com.dynask.id.Id;

public class SnowflakeWorker implements IdService {

    private IdWorker idWorker;

    public SnowflakeWorker(){
        idWorker = new IdWorker();
    }

    public SnowflakeWorker(IdConfig idConfig){
        idWorker = new IdWorker(idConfig);
    }

    @Override
    public long getId() {
        return idWorker.nextId();
    }

    @Override
    public Id getId(long id) {
        return idWorker.getId(id);
    }
}
