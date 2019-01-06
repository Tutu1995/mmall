package com.mmall.task;

import com.mmall.common.Const;
import com.mmall.service.IOrderService;
import com.mmall.util.PropertiesUtil;
import com.mmall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by tino on 1/5/19.
 */
@Component
@Slf4j
public class CloseOrderTask {
    @Autowired
    private IOrderService iOrderService;

//    @Scheduled(cron="0 */1 * * * ?") // per minute
    public void closeOrderTaskV1() {
        log.info("Close order task starts.");
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", "2"));
        iOrderService.closeOrder(hour);
        log.info("Close order task ends.");

    }

    public void closeOrderTaskV2() {
        log.info("Close order task starts.");
        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));
        Long setnxResult = RedisShardedPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeout));
        if(setnxResult != null && setnxResult == 1) {
            // if setnxResult = 1, successfully set value, get lock
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        } else {
            log.info("Do not get distributed lock:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        }
        log.info("Close order task ends.");
    }

    private void closeOrder(String lockName) {
        RedisShardedPoolUtil.expire(lockName, 50);
        log.info("Get{}, THreadName:{}", lockName, Thread.currentThread().getName());
        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", "2"));
        iOrderService.closeOrder(hour);
        RedisShardedPoolUtil.del(lockName);
        log.info("Release{}, THreadName:{}", lockName, Thread.currentThread().getName());
    }



}
