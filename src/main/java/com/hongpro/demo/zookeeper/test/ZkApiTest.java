package com.hongpro.demo.zookeeper.test;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author zhangzihong
 * @version 1.0.0.0
 * @description
 * @date 2021/5/15 15:06
 */
public class ZkApiTest {
    @Test
    public void test() throws IOException, InterruptedException, KeeperException {
        //创建zookeeper连接
        ZooKeeper zooKeeper = new ZooKeeper("192.168.56.103:2181", 2000, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println("触发了" + watchedEvent.getType() + "事件");
            }
        });
/*
        String path = zooKeeper.create("/hongpro",
                "hongpro".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        System.out.println(path);*/

        /*String path = zooKeeper.create("/hongpro/hongproZookeeper",
                "hongproZookeeper".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        System.out.println(path);*/

        byte[] data = zooKeeper.getData("/hongpro", false, null);
        System.out.println(new String(data));
        List<String> children = zooKeeper.getChildren("/hongpro", false);

        Stat stat = zooKeeper.setData("/hongpro", "hongproUpdate".getBytes(StandardCharsets.UTF_8), -1);
        System.out.println(stat);

        //zooKeeper.exists()


    }

}
