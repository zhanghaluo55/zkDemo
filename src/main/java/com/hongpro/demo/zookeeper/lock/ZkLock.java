package com.hongpro.demo.zookeeper.lock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.redisson.config.Config;

import java.io.IOException;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author zhangzihong
 * @version 1.0.0.0
 * @description
 * @date 2021/5/17 11:18
 */
public class ZkLock implements Lock {
    private ZooKeeper zk;
    private String root = "/locks";
    private String lockName;
    //当前线程创建的序列node
    private ThreadLocal<String> nodeId = new ThreadLocal<>();
    //用于同步等待zkClient连接到了服务器
    private CountDownLatch connectedSignal = new CountDownLatch(1);
    private final static int sessionTimeout = 3000;
    private final static byte[] data = new byte[0];

    public ZkLock(String config, String lockName) {
        this.lockName = lockName;
        try {
            zk = new ZooKeeper(config, sessionTimeout, new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    //建立连接
                    if (watchedEvent.getState() == Event.KeeperState.SyncConnected) {
                        connectedSignal.countDown();
                    }
                }
            });
            connectedSignal.await();
            Stat stat = zk.exists(root, false);
            if (null == stat) {
                zk.create(root, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    class LockWatch implements Watcher {
        private CountDownLatch latch = null;

        public LockWatch(CountDownLatch latch) {
            this.latch = latch;
        }


        @Override
        public void process(WatchedEvent watchedEvent) {
            if (watchedEvent.getType() == Event.EventType.NodeDeleted) {
                latch.countDown();
            }
        }
    }

    @Override
    public void lock() {
        String myNode = null;
        try {
            myNode = zk.create(root + "/" + lockName, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println(Thread.currentThread().getName() + myNode + "created");

            List<String> subNodes = zk.getChildren(root, false);
            TreeSet<String> sortedNodes = new TreeSet<>();
            for (String node : subNodes) {
                sortedNodes.add(root + "/" + node);
            }

            String smallNode = sortedNodes.first();

            if (myNode.equals(smallNode)) {
                System.out.println(Thread.currentThread().getName() + myNode + " get lock");
                this.nodeId.set(myNode);
                return;
            }

            String preNode = sortedNodes.lower(myNode);

            CountDownLatch latch = new CountDownLatch(1);
            Stat stat = zk.exists(preNode, new LockWatch(latch));

            if (stat != null) {
                System.out.println(Thread.currentThread().getName() + "-" + myNode + " waiting for " + root + "/" + preNode + "release lock");
                latch.await();
                nodeId.set(myNode);
                latch = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        System.out.println(Thread.currentThread().getName() + "unlock");
        if (null != nodeId) {
            try {
                zk.delete(nodeId.get(), -1);
                nodeId.remove();
            } catch (InterruptedException | KeeperException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
