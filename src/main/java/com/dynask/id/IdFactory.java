package com.dynask.id;

import java.util.Iterator;
import java.util.ServiceLoader;

public class IdFactory {

    private IdFactory(){}

    public static IdProvider newIdProvider(){
        IdProvider idProvider = null;
        ServiceLoader<IdProvider> serviceLoader = ServiceLoader.load(IdProvider.class);
        Iterator<IdProvider> idProviders = serviceLoader.iterator();
        if (idProviders.hasNext()) {
            idProvider = idProviders.next();
        }
        if (idProvider == null) {//如果没有spi实现,就是用默认的类对象实现
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            try {
                idProvider = (IdProvider) classLoader.loadClass("com.dynask.id.snowflake.SnowflakeWorker").newInstance();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return idProvider;
    }
}
