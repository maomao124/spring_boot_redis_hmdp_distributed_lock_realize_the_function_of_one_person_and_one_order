package mao.spring_boot_redis_hmdp.utils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Project name(项目名称)：spring_boot_redis_hmdp_distributed_lock_realize_the_function_of_one_person_and_one_order
 * Package(包名): mao.spring_boot_redis_hmdp.utils
 * Class(类名): RedisLockImpl
 * Author(作者）: mao
 * Author QQ：1296193245
 * GitHub：https://github.com/maomao124/
 * Date(创建日期)： 2022/5/17
 * Time(创建时间)： 10:53
 * Version(版本): 1.0
 * Description(描述)： 简单分布式锁，非单例
 */


public class RedisLockImpl implements RedisLock
{
    /**
     * 锁的名称
     */
    private String name;
    /**
     * StringRedisTemplate
     */
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 锁前缀
     */
    private static final String KEY_PREFIX = "lock:";

    /**
     * 构造函数
     *
     * @param name                锁的名称
     * @param stringRedisTemplate StringRedisTemplate
     */
    public RedisLockImpl(String name, StringRedisTemplate stringRedisTemplate)
    {
        this.name = name;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean tryLock(long timeoutSec)
    {
        //获得线程标识
        long threadID = Thread.currentThread().getId();
        //锁key
        String lockKey = KEY_PREFIX + name;
        //获取锁
        Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, String.valueOf(threadID),
                timeoutSec, TimeUnit.SECONDS);
        //返回
        return Boolean.TRUE.equals(result);
    }

    @Override
    public void unlock()
    {
        //锁key
        String lockKey = KEY_PREFIX + name;
        //释放
        stringRedisTemplate.delete(lockKey);
    }
}
