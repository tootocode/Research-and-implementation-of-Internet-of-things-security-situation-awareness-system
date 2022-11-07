package com.example.demo.redis;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.regex.Pattern;
@Component
@ConfigurationProperties(prefix = "redis")
public class RedisConfig {
    public String redisip;
    public String getRedisip(){
        return redisip;
    }
    public void setRedisip(String redisip){
        this.redisip=redisip;
    }
}
