package com.example.demo.controller;


import com.example.demo.database.Query.devicemarktaskQuery;
import com.example.demo.database.Query.protocoltaskQuery;
import com.example.demo.database.Query.tlstaskQuery;
import com.example.demo.database.Record.DatabaseConfig;
import com.example.demo.entity.*;
import com.example.demo.queue.ActivemqConfig;
import com.example.demo.redis.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin
@EnableConfigurationProperties({ActivemqConfig.class})
public class analyseController {
    @Autowired(required = false)
    private ActivemqConfig activemqConfig;

    @Autowired
    private DatabaseConfig databaseConfig;
    @Autowired
    private RedisConfig redisConfig;
    @GetMapping("/getanalyse")
    public analyseEntity getAnalyseDate(String name,String date1,String date2) throws SQLException {
        List<String> tlsbugs=new ArrayList<>();
        tlsbugs.add("heartbleed");
        tlsbugs.add("css");
        tlsbugs.add("ticketbleed");
        tlsbugs.add("robot");
        tlsbugs.add("secure_renego");
        tlsbugs.add("crime");
        tlsbugs.add("breach");
        tlsbugs.add("sweet32");
        tlsbugs.add("freak");
        tlsbugs.add("logjam");
        tlsbugs.add("beast");
        tlsbugs.add("lucky13");
        tlsbugs.add("rc4");
        tlsbugs.add("poodle");
        tlsbugs.add("learn_long_session_live");
        tlsbugs.add("openssl_aes_ni");
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        String sql1="select * from IOTMark where Insert_date>=\'"+date1+"\' and Insert_date<=\'"+date2+"\'"+" and taskname=\'"+name+"\'";
        String sql2="select * from findProtocol where Insert_date>=\'"+date1+"\' and Insert_date<=\'"+date2+"\'"+" and taskname=\'"+name+"\'";
        String sql3="select * from ALLVulTable where Insert_date>=\'"+date1+"\' and Insert_date<=\'"+date2+"\' and ";
        String sql4="select distinct host,taskdate,taskname from CurveSwapFinalTable where Insert_date>=\'"+date1+"\' and Insert_date<=\'"+date2+"\'"+
                "and InvalidCurveSwap='1' or TwistCurveSwap='1'" +" and taskname=\'"+name+"\'";
        devicemarktaskQuery devicemarktaskQuery=new devicemarktaskQuery(connection);
        List<devicetaskInfo> devicemarklist=new ArrayList<>();
        devicemarklist=devicemarktaskQuery.queryRecord(sql1);
        List<protocoltaskInfo> protocollist=new ArrayList<>();
        protocoltaskQuery protocoltaskQuery=new protocoltaskQuery(connection);
        protocollist=protocoltaskQuery.queryRecord(sql2);
        List<tlstaskInfo> tlsbuglist=new ArrayList<>();
        tlstaskQuery tlstaskQuery=new tlstaskQuery(connection);
        for(String bug:tlsbugs){
            String tempsql=sql3+bug+"='1'"+" and taskname=\'"+name+"\'";
            List<tlstaskInfo> temp=new ArrayList<>();
            temp=tlstaskQuery.queryRecord(tempsql,bug);
            tlsbuglist.addAll(temp);
        }
        tlsbuglist.addAll(tlstaskQuery.queryRecord(sql4,"curveswap"));
        HashMap<String,List<devicetaskInfo>> devicemap=new HashMap<>();
        for(devicetaskInfo info:devicemarklist){
            if(!devicemap.containsKey(info.getDevicetype())){
                devicemap.put(info.getDevicetype(),new ArrayList<devicetaskInfo>());
                devicemap.get(info.getDevicetype()).add(info);
            }else{
                devicemap.get(info.getDevicetype()).add(info);
            }
        }

        HashMap<String,List<protocoltaskInfo>> protocolmap=new HashMap<>();
        for(protocoltaskInfo info:protocollist){
            if(!protocolmap.containsKey(info.getProtocol())){
                protocolmap.put(info.getProtocol(),new ArrayList<protocoltaskInfo>());
                protocolmap.get(info.getProtocol()).add(info);
            }else{
                protocolmap.get(info.getProtocol()).add(info);
            }
        }

        HashMap<String,List<tlstaskInfo>> tlsbugmap=new HashMap<>();
        for(tlstaskInfo info:tlsbuglist){
            if(!tlsbugmap.containsKey(info.getTlsbug())){
                tlsbugmap.put(info.getTlsbug(),new ArrayList<tlstaskInfo>());
                tlsbugmap.get(info.getTlsbug()).add(info);
            }else{
                tlsbugmap.get(info.getTlsbug()).add(info);
            }
        }

        analyseEntity analyseEntity=new analyseEntity();
        for(String key:devicemap.keySet()){
            //System.out.println(key);
            analyseEntity.devicetype.add(key);
            analyseEntity.devicenum.add(devicemap.get(key).size());
        }

        for(String key:protocolmap.keySet()){
            analyseEntity.protocol.add(key);
            analyseEntity.protocolnum.add(protocolmap.get(key).size());
        }

        for(String key:tlsbugmap.keySet()){
            analyseEntity.bug.add(key);
            analyseEntity.bugnum.add(tlsbugmap.get(key).size());
        }
        return analyseEntity;
    }

    @GetMapping("/getanalyseCompare")
    public compareEntity getCompareData(String name1,String date1_1,String date1_2,String name2,String date2_1,String date2_2) throws SQLException {

        List<String> tlsbugs=new ArrayList<>();
        tlsbugs.add("heartbleed");
        tlsbugs.add("css");
        tlsbugs.add("ticketbleed");
        tlsbugs.add("robot");
        tlsbugs.add("secure_renego");
        tlsbugs.add("crime");
        tlsbugs.add("breach");
        tlsbugs.add("sweet32");
        tlsbugs.add("freak");
        tlsbugs.add("logjam");
        tlsbugs.add("beast");
        tlsbugs.add("lucky13");
        tlsbugs.add("rc4");
        tlsbugs.add("poodle");
        tlsbugs.add("learn_long_session_live");
        tlsbugs.add("openssl_aes_ni");

        List<tlstaskInfo> tlsbuglist1=new ArrayList<>();
        List<tlstaskInfo> tlsbuglist2=new ArrayList<>();

        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        String sql1="select * from ALLVulTable where Insert_date>=\'"+date1_1+"\' and Insert_date<=\'"+date1_2+"\' and ";
        String sql2="select distinct host,taskdate,taskname from CurveSwapFinalTable where Insert_date>=\'"+date1_1+"\' and Insert_date<=\'"+date1_2+"\'"+
                "and InvalidCurveSwap='1' or TwistCurveSwap='1'"+" and taskname=\'"+name1+"\'";
        tlstaskQuery tlstaskQuery=new tlstaskQuery(connection);

        String sql3="select * from ALLVulTable where Insert_date>=\'"+date2_1+"\' and Insert_date<=\'"+date2_2+"\' and ";
        String sql4="select distinct host,taskdate,taskname from CurveSwapFinalTable where Insert_date>=\'"+date2_1+"\' and Insert_date<=\'"+date2_2+"\'"+
                "and InvalidCurveSwap='1' or TwistCurveSwap='1'"+" and taskname=\'"+name2+"\'";

        for(String bug:tlsbugs){
            String tempsql=sql1+bug+"='1'"+" and taskname=\'"+name1+"\'";
            List<tlstaskInfo> temp=new ArrayList<>();
            temp=tlstaskQuery.queryRecord(tempsql,bug);
            //for(tlstaskInfo t:temp)System.out.println(t.getTlsbug());
            tlsbuglist1.addAll(temp);
        }
        tlsbuglist1.addAll(tlstaskQuery.queryRecord(sql2,"curveswap"));

        for(String bug:tlsbugs){
            String tempsql=sql3+bug+"='1'"+" and taskname=\'"+name2+"\'";
            //System.out.println(tempsql);
            List<tlstaskInfo> temp=new ArrayList<>();
            temp=tlstaskQuery.queryRecord(tempsql,bug);
            //for(tlstaskInfo t:temp)System.out.println(t.getTlsbug());
            tlsbuglist2.addAll(temp);
        }
        tlsbuglist2.addAll(tlstaskQuery.queryRecord(sql4,"curveswap"));
        connection.close();

        HashMap<String,List<tlstaskInfo>> tlsbugmap1=new HashMap<>();
        for(tlstaskInfo info:tlsbuglist1){
            if(!tlsbugmap1.containsKey(info.getTlsbug())){
                tlsbugmap1.put(info.getTlsbug(),new ArrayList<tlstaskInfo>());
                tlsbugmap1.get(info.getTlsbug()).add(info);
            }else{
                tlsbugmap1.get(info.getTlsbug()).add(info);
            }
        }
        HashMap<String,List<tlstaskInfo>> tlsbugmap2=new HashMap<>();
        for(tlstaskInfo info:tlsbuglist2){
            //System.out.println(info.getTlsbug());
            if(!tlsbugmap2.containsKey(info.getTlsbug())){
                tlsbugmap2.put(info.getTlsbug(),new ArrayList<tlstaskInfo>());
                tlsbugmap2.get(info.getTlsbug()).add(info);
            }else{
                tlsbugmap2.get(info.getTlsbug()).add(info);
            }
        }
        List<String> labels=new ArrayList<>();
        for(String key:tlsbugmap1.keySet()){
            if(!labels.contains(key))
                labels.add(key);
        }
        for(String key:tlsbugmap2.keySet()){
            if(!labels.contains(key))
                labels.add(key);
        }
        List<Integer> data1=new ArrayList<>();
        List<Integer> data2=new ArrayList<>();
        for(String key:labels){
            if(tlsbugmap1.containsKey(key))
                data1.add(tlsbugmap1.get(key).size());
            else
                data1.add(0);
        }
        for(String key:labels){
            //System.out.println(tlsbugmap2.get(key).size());
            if(tlsbugmap2.containsKey(key))
                data2.add(tlsbugmap2.get(key).size());
            else
                data2.add(0);
        }
        compareEntity compareEntity=new compareEntity();
        compareEntity.data1=data1;
        compareEntity.data2=data2;
        compareEntity.labels=labels;
        return compareEntity;
    }


}
