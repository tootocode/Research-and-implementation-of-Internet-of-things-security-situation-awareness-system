package com.example.demo.controller;

import com.example.demo.database.Query.Query;
import com.example.demo.database.Query.QueryFactory;
import com.example.demo.database.Query.protocolInfoQuery;
import com.example.demo.database.Record.DatabaseConfig;
import com.example.demo.database.Record.ProtocolRecord;
import com.example.demo.database.Record.Record;
import com.example.demo.database.Record.RecordFactory;
import com.example.demo.entity.protocolInfo;
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
public class protocolInfoController {
    @Autowired
    private DatabaseConfig databaseConfig;

    @GetMapping("/postprotocolInfo")
    public String postdata(String name,String desc){
        RecordFactory recordFactory=new RecordFactory(databaseConfig);
        Record protocolRecorder=recordFactory.get(ProtocolRecord.class);
        protocolRecorder.addRecord(name,desc);
        return "success";
    }
    @GetMapping("/searchprotocolinfo")
    public List<protocolInfo> getdata(String name) throws Exception {

        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        protocolInfoQuery protocolInfoQuery=new protocolInfoQuery(connection);
        if(!name.equals("")){
            String sql="select * from protocolInfo where protocol='"+name+"';";
            System.out.println(sql);
            List<protocolInfo> result=protocolInfoQuery.queryRecord(sql);
            connection.close();
            for(protocolInfo i:result)
                System.out.println(i.getProtocol());
            return result;
        }
        return new ArrayList<>();
    }
    @GetMapping("/protocolInfoInit")
    public List<protocolInfo> getalldata() throws Exception {
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        protocolInfoQuery protocolInfoQuery=new protocolInfoQuery(connection);
        String sql="select * from protocolInfo";
        List<protocolInfo> list=new ArrayList<>();
        list=protocolInfoQuery.queryRecord(sql);
        connection.close();
        return list;
    }
}
