package queue;

import BannerInfo.IotMarkEntrance;
import database.Query;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.json.JSONObject;
import redis.RedisLock;
import sun.font.TextRecord;
import javax.jms.*;

public class IPConsumer {
    private RedisLock redisLock = new RedisLock();
    //    public static ActivemqConfig activemqConfig=new ActivemqConfig("/Config");
//    public static RedisConfig redisConfig=new RedisConfig("/Users/ludam/Desktop/IOTMark/src/main/java/queue/Config");
    public static ActivemqConfig activemqConfig = new ActivemqConfig("/Config");

    public void getIp() {
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://" + activemqConfig.ActiveMqIP + ":61617");
        System.out.println(activemqConfig.ActiveMqIP);
        String name = "";
        String target = "";
        String desc = "";
        String date = "";
        String port = "";
        String Queue = "PortQueue";
        String task = "";
        String excuteexploit = "";
        String excutetlstest = "";
        String tlstest = "";
        String exploit1 = "";
        String exploit2 = "";
        try {
            Connection connection = activeMQConnectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(Queue);
            MessageConsumer messageConsumer = session.createConsumer(destination);
            try {
                redisLock.lock(10, Queue, true);
                TextMessage msg = (TextMessage) messageConsumer.receive(1000);
                if (msg != null)
                    task = msg.getText();
            } catch (Exception e) {
                System.out.println("No data");
            }
            redisLock.unlock(Queue);
            System.out.println(task);
            messageConsumer.close();
            session.close();
            connection.close();
            JSONObject arr = (JSONObject) new JSONObject("{\"list\":" + task + "}").get("list");
            name = arr.getString("name");
            target = arr.getString("target");
            desc = arr.getString("desc");
            date = arr.getString("date");
            port = arr.getString("port");
            excuteexploit = arr.getString("excuteexploit");
            excutetlstest = arr.getString("excutetlstest");
            System.out.println(name + " " + target + " " + desc + " " + date + " " + port);
            IotMarkEntrance iotMarkEntrance = new IotMarkEntrance();
            String markQueue = "markQueue";
            String protocolQueue = "protocolQueue";
            String IpToScan = target + ":" + port;
            System.out.println("Start Mark:" + IpToScan);

            String markres = iotMarkEntrance.StartIotMark(IpToScan);
            System.out.println(markres);

            arr = (JSONObject) new JSONObject("{\"list\":" + markres + "}").get("list");
            JSONObject markJudge = (JSONObject) new JSONObject("{\"list\":" + arr.get("iotMarkResult") + "}").get("list");
            String isMarked = (String) markJudge.get("Marked");
            String protocol = markJudge.getString("Protocol");
            String vendor = "";
            try {
                vendor = markJudge.getString("Vendor");
            } catch (Exception e) {
                System.out.println("标记失败");
            }
            tlstest = "{\"name\":\"" + name + "\"," + "\"target\":\"" + IpToScan + "\"," + "\"desc\":\"" + desc + "\"," + "\"date\":\"" + date + "\"}";
            exploit1 = "{\"name\":\"" + name + "\"," + "\"target\":\"" + IpToScan + "\"," + "\"desc\":\"" + desc + "\"," + "\"date\":\"" + date + "\",\"object\":\"" + vendor + "\"}";
            exploit2 = "{\"name\":\"" + name + "\"," + "\"target\":\"" + IpToScan + "\"," + "\"desc\":\"" + desc + "\"," + "\"date\":\"" + date + "\",\"object\":\"" + protocol + "\"}";
            if (isMarked.equals("true")) {
                String StoreMark = "{\"name\":\"" + name + "\"," + "\"target\":\"" + target + "\"," + "\"desc\":\"" + desc + "\"," + "\"date\":\"" + date + "\",\"markres\":" + markres +
                        "}";
                redisLock.lock(10, markQueue, true);
                connection = activeMQConnectionFactory.createConnection();
                connection.start();
                session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
                Queue MarkQueue = session.createQueue(markQueue);
                MessageProducer messageProducer = session.createProducer(MarkQueue);
                TextMessage textMessage = session.createTextMessage(StoreMark);
                messageProducer.send(textMessage);
                session.commit();
                session.close();
                messageProducer.close();
                connection.close();
                redisLock.unlock(markQueue);
            }
            String StoreProtocol = "{\"name\":\"" + name + "\"," + "\"target\":\"" + target + "\"," + "\"desc\":\"" + desc + "\"," + "\"date\":\"" + date + "\",\"markres\":" + markres +
                    "}";
            redisLock.lock(10, protocolQueue, true);
            connection = activeMQConnectionFactory.createConnection();
            connection.start();
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            Queue ProtocolQueue = session.createQueue(protocolQueue);
            MessageProducer messageProducer = session.createProducer(ProtocolQueue);
            TextMessage textMessage = session.createTextMessage(StoreProtocol);
            messageProducer.send(textMessage);
            session.commit();
            session.close();
            messageProducer.close();
            connection.close();
            redisLock.unlock(protocolQueue);
            if (excutetlstest.equals("Yes")) {
                redisLock.lock(10, "TLSTestQueue", true);
                connection = activeMQConnectionFactory.createConnection();
                connection.start();
                session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
                Queue TLSTestQueue = session.createQueue("TLSTestQueue");
                MessageProducer tlstestmessageProducer = session.createProducer(TLSTestQueue);
                TextMessage tlstesttextMessage = session.createTextMessage(tlstest);
                tlstestmessageProducer.send(tlstesttextMessage);
                session.commit();
                session.close();
                tlstestmessageProducer.close();
                connection.close();
                redisLock.unlock("TLSTestQueue");
            }
            if (excuteexploit.equals("Yes")) {
                redisLock.lock(10, "exploitQueue", true);
                connection = activeMQConnectionFactory.createConnection();
                connection.start();
                session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
                Queue exploitQueue = session.createQueue("exploitQueue");
                MessageProducer exploitmessageProducer = session.createProducer(exploitQueue);
                TextMessage exploittextMessage1 = session.createTextMessage(exploit1);
                TextMessage exploittextMessage2 = session.createTextMessage(exploit2);
                exploitmessageProducer.send(exploittextMessage1);
                exploitmessageProducer.send(exploittextMessage2);
                session.commit();
                session.close();
                exploitmessageProducer.close();
                connection.close();
                redisLock.unlock("exploitQueue");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] argv) {
        IPConsumer consumer = new IPConsumer();
        consumer.getIp();
    }
}
