package com.example.demo.controller;

import com.example.demo.database.Query.tlsbugInfoQuery;
import com.example.demo.database.Record.DatabaseConfig;
import com.example.demo.database.Record.Record;
import com.example.demo.database.Record.RecordFactory;
import com.example.demo.database.Record.TLSRecord;
import com.example.demo.entity.TLSInfo;
import com.example.demo.queue.ActivemqConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class tlsBugInfoController {

    @Autowired
    private DatabaseConfig databaseConfig;

    @GetMapping("/posttlsbugInfo")
    public String postdata(String name,String desc){
        RecordFactory recordFactory=new RecordFactory(databaseConfig);
        Record record=recordFactory.get(TLSRecord.class);
        record.addRecord(name,desc);
        return "success";
    }

    @GetMapping("/searchtlsbugInfo")
    public List<TLSInfo> getdata(String name) throws SQLException {
        if(name.equals(""))return new ArrayList<>();
        String sql="select * from tlsInfo where tls='"+name+"'";
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        tlsbugInfoQuery tlsbugInfoQuery=new tlsbugInfoQuery(connection);
        System.out.println(sql);
        List<TLSInfo> result=tlsbugInfoQuery.queryRecord(sql);
        connection.close();
        System.out.println(sql);
        return result;
    }

    @GetMapping("/gettlsbuginfolist")
    public List<TLSInfo> getInitlist() throws SQLException {
        String sql="select * from tlsInfo";
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        tlsbugInfoQuery tlsbugInfoQuery=new tlsbugInfoQuery(connection);
        List<TLSInfo> list=new ArrayList<>();
        list=tlsbugInfoQuery.queryRecord(sql);
        connection.close();
        return list;
    }
}
