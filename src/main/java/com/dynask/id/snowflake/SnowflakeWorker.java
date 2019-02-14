package com.dynask.id.snowflake;

import com.dynask.id.IdService;
import com.dynask.id.Id;

public class SnowflakeWorker implements IdService {

    private Snowflake snowflake;

    public SnowflakeWorker(){
        snowflake = new Snowflake();
    }

    public SnowflakeWorker(SnowflakeConfig snowflakeConfig){
        snowflake = new Snowflake(snowflakeConfig);
    }

    @Override
    public long getId() {
        return snowflake.nextId();
    }

    @Override
    public Id getId(long id) {
        return snowflake.getId(id);
    }
}
