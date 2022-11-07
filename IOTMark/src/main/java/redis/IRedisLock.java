package redis;

public interface IRedisLock {
    Boolean lock(int lockKeyExpireSecond, String lockName, Boolean isWait) throws Exception;
    Boolean unlock(String lockName) throws Exception;
}
