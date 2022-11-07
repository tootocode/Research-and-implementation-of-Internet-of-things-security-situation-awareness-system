package queue;

import database.*;
import database.Query.deviceaddressQuery;
import database.entity.devicetaskInfo;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.RedisConfig;
import redis.RedisLock;

import javax.jms.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ConsumerMessage {
    private RedisLock redisLock = new RedisLock();
//    public static ActivemqConfig activemqConfig=new ActivemqConfig("/Config");

    //    public static RedisConfig redisConfig=new RedisConfig("/Users/ludam/Desktop/IOTMark/src/main/java/queue/Config");
    public static ActivemqConfig activemqConfig = new ActivemqConfig("/Config");
    public static DataBaseConfig dataBaseConfig = new DataBaseConfig("/Config");

    public void storeResult() {
        //ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://" + activemqConfig.ActiveMqIP + ":61617");
        // redisLock.lock(10, databaseLock, true);
        String IP = null;
        List<devicetaskInfo> list = new ArrayList<>();
        String sql = "select * from IOTMark";
        String mysqlPath = "jdbc:mysql://" + dataBaseConfig.DataBaseIP + "/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        java.sql.Connection connectionsql = null;
        deviceaddressQuery deviceaddressQueryQuery = null;
        try {
            connectionsql = DriverManager.getConnection(mysqlPath, "root", "123456");
            deviceaddressQueryQuery = new deviceaddressQuery(connectionsql);
            list =deviceaddressQueryQuery.queryRecord(sql);
            connectionsql.close();

        } catch(Exception e) {
            e.printStackTrace();
        }

        for(devicetaskInfo a:list){
            if (a.getTarget().contains(":")){
                IP = a.getTarget().split(":")[0];
            }
            else if (!a.getTarget().contains(":")){
                IP = a.getTarget();
            }
            String line = null;
            try{
                String cmd = "python3 /findaddr.py ";
                cmd = cmd + IP;

                final Process p = Runtime.getRuntime().exec(cmd);
//                System.out.println(cmd);
                BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

                try {
//                    while ((line = in.readLine()) != null) {
//
//                    }
                    line = in.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                p.waitFor();
                p.destroy();
            }catch (Exception e){
                e.printStackTrace();
            }
            try{
                String[] detials = line.split(":");
//              System.out.println(detials[0]+"/"+detials[1]+"/"+detials[2]);
                System.out.println(IP+detials[0]+detials[1]+detials[2]);
                Record record = RecordFactory.get(DeviceAddressRecord.class);
                record.addRecord(IP,detials[0],detials[1],detials[2]);
            }
            catch (Exception e){
                System.out.print("");
            }

        }


//        String sql1 = "create table tmp as (select distinct IP,province,country,city from IOTaddress);";
//        String sql2 = "delete from IOTaddress;";
//        String sql3 = "insert into IOTaddress select * from tmp;";
//        String sql4 = "drop table tmp;";
//        mysqlPath = "jdbc:mysql://" + dataBaseConfig.DataBaseIP + "/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
//        try {
//            connectionsql = DriverManager.getConnection(mysqlPath, "root", "123456");
//            synchronized (connectionsql) {
//                try{
//                    connectionsql.createStatement().executeUpdate(sql1);
//                    connectionsql.createStatement().executeUpdate(sql2);
//                    connectionsql.createStatement().executeUpdate(sql3);
//                    connectionsql.createStatement().executeUpdate(sql4);
//                }catch (SQLException e){
//                    e.printStackTrace();
//                }
//
//            }
//            connectionsql.close();
//
//        } catch(Exception e) {
//            e.printStackTrace();
//        }


//        try {
//            List<String> queueList = new ArrayList<>();
//            queueList.add("markQueue");
//            queueList.add("protocolQueue");
//            queueList.add("ScanIPQueue");
//            for (String q : queueList) {
//                Connection connection = activeMQConnectionFactory.createConnection();
//                connection.start();
//                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//                Destination destination = session.createQueue(q);
//                TextMessage msg = null;
//                MessageConsumer messageConsumer = session.createConsumer(destination);
//                try {
//                    redisLock.lock(10, q, true);
//                    msg = (TextMessage) messageConsumer.receive(1000);
//                    redisLock.unlock(q);
//                } catch (Exception e) {
//                    System.out.println("No data");
//
//                }
//
//                messageConsumer.close();
//                session.close();
//                connection.close();
//                String databaseLock = q + "database";
//                if (msg != null) {
//                    String message = msg.getText();
//                    System.out.println(message);
//                    redisLock.lock(10, databaseLock, true);
//                    if (q.equals("markQueue")) {
//                        Record record = RecordFactory.get(DeviceIPRecord.class);
//                        record.addRecord(message);
//                    } else if (q.equals("protocolQueue")) {
//                        Record record = RecordFactory.get(ProtocolRecord.class);
//                        record.addRecord(message);
//                    } else if (q.equals("ScanIPQueue")) {
//                        Record record = RecordFactory.get(IPRecord.class);
//                        record.addRecord(message);
//                    }
//                    redisLock.unlock(databaseLock);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static void main(String[] argv) throws JMSException {
        ConsumerMessage consumerMessage = new ConsumerMessage();
        consumerMessage.storeResult();

    }
}
