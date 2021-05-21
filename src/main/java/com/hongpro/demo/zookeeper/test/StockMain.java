package com.hongpro.demo.zookeeper.test;

import com.hongpro.demo.zookeeper.bean.Stock;
import com.hongpro.demo.zookeeper.lock.ZkLock;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.config.Config;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhangzihong
 * @version 1.0.0.0
 * @description
 * @date 2021/5/15 18:59
 */
public class StockMain {
    private static Lock lock = new ReentrantLock();

    private static ZkLock mylock;

    //private static RLock mylock;
    static {
        /*Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.56.103:6379").setPassword("atmpredis").setDatabase(0);
        Redisson redisson = (Redisson) Redisson.create(config);
        mylock = redisson.getLock("redis_lock_stock");*/

        mylock = new ZkLock("192.168.56.103:2181", "stock_zk");
    }


    static class StockThread implements Runnable {
        @Override
        public void run() {
            mylock.lock();
            boolean b = new Stock().reduceStock();
            mylock.unlock();
            if (b) {
                System.out.println(Thread.currentThread().getName() + "减少库存成功");
            } else {
                System.out.println(Thread.currentThread().getName() + "减少库存失败");
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new StockThread(), "线程1").start();
        new Thread(new StockThread(), "线程2").start();

    }
}
