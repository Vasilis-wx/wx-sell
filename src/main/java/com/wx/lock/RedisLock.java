package com.wx.lock;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Created by wx
 * 2018/8/24
 */
@Component
@Slf4j
public class RedisLock {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 加锁
     * @param key
     * @param value 当前时间+有效时间
     * @return
     */
    public boolean lock(String key,String value){

        if(stringRedisTemplate.opsForValue().setIfAbsent(key,value)){

            return true;
        }

        String currentValue = stringRedisTemplate.opsForValue().get(key);
        if(StringUtils.isNotBlank(currentValue) && Long.valueOf(currentValue) < System.currentTimeMillis()){

            String oldValue = stringRedisTemplate.opsForValue().getAndSet(key,value);

            if(StringUtils.isNotBlank(oldValue) && oldValue.equals(currentValue)){
                return true;
            }

        }
        return false;
    }

    /**
     * 解锁
     * @param key
     * @param value 当前时间+有效时间
     * @return
     */
    public void unlock(String key,String value){

        try {
            String currentValue = stringRedisTemplate.opsForValue().get(key);

            if(StringUtils.isNotBlank(currentValue) && currentValue.equals(value)){
                stringRedisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("redis分布式锁，解锁异常e={}",e);
        }

    }
}
