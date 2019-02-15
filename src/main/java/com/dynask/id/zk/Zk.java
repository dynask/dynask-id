package com.dynask.id.zk;

import org.I0Itec.zkclient.IZkChildListener;

import java.util.List;

public interface Zk {
    void createPersistent(String path);
    void createPersistentWithParent(String path);
    void createPersistent(String path, Object data);
    void createPersistentSequential(String path, Object data);

    void createEphemeral(String path);
    void createEphemeral(String path, Object data);
    void createEphemeralSequential(String path, Object data);

    boolean delete(String path);
    boolean deleteRecursive(String path);

    List<String> getChildren(String path);
    int countChildren(String path);
    boolean exists(final String path);

    <T extends Object> T readData(String path);
    void writeData(String path, Object object);

    String getPath(IdZkTarget target, String path);

    List<String> subscribeChildChanges(String path, IZkChildListener listener);
}
