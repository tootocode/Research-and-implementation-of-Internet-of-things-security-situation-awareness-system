package redis;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

public class RedisLock {
    private static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RedisLock.class);
    private static String redisIdentityKey = UUID.randomUUID().toString();
    public static String getRedisIdentityKey() {
        return redisIdentityKey;
    }
    private static final long WaitLockTimeSecond = 2000;
    private static final int RetryCount = 10;
//    public static  RedisConfig redisconfig=new RedisConfig("/Config");
    public static  RedisConfig redisconfig=new RedisConfig("/Config");
    private JedisPool jedisPool=new JedisPool(redisconfig.RedisIP,32768);
    private Jedis jedis=jedisPool.getResource();


    public Boolean lock(int lockNameExpireSecond, String lockName, Boolean isWait) throws Exception {
        if (StringUtils.isEmpty(lockName))
            throw new Exception("lockName is empty.");
        int retryCounts = 0;
        while (true) {
            Long status, expire = 0L;
            status = jedis.setnx(lockName, redisIdentityKey);
            if (status > 0) {
                expire = jedis.expire(lockName, lockNameExpireSecond);
            }
            if (status > 0 && expire > 0) {
                logger.info(String.format("t:%s,当前节点：%s,获取到锁：%s",
                        Thread.currentThread().getId(),
                        getRedisIdentityKey(),
                        lockName));
                return true;/** 获取到lock */
            }

            try {
                if (isWait && retryCounts < RetryCount) {
                    retryCounts++;
                    synchronized (this) {
                        logger.info(String.format("t:%s,当前节点：%s,尝试等待获取锁：%s",
                                Thread.currentThread().getId(),
                                getRedisIdentityKey(),
                                lockName));

                        // 未能获取到lock，进行指定时间的wait再重试.
                        this.wait(WaitLockTimeSecond);
                    }
                } else if (retryCounts == RetryCount) {
                    logger.info(String.format("t:%s,当前节点：%s,指定时间内获取锁失败：%s",
                            Thread.currentThread().getId(),
                            getRedisIdentityKey(),
                            lockName));
                    return false;
                } else {
                    return false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean unlock(String lockName) throws Exception {
        if (StringUtils.isEmpty(lockName))
            throw new Exception("lockName is empty.");

        long status = jedis.del(lockName);
        if (status > 0) {
            logger.info(String.format("t:%s,当前节点：%s,释放锁：%s 成功。",
                    Thread.currentThread().getId(),
                    getRedisIdentityKey(),
                    lockName));
            return true;
        }
        logger.info(String.format("t:%s,当前节点：%s,释放锁：%s 失败。",
                Thread.currentThread().getId(),
                getRedisIdentityKey(),
                lockName));
        return false;
    }
}
