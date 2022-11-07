package OnlyToActiveMq;


import portscan.AllPortLzr;
import portscan.PortScan;
import queue.ActivemqConfig;
import redis.RedisLock;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.json.JSONObject;
import portscan.ALLPortScan;

import javax.jms.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class IPConsumer {
    private RedisLock redisLock = new RedisLock();
    //    public static ActivemqConfig activemqConfig=new ActivemqConfig("/Config");
    //public static ActivemqConfig activemqConfig = new ActivemqConfig("/home/ysf/桌面/Project_service/Config");
    public static ActivemqConfig activemqConfig = new ActivemqConfig("/Config");
    public void getIP() throws Exception {
        String lockname = "IPQueue";
        String task = "";
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://" + activemqConfig.ActiveMqIP + ":61617");
        File file=new File("/home/ysf/China_IP_Device");
        String line="";
        InputStreamReader inputStreamReader=new InputStreamReader(new FileInputStream(file),"UTF-8");
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        redisLock.lock(10, "IPQueue", true);
        while((line=bufferedReader.readLine())!=null){
            System.out.println(line);
            try {
                String portResult = "{\"name\":\"" + "China_IP_Device" + "\"," + "\"target\":\"" + line + "\",\"desc\":\"\",\"date\":\"2022-03-12\",\"excuteexploit\":\"No\",\"excutetlstest\":\"No\",\"allportscan\":\"No\"}";
                Connection storeconnection = activeMQConnectionFactory.createConnection();
                storeconnection.start();
                Session storesession = storeconnection.createSession(true, Session.AUTO_ACKNOWLEDGE);

                Queue storequeue = storesession.createQueue("IPQueue");
                MessageProducer storeproducer = storesession.createProducer(storequeue);
                TextMessage message = storesession.createTextMessage(portResult);
                storeproducer.send(message);
                storesession.commit();
                storesession.close();
                storeproducer.close();
                storeconnection.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        redisLock.unlock("IPQueue");
//        try {
//
//            Connection connection = activeMQConnectionFactory.createConnection();
//            connection.start();
//            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//            Destination destination = session.createQueue("protocolQueue");
//            MessageConsumer consumer = session.createConsumer(destination);
//            try {
//                redisLock.lock(10, lockname, true);
//                TextMessage msg = (TextMessage) consumer.receive(1000);
//                redisLock.unlock(lockname);
//                if (msg != null)
//                    task = msg.getText();
//                    System.out.println(task);
//            } catch (Exception e) {
//                System.out.println("No data");
//            }
//        } catch (Exception e) {
//            System.out.println("No start ActiveMq");
//            e.printStackTrace();
//        }
    }

    public static void main(String[] argv) throws Exception {

//        System.out.println(Pattern.matches(".*/.*", "166.254.194.0/24"));
//        System.out.println("{\"name\":\"" + 1 + "\"," + "\"target\":\"" + "129.226.10.238" + "\"," + "\"desc\":\"" + "desc" + "\"," + "\"date\":\"" + "date" + "\",\"port\":\"" + "80" + "\"," +
//                "\"excuteexploit\":\"" + "Yes" + "\"," +
//                "\"excutetlstest\":\"" + "Yes"
//                + "\"}");
        OnlyToActiveMq.IPConsumer consumer = new OnlyToActiveMq.IPConsumer();
        consumer.getIP();

    }
}
