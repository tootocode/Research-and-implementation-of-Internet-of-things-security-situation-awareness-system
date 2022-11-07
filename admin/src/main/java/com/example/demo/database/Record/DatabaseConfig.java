package com.example.demo.database.Record;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mysql")
public class DatabaseConfig {
    private String mysqlip;
    public String getMysqlip(){
        return mysqlip;
    }
    public void setMysqlip(String mysqlip){
        this.mysqlip=mysqlip;
    }
}
