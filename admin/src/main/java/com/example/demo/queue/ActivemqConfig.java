package com.example.demo.queue;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.regex.Pattern;
@Component
@ConfigurationProperties(prefix = "activemqconfig")
public class ActivemqConfig {
    private String activemqip;
    public String getActiveMqIP(){
        return  activemqip;
    }
    public void setActiveMqIP(String activeMqIP){
        this.activemqip=activeMqIP;
    }
}
