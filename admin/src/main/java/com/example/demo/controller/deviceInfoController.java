package com.example.demo.controller;

import com.example.demo.database.Query.deviceInfoQuery;
import com.example.demo.database.Record.DatabaseConfig;
import com.example.demo.database.Record.DeviceRecord;
import com.example.demo.database.Record.Record;
import com.example.demo.database.Record.RecordFactory;
import com.example.demo.entity.deviceInfo;
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
public class deviceInfoController {

    @Autowired
    private DatabaseConfig databaseConfig;
    @GetMapping("/postdeviceInfo")
    public String postdata(String name,String devicetype,String vendor){
        RecordFactory recordFactory=new RecordFactory(databaseConfig);
        Record record=recordFactory.get(DeviceRecord.class);
        record.addRecord(vendor,name,devicetype);
        return "success";
    }

    @GetMapping("/searchdeviceinfo")
    public List<deviceInfo> getdata(String name, String devicetype, String vendor) throws SQLException {
        if(name.equals("")&&devicetype.equals("")&&vendor.equals(""))return new ArrayList<>();
        String sql="select * from deviceInfo where ";
        if(!name.equals(""))sql=sql+"device='"+name+"'";
        if(!name.equals("")&&!devicetype.equals(""))sql=sql+" and devicetype='"+devicetype+"'";
        else if(name.equals("")&&!devicetype.equals(""))sql=sql+"devicetype='"+devicetype+"'";
        if((!name.equals("")||!devicetype.equals(""))&&!vendor.equals(""))sql=sql+" and vendor='"+vendor+"'";
        else if(name.equals("")&&devicetype.equals(""))sql=sql+"vendor='"+vendor+"'";
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        deviceInfoQuery deviceInfoQuery=new deviceInfoQuery(connection);
        System.out.println(sql);
        List<deviceInfo> result=deviceInfoQuery.queryRecord(sql);
        connection.close();
        return result;
    }

    @GetMapping("/searchdeviceinfolist")
    public List<deviceInfo> getInitlist() throws SQLException {
        String sql="select * from deviceInfo";
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        deviceInfoQuery deviceInfoQuery=new deviceInfoQuery(connection);
        System.out.println(sql);
        List<deviceInfo> result=deviceInfoQuery.queryRecord(sql);
        connection.close();
        return result;
    }
}
