package com.example.demo.controller;

import com.example.demo.database.Query.countNumberQuery;
import com.example.demo.database.Query.listQuery;
import com.example.demo.database.Record.DatabaseConfig;
import com.example.demo.entity.bugInfo;
import com.example.demo.entity.listInfo;
import com.example.demo.queue.ActivemqConfig;
import com.example.demo.redis.RedisConfig;
import com.example.demo.redis.RedisLock;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

@RestController
public class Dashboard {

    @Autowired
    private DatabaseConfig databaseConfig;

    @Autowired(required = false)
    private ActivemqConfig activemqConfig;

    @Autowired
    private RedisConfig redisConfig;

    @GetMapping("/hello")
    public String hello(){
        System.out.println("hello1");
        return "Hello";
    }

    @GetMapping("/ScanIPV4")
    public String ScanIP(String name,String target,String desc,String date){
        String excuteexploit="Yes";
        String excutetlstest="Yes";
        String task="{\"name\":\""+name+"\","+"\"target\":\""+target+"\","+"\"desc\":\""+desc+"\","+"\"date\":\""+date+"\","+
                "\"excuteexploit\":\""+excuteexploit+"\","+
                "\"excutetlstest\":\""+excutetlstest+
                "\"}";
        RedisLock redisLock=new RedisLock(redisConfig.getRedisip());
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory("tcp://"+activemqConfig.getActiveMqIP()+":61617");
        String Queue="IPQueue";
        System.out.println(task);
        try{
            javax.jms.Connection connection= activeMQConnectionFactory.createConnection();
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
        return "sucess";
    }

    @GetMapping("/dashboard/finddevice")
    public int getfindDevice() throws SQLException {
        int res=0;
        String sql="select count(*) as number from IOTMark";
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        countNumberQuery countNumberQuery=new countNumberQuery(connection);
        res=countNumberQuery.queryRecord(sql);
        connection.close();
        //System.out.println(res);
        return res;
    }

    @GetMapping("/dashboard/findprotocol")
    public int getfindprotocl() throws SQLException {
        int res=0;
        String sql="select count(*) as number from findProtocol";
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        countNumberQuery countNumberQuery=new countNumberQuery(connection);
        res=countNumberQuery.queryRecord(sql);
        //System.out.println(res);
        connection.close();
        return res;
    }

    @GetMapping("/dashboard/findIPs")
    public int getfindIps() throws SQLException {
        int res=0;
        String sql="select count(*) as number from ScanedIP";
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        countNumberQuery countNumberQuery=new countNumberQuery(connection);
        res=countNumberQuery.queryRecord(sql);
        //System.out.println(res);
        connection.close();
        return res;
    }

    @GetMapping("/dashboard/findBugs")
    public int getfindBugs() throws SQLException {
        int res=0;
        String sql="select count(*) as number from ALLVulTable where ";
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
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
        countNumberQuery countNumberQuery=new countNumberQuery(connection);
        for(String bug :tlsbugs){
            String tempsql=sql+bug+"='1'";
            res=res+countNumberQuery.queryRecord(tempsql);
        }
        //System.out.println(res);
        connection.close();
        return res;
    }

    @GetMapping("/dashboard/devicelist")
    public List<listInfo> getDevicetypeRank() throws SQLException {
        List<listInfo> list=new ArrayList<>();
        String sql="select DeviceType,count(IOTMark.IP) as num from IOTMark group by DeviceType order by num desc";
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        listQuery listQuery=new listQuery(connection);
        list=listQuery.queryRecord(sql,"DeviceType");
        return list;
    }

    @GetMapping("/dashboard/protocollist")
    public List<listInfo> getProtocolRank() throws SQLException {
        List<listInfo> list=new ArrayList<>();
        String sql="select protocol,count(findProtocol.IP) as num from findProtocol group by protocol order by num desc";
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        listQuery listQuery=new listQuery(connection);
        list=listQuery.queryRecord(sql,"protocol");
        connection.close();
        return list;
    }

    @GetMapping("/dashboard/buglist")
    public List<listInfo> getBugRank() throws SQLException {
        List<listInfo> list=new ArrayList<>();
        List<String> tlsbugs=new ArrayList<>();
        List<bugInfo> temp=new ArrayList<>();
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
        String sql="select count(*) as number from ALLVulTable where ";
        String mysqlPath="jdbc:mysql://"+databaseConfig.getMysqlip()+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        Connection connection= DriverManager.getConnection(mysqlPath,"root","123456");
        countNumberQuery countNumberQuery=new countNumberQuery(connection);
        for(String bug :tlsbugs){
            String tempsql=sql+bug+"='1'";
            int t=countNumberQuery.queryRecord(tempsql);
            bugInfo bugInfo=new bugInfo();
            bugInfo.setName(bug);
            bugInfo.setNum(t);
            temp.add(bugInfo);
        }

        Collections.sort(temp, new Comparator<bugInfo>() {
            @Override
            public int compare(bugInfo o1, bugInfo o2) {
                return o2.getNum()-o1.getNum();
            }
        });

        int i=0;

        while(i<temp.size()&&i<10){
            listInfo listInfo=new listInfo();
            listInfo.setRank(i+1);
            listInfo.setName(temp.get(i).getName());
            listInfo.setNum(temp.get(i).getNum());
            list.add(listInfo);
            i++;
        }
        //System.out.println(list);
        return list;
    }

}
