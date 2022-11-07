package redis;

import redis.clients.jedis.Jedis;

public class JedisTest
{
    public static void main(String[] args){
        Jedis jedis=new Jedis("10.0.2.15",32768);
        System.out.println(jedis.ping());
    }
}
