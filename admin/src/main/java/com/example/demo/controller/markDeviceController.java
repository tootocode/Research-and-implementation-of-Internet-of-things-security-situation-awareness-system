package com.example.demo.controller;

import com.example.demo.database.Query.devicemarktaskQuery;
import com.example.demo.database.Record.DatabaseConfig;
import com.example.demo.entity.devicetaskInfo;
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
public class markDeviceController {
    @Autowired
    private DatabaseConfig databaseConfig;

    @Autowired(required = false)
    private ActivemqConfig activemqConfig;
    @Autowired
    private RedisConfig redisConfig;
    @GetMapping("/markdevice")
    public String postdata(String name,String target,String desc,String date){
        RedisLock redisLock=new RedisLock(redisConfig.getRedisip());
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory("tcp://"+activemqConfig.getActiveMqIP()+":61617");
        String Queue="IPQueue";
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

    @GetMapping("/searchdeviceresult")
    public List<devicetaskInfo> getdata(String name, String target, String deviceType, String device, String vendor) throws SQLException {
        List<devicetaskInfo> list=new ArrayList<>();

        String sql="select * from IOTMark where ";
        if(!name.equals(""))sql=sql+"taskname='"+name+"' ";
        if(!target.equals("")){
            if(!name.equals("")) sql=sql+"and IP='"+target+"' ";
            else sql=sql+"IP='"+target+"' ";
        }
        if(!deviceType.equals("")){
            if(!name.equals("")||!target.equals("")) sql=sql+"and DeviceType='"+deviceType+"' ";
            else sql=sql+"DeviceType='"+deviceType+"' ";
        }
        if(!device.equals("")){
            if(!name.equals("")||!target.equals("")||deviceType.equals("")) sql=sql+"and Device='"+device+"' ";
            else sql=sql+"Device='"+device+"' ";
        }
        if(!vendor.equals("")){
            if(!name.equals("")||!target.equals("")||deviceType.equals("")||device.equals(""))
                sql=sql+"and Vendor='"+vendor+"'";
            else
                sql=sql+"Vendor='"+vendor+"'";

        }
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        java.sql.Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        devicemarktaskQuery devicemarktaskQuery=new devicemarktaskQuery(connection);
        list=devicemarktaskQuery.queryRecord(sql);
        connection.close();
        return list;
    }

    @GetMapping("/searchmarkdevicelist")
    public List<devicetaskInfo> getInitList() throws SQLException {
        List<devicetaskInfo> list=new ArrayList<>();
        String sql="select * from IOTMark";
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        java.sql.Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        devicemarktaskQuery devicemarktaskQuery=new devicemarktaskQuery(connection);
        list=devicemarktaskQuery.queryRecord(sql);
        connection.close();
        return list;
    }
}
