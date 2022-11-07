package com.example.demo.controller;

import com.example.demo.database.Query.tlstaskQuery;
import com.example.demo.database.Record.DatabaseConfig;
import com.example.demo.entity.tlstaskInfo;
import com.example.demo.queue.ActivemqConfig;
import com.example.demo.redis.RedisConfig;
import com.example.demo.redis.RedisLock;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class tlsTestController {

    @Autowired
    private DatabaseConfig databaseConfig;

    @Autowired(required = false)
    private ActivemqConfig activemqConfig;
    @Autowired
    private RedisConfig redisConfig;
    @GetMapping("/tlstest")
    public String postdata(String name,String target,String desc,String date){
        RedisLock redisLock=new RedisLock(redisConfig.getRedisip());
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory("tcp://"+activemqConfig.getActiveMqIP()+":61617");
        String Queue="TLSTestQueue";
        String task="{\"name\":\""+name+"\","+"\"target\":\""+target+"\","+"\"desc\":\""+desc+"\","+"\"date\":\""+date+"\"}";
        try{
            Connection connection= activeMQConnectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            redisLock.lock(10,Queue,true);
            javax.jms.Queue queue=session.createQueue(Queue);
            MessageProducer producer=session.createProducer(queue);
            TextMessage textMessage=session.createTextMessage(task);
            producer.send(textMessage);
            session.commit();
            session.close();
            producer.close();
            connection.close();
            redisLock.unlock(Queue);
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(task);
        return "success";
    }

    @GetMapping("/searchtlstestresult")
    public List<tlstaskInfo> getdata(String name, String target, String tlsbug) throws SQLException {
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
        List<tlstaskInfo> list=new ArrayList<>();
        String sql1="select * from ALLVulTable where ";
        if(!name.equals(""))sql1=sql1+" taskname='"+name+"'";
        if(!target.equals("")){
            if(!name.equals(""))
                sql1=sql1+" and host='"+target+"'";
            else
                sql1=sql1+" host='"+target+"'";
        }

        if(!tlsbug.equals("")){
            if(!name.equals("")||!target.equals(""))
                sql1=sql1+" and "+tlsbug+"='1'";
            else
                sql1=sql1+tlsbug+"='1'";
        }

        if(!tlsbug.equals("")){
            String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            java.sql.Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
            tlstaskQuery  tlstaskQuery=new tlstaskQuery(connection);
            System.out.println(sql1);
            list=tlstaskQuery.queryRecord(sql1,tlsbug);
            connection.close();
            return list;
        }else{
            String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            java.sql.Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
            tlstaskQuery  tlstaskQuery=new tlstaskQuery(connection);
            for(String bug:tlsbugs){
                List<tlstaskInfo> temp=new ArrayList<>();
                String tempsql=sql1+" and "+bug+"='1'";
                System.out.println(sql1);
                temp=tlstaskQuery.queryRecord(tempsql,bug);
                list.addAll(temp);
            }
            connection.close();
        }
        String sql2="select * from CurveSwapFinalTable where ";
        if(!name.equals(""))sql2=sql2+" taskname='"+name+"'";
        if(!target.equals("")){
            if(!name.equals(""))
                sql2=sql2+" and host='"+target+"'";
            else
                sql2=sql2+" host='"+target+"'";
        }
        if(tlsbug.equals("")){
            sql2=sql2+" and (InvalidCurveSwap='1' or TwistCurveSwap='1')";
            String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            java.sql.Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
            tlstaskQuery  tlstaskQuery=new tlstaskQuery(connection);
            System.out.println(sql2);
            list.addAll(tlstaskQuery.queryRecord(sql2,"curveswap"));
            connection.close();
        }
        return list;
    }

    @GetMapping("/gettlsresultlist")
    public List<tlstaskInfo> getInitdata() throws SQLException {
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
        List<tlstaskInfo> list=new ArrayList<>();
        String sql1="select * from ALLVulTable where ";
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        java.sql.Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        tlstaskQuery  tlstaskQuery=new tlstaskQuery(connection);
        for(String bug:tlsbugs){
            String tempsql=sql1+bug+"='1'";
            List<tlstaskInfo> temp=new ArrayList<>();
            temp=tlstaskQuery.queryRecord(tempsql,bug);
            list.addAll(temp);
        }
        String sql2="select * from CurveSwapFinalTable where InvalidCurveSwap='1' or TwistCurveSwap='1'";
        list.addAll(tlstaskQuery.queryRecord(sql2,"curveswap"));
        connection.close();
        return list;
    }
}
