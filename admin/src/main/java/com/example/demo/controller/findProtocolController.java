package com.example.demo.controller;

import com.example.demo.database.Query.protocoltaskQuery;
import com.example.demo.database.Record.DatabaseConfig;
import com.example.demo.entity.protocoltaskInfo;
import com.example.demo.queue.ActivemqConfig;
import com.example.demo.redis.RedisConfig;
import com.example.demo.redis.RedisLock;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.*;

import javax.jms.*;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@EnableConfigurationProperties({ActivemqConfig.class})
public class findProtocolController {

    @Autowired(required = false)
    private ActivemqConfig activemqConfig;

    @Autowired
    private DatabaseConfig databaseConfig;
    @Autowired
    private RedisConfig redisConfig;

    @GetMapping("/findprotocol")
    public String postdata(String name,String target,String desc,String date,String excuteexploit,String excutetlstest){

        RedisLock redisLock=new RedisLock(redisConfig.getRedisip());
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory("tcp://"+activemqConfig.getActiveMqIP()+":61617");
        String Queue="IPQueue";
        String task="{\"name\":\""+name+"\","+"\"target\":\""+target+"\","+"\"desc\":\""+desc+"\","+"\"date\":\""+date+"\","+
                "\"excuteexploit\":\""+excuteexploit+"\","+
                "\"excutetlstest\":\""+excutetlstest+
                "\"}";
        System.out.println(task);
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

    @GetMapping("/searchprotocolresult")
    public List<protocoltaskInfo> getdata(String name, String target, String protocol) throws SQLException {
        if(name.equals("")&&target.equals("")&&protocol.equals(""))return new ArrayList<>();
        System.out.println("name:"+name);
        System.out.println("target:"+target);
        System.out.println("protocol:"+protocol);
        String sql="select * from findProtocol where ";
        if(!name.equals(""))sql=sql+"taskname='"+name+"'";
        if(!name.equals("")&&!target.equals(""))sql=sql+" and IP='"+target+"'";
        else if(name.equals("")&&!target.equals(""))sql=sql+"IP='"+target+"'";
        if((!name.equals("")||!target.equals(""))&&!protocol.equals(""))sql=sql+" and protocol='"+protocol+"'";
        else if(name.equals("")&&target.equals(""))sql=sql+"protocol='"+protocol+"'";
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        java.sql.Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        protocoltaskQuery protocoltaskQuery=new protocoltaskQuery(connection);
        List<protocoltaskInfo> result=protocoltaskQuery.queryRecord(sql);
        connection.close();
        return result;
    }

    @GetMapping("/searchProtocollist")
    public List<protocoltaskInfo> getInitData() throws SQLException {
        String sql="select * from findProtocol";
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        java.sql.Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        protocoltaskQuery protocoltaskQuery=new protocoltaskQuery(connection);
        List<protocoltaskInfo> result=protocoltaskQuery.queryRecord(sql);
        connection.close();
        System.out.println(result);
        return result;
    }
}
