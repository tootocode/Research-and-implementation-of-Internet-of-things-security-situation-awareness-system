package com.example.demo.database.Record;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

@Component
public class RecordFactory {

    private  Map<Class<? extends AbstractRecord>,Record> RecordMap=new HashMap<Class<? extends AbstractRecord>, Record>();
    public Connection connection;

    public RecordFactory(DatabaseConfig databaseConfig){
        String driver = "com.mysql.cj.jdbc.Driver";
        try{
            //System.out.println(databaseConfig.getMysqlip());
            String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            connection= DriverManager.getConnection(mysqlPath,"root","123456");
            RecordMap.put(ProtocolRecord.class,new ProtocolRecord(connection));
            RecordMap.put(DeviceRecord.class,new DeviceRecord(connection));
            RecordMap.put(TLSRecord.class,new TLSRecord(connection));
            RecordMap.put(exploitRecord.class,new exploitRecord(connection));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public  Record get(Class<? extends AbstractRecord> clazz){
        return RecordMap.get(clazz);
    }

}
