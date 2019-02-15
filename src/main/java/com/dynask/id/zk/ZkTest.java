package com.dynask.id.zk;

import org.I0Itec.zkclient.IZkChildListener;

import java.util.List;

public class ZkTest {
    public static void main(String[] args) {
        ZkConfig zkConfig = new ZkConfig();
        zkConfig.setServers("127.0.0.1:2181");
        zkConfig.setConnectionTimeout(3000);
        zkConfig.setSessionTimeout(3000);
        Zk zk = new IdZk(zkConfig);

        String parent = zk.getPath(IdZkTarget.PROVIDER, "");

        String path = zk.getPath(IdZkTarget.PROVIDER, "/01");
        System.out.println("path = " + path);
        boolean exists = zk.exists(path);
        System.out.println(exists);

        zk.subscribeChildChanges(parent, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println(parentPath + "s child changed, currentChilds: " + currentChilds);
            }
        });

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        boolean delete = zk.delete(path);
//        System.out.println();
    }
}
