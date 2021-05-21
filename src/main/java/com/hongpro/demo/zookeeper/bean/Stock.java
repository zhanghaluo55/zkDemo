package com.hongpro.demo.zookeeper.bean;

/**
 * @author zhangzihong
 * @version 1.0.0.0
 * @description
 * @date 2021/5/15 18:56
 */
public class Stock {
    //库存数量
    private static int num = 1;

    //减少库存的方法
    public boolean reduceStock() {
        if (num > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            num--;
            return true;
        } else {
            return false;
        }
    }
}
