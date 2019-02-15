package com.dynask.id.zk;

public class EphemeralTest {
    public static void main(String[] args) {
        ZkConfig zkConfig = new ZkConfig();
        zkConfig.setServers("127.0.0.1:2181");
        zkConfig.setConnectionTimeout(3000);
        zkConfig.setSessionTimeout(3000);
        Zk zk = new IdZk(zkConfig);

        String path = zk.getPath(IdZkTarget.PROVIDER, "");
        System.out.println("path = " + path);
        boolean exists = zk.exists(path);
        System.out.println(exists);
        if (!exists){
            zk.createPersistentWithParent(path);
        }
        path = zk.getPath(IdZkTarget.PROVIDER, "/01");
        exists = zk.exists(path);
        System.out.println(exists);
        if (!exists){
            zk.createEphemeralSequential(path, "01节点");
        }
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
