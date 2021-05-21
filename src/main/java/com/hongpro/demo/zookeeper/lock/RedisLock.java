package com.hongpro.demo.zookeeper.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author zhangzihong
 * @version 1.0.0.0
 * @description
 * @date 2021/5/17 10:47
 */
public class RedisLock implements Lock {

    private static final String LOCK_NAME = "db_lock_stock";

    @Override
    public void lock() {
        while (true) {
            //Boolean b = redisTemplate.opsForValue().setIfAbsent("lockName", LOCK_NAME);

            //Boolean b = redisTemplate.opsForValue().setIfAbsent("lockName", LOCK_NAME, 15L, TimeUnit.SECOND);
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

    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
