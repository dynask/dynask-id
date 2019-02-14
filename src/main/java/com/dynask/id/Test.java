package com.dynask.id;

import com.dynask.id.snowflake.SnowflakeConfig;
import com.dynask.id.snowflake.SnowflakeWorker;

public class Test {
    public static void main(String[] args) {
        IdService idService = new SnowflakeWorker();

        for(int i=0; i< 1000; i++){
            long id = idService.getId();
            System.out.println("id = " + id);
            Id id1 = idService.getId(id);
            System.out.println("id1 = " + id1);
        }


        SnowflakeConfig snowflakeConfig = new SnowflakeConfig(1,3);

        IdService idService1 = new SnowflakeWorker(snowflakeConfig);
        long id2 = idService1.getId();
        System.out.println("id2 = " + id2);
        Id id3 = idService1.getId(id2);
        System.out.println("id3 = " + id3);
        System.out.println(Long.MAX_VALUE);
    }
}
