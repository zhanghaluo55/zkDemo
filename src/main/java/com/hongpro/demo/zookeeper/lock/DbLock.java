package com.hongpro.demo.zookeeper.lock;

import com.hongpro.demo.zookeeper.bean.LockRecord;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author zhangzihong
 * @version 1.0.0.0
 * @description
 * @date 2021/5/17 10:08
 */
public class DbLock implements Lock {

    private static final String LOCK_NAME = "db_lock_stock";

    @Override
    public void lock() {
        while (true) {
            boolean b = tryLock();
            if (b) {
                LockRecord lockRecord = new LockRecord();
                //insert
            } else {
                System.out.println("等待中...");
            }
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    /**
     * select * from lock_record where lock_name = "db_lock_stock
     * @return
     */
    @Override
    public boolean tryLock() {
        //select
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {
        //delete
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
