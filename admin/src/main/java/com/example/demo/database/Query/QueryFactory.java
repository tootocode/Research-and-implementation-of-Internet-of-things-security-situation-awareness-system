package com.example.demo.database.Query;

import com.example.demo.database.Record.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

public class QueryFactory {
    private Map<Class<? extends AbstractQuery>, Query> RecordMap=new HashMap<Class<? extends AbstractQuery>, Query>();
    public Connection connection;
    public QueryFactory(DatabaseConfig databaseConfig){
        String driver = "com.mysql.cj.jdbc.Driver";
        try{
            //System.out.println(databaseConfig.getMysqlip());
            String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            connection= DriverManager.getConnection(mysqlPath,"root","123456");
            RecordMap.put(protocolInfoQuery.class,new protocolInfoQuery(connection));
            RecordMap.put(deviceInfoQuery.class,new deviceInfoQuery(connection));
            RecordMap.put(exploitInfoQuery.class,new exploitInfoQuery(connection));
            RecordMap.put(tlsbugInfoQuery.class,new tlsbugInfoQuery(connection));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public  Query get(Class<? extends AbstractQuery> clazz){
        return RecordMap.get(clazz);
    }
}
