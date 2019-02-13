package com.dynask.id;

public class DefaultIdService implements IdService {

    private SnowflakeWorker snowflakeWorker;

    @Override
    public long getId() {
        return snowflakeWorker.nextId();
    }

    @Override
    public Id getId(long id) {
        return null;
    }
}
