package queue;

import database.ExploitRecord;
import database.Record;
import database.RecordFactory;
import exploit.Generalmetasploit;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.json.JSONObject;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.RedisConfig;
import redis.RedisLock;
import exploit.metasploitWay;

import javax.jms.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class metasploitConsumer {
    private RedisLock redisLock = new RedisLock();
    //public static RedisConfig redisConfig=new RedisConfig("/Users/ludam/Desktop/IOTMark/src/main/java/queue/Config");
    public static ActivemqConfig activemqConfig = new ActivemqConfig("/Config");

    //public static scanConfig scanconfig=new scanConfig("/Users/ludam/Desktop/IOTMark/src/main/java/queue/Config");
    public void excuteExploit() {
        String lockname = "exploitQueue";
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://" + activemqConfig.ActiveMqIP + ":61617");
        List<String> generalProtocol = new ArrayList<>();
        generalProtocol.add("http");
        generalProtocol.add("telnet");
        generalProtocol.add("snmp");
        generalProtocol.add("ftp");
        generalProtocol.add("ssh");
        try {
            Connection connection = activeMQConnectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(lockname);
            MessageConsumer consumer = session.createConsumer(destination);
            TextMessage msg = null;
            try {
                redisLock.lock(10, lockname, true);
                msg = (TextMessage) consumer.receive(1000);
                redisLock.unlock(lockname);
            } catch (Exception e) {
                System.out.println("no data");
            }
            consumer.close();
            session.close();
            connection.close();
            System.out.println(msg.getText());
            //{"name":"Test","target":"58.64.190.210:21","desc":"","date":"2021-03-04","object":"ftp"}
            if (msg != null) {
                String task = msg.getText();
                JSONObject arr = (JSONObject) new JSONObject("{\"list\":" + task + "}").get("list");
                String taskname = arr.getString("name");
                String target = arr.getString("target");
                String desc = arr.getString("desc");
                String taskdate = arr.getString("date");
                String object = arr.getString("object");
                if (generalProtocol.contains(object.toLowerCase())) {
                    System.out.println("next ip");
                    /*try {
                        Generalmetasploit generalmetasploit = new Generalmetasploit();
                        List<String> exploitResult = generalmetasploit.excuteExploit(target, object);
                        Record record = RecordFactory.get(ExploitRecord.class);
                        for (String result : exploitResult) {
                            System.out.println(result);
                            JSONObject t = (JSONObject) new JSONObject("{\"list\":" + result + "}").get("list");
                            String exploitRes = t.getString("result");
                            String module = t.getString("module");
                            record.addRecord(target, exploitRes, module, taskname, taskdate, desc, object);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                } else {
                    try {
                        System.out.println("123456");
                        metasploitWay metasploitWay = new metasploitWay();
                        List<String> exploitResult = metasploitWay.excuteExploit(target, object);
                        Record record = RecordFactory.get(ExploitRecord.class);
                        //record.addRecord(target, "exploitRes", "module", taskname, taskdate, desc, object);
                        for (String result : exploitResult) {
                            System.out.println("*********" + result);
                            JSONObject t = (JSONObject) new JSONObject("{\"list\":" + result + "}").get("list");
                            String exploitRes = t.getString("result");
                            String module = t.getString("module");
                            record.addRecord(target, exploitRes, module, taskname, taskdate, desc, object);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] argv) {
        metasploitConsumer metasploitconsumer = new metasploitConsumer();
        metasploitconsumer.excuteExploit();
    }
}
