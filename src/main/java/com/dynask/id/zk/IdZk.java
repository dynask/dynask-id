package com.dynask.id.zk;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * zookeeper操作类
 */
public class IdZk implements Zk {

    private static final String ROOT_PATH = "/config/dynask-id";

    private static final String PROVIDER_PATH = "/provider";

    private static final String CONSUMER_PATH = "/consumer";

    private ZkClient zkClient;

    public IdZk(ZkConfig zkConfig) {
        this.zkClient = new ZkClient(zkConfig.getServers(), zkConfig.getSessionTimeout(), zkConfig.getConnectionTimeout());
    }


    @Override
    public void createPersistent(String path) {
        zkClient.createPersistent(path);
    }

    @Override
    public void createPersistentWithParent(String path) {
        zkClient.createPersistent(path, true);
    }

    @Override
    public void createPersistent(String path, Object data) {
        zkClient.createPersistent(path, data);
    }

    @Override
    public void createPersistentSequential(String path, Object data) {
        zkClient.createPersistentSequential(path, data);
    }

    @Override
    public void createEphemeral(String path) {
        zkClient.createEphemeral(path);
    }

    @Override
    public void createEphemeral(String path, Object data) {
        zkClient.createEphemeral(path, data);
    }

    @Override
    public void createEphemeralSequential(String path, Object data) {
        zkClient.createEphemeralSequential(path, data);
    }

    @Override
    public boolean delete(String path) {
        return zkClient.delete(path);
    }

    @Override
    public boolean deleteRecursive(String path) {
        return zkClient.deleteRecursive(path);
    }

    @Override
    public List<String> getChildren(String path) {
        return zkClient.getChildren(path);
    }

    @Override
    public int countChildren(String path) {
        return zkClient.countChildren(path);
    }

    @Override
    public boolean exists(String path) {
        return zkClient.exists(path);
    }

    @Override
    public <T> T readData(String path) {
        return zkClient.readData(path);
    }

    @Override
    public void writeData(String path, Object object) {
        zkClient.writeData(path, object);
    }

    @Override
    public String getPath(IdZkTarget target, String path) {
        switch (target) {
            case CONSUMER:
                return ROOT_PATH + CONSUMER_PATH + path;
            case PROVIDER:
                return ROOT_PATH + PROVIDER_PATH + path;
        }
        return path;
    }

    @Override
    public List<String> subscribeChildChanges(String path, IZkChildListener listener) {
        return zkClient.subscribeChildChanges(path, listener);
    }

}
