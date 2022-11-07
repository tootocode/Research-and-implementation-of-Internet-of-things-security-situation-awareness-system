package queue;

import portscan.AllPortLzr;
import portscan.PortScan;
import redis.RedisLock;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.json.JSONObject;
import portscan.ALLPortScan;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class IPConsumer {
    private RedisLock redisLock = new RedisLock();
    //    public static ActivemqConfig activemqConfig=new ActivemqConfig("/Config");
    //public static ActivemqConfig activemqConfig = new ActivemqConfig("/home/ysf/桌面/Project_service/Config");
    public static ActivemqConfig activemqConfig = new ActivemqConfig("/Config");
    public void getIP() {
        String task = "";
        String name = "";
        String target = "";
        String desc = "";
        String date = "";
        String excuteexploit = "";
        String excutetlstest = "";
        String allportlzr = "No";
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://" + activemqConfig.ActiveMqIP + ":61617");
        String lockname = "IPQueue";
        List<String> ports = new ArrayList<String>();
        ports.add("80");
        ports.add("47808");
        ports.add("2455");
        ports.add("789");
        ports.add("2222");
        ports.add("20000");
        ports.add("44818");
        ports.add("1911");
        ports.add("21");
        ports.add("18245");
        ports.add("2404");
        ports.add("5007");
        ports.add("4800");
        ports.add("1962");
        ports.add("20547");
        ports.add("102");
        ports.add("23");
        ports.add("22");
        ports.add("554");
        ports.add("161");
        ports.add("3702");
        ports.add("9100");
        ports.add("443");
        ports.add("8080");
        ports.add("9600");
        ports.add("502");
        ports.add("8443");
        PortScan portScan = new PortScan();
        ALLPortScan allPortScan = new ALLPortScan();
        AllPortLzr allportlzrobject = new AllPortLzr();
        try {

            Connection connection = activeMQConnectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("IPQueue");
            MessageConsumer consumer = session.createConsumer(destination);
            try {
                redisLock.lock(10, lockname, true);
                TextMessage msg = (TextMessage) consumer.receive(1000);
                redisLock.unlock(lockname);
                if (msg != null)
                    task = msg.getText();
            } catch (Exception e) {
                System.out.println("No data");
            }
            System.out.println(task);
            System.out.println("{\"list\":" + task + "}");
            consumer.close();
            session.close();
            connection.close();
            JSONObject arr = (JSONObject) new JSONObject("{\"list\":" + task + "}").get("list");
            name = arr.getString("name");
            target = arr.getString("target");
            desc = arr.getString("desc");
            date = arr.getString("date");
            excuteexploit = arr.getString("excuteexploit");
            excutetlstest = arr.getString("excutetlstest");
            allportlzr = arr.getString("allportscan");
            System.out.println(name + " " + target + " " + desc + " " + date);
        } catch (Exception e) {
            System.out.println("No start ActiveMq");
            e.printStackTrace();
        }
        if (!target.equals("ALL")) {
            String locknameRedis = "PortQueue";
            System.out.println(target);
            if (!target.equals("")) {
                for (String port : ports) {
                    List<String> result = portScan.ScanPorts(port, target, "1000", "/home/" + port + "Portresult.csv");
//                List<String> result=portScan.ScanPorts(port,target,"100","/PortScan/"+port+"Portresult.csv");
                    for (String ip : result) {
                        try {
                            String portResult = "{\"name\":\"" + name + "\"," + "\"target\":\"" + ip + "\"," + "\"desc\":\"" + desc + "\"," + "\"date\":\"" + date + "\",\"port\":\"" + port + "\"," +
                                    "\"excuteexploit\":\"" + excuteexploit + "\"," +
                                    "\"excutetlstest\":\"" + excutetlstest
                                    + "\"}";
                            Connection connection = activeMQConnectionFactory.createConnection();
                            connection.start();
                            Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
                            redisLock.lock(10, locknameRedis, true);
                            javax.jms.Queue queue = session.createQueue(locknameRedis);
                            MessageProducer producer = session.createProducer(queue);
                            TextMessage textMessage = session.createTextMessage(portResult);
                            producer.send(textMessage);
                            session.commit();
                            session.close();
                            producer.close();
                            connection.close();
                            redisLock.unlock(locknameRedis);
                            try {
                                Connection storeconnection = activeMQConnectionFactory.createConnection();
                                storeconnection.start();
                                Session storesession = storeconnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
                                redisLock.lock(10, "ScanIPQueue", true);
                                Queue storequeue = storesession.createQueue("ScanIPQueue");
                                MessageProducer storeproducer = storesession.createProducer(storequeue);
                                TextMessage message = storesession.createTextMessage(portResult);
                                storeproducer.send(message);
                                storesession.commit();
                                storesession.close();
                                storeproducer.close();
                                storeconnection.close();
                                redisLock.unlock("ScanIPQueue");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String portResult = "{\"name\":\"" + name + "\"," + "\"target\":\"" + ip + "\"," + "\"desc\":\"" + desc + "\"," + "\"date\":\"" + date + "\",\"port\":\"" + port + "\"}";
                        try {
                            redisLock.unlock(locknameRedis);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if (target.equals("ALL")) {
            String locknameRedis = "PortQueue";
            for (String port : ports) {
                List<String> result = allPortScan.ScanPorts(port, target, "1000", "/home/" + port + "ALLIPV4result.csv");
//                List<String> result=portScan.ScanPorts(port,target,"100","/PortScan/"+port+"Portresult.csv");
                for (String ip : result) {
                    try {
                        String portResult = "{\"name\":\"" + name + "\"," + "\"target\":\"" + ip + "\"," + "\"desc\":\"" + desc + "\"," + "\"date\":\"" + date + "\",\"port\":\"" + port + "\"," +
                                "\"excuteexploit\":\"" + excuteexploit + "\"," +
                                "\"excutetlstest\":\"" + excutetlstest
                                + "\"}";
                        Connection connection = activeMQConnectionFactory.createConnection();
                        connection.start();
                        Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
                        redisLock.lock(10, locknameRedis, true);
                        Queue queue = session.createQueue(locknameRedis);
                        MessageProducer producer = session.createProducer(queue);
                        TextMessage textMessage = session.createTextMessage(portResult);
                        producer.send(textMessage);
                        session.commit();
                        session.close();
                        producer.close();
                        connection.close();
                        redisLock.unlock(locknameRedis);
                        try {
                            Connection storeconnection = activeMQConnectionFactory.createConnection();
                            storeconnection.start();
                            Session storesession = storeconnection.createSession(true, Session.AUTO_ACKNOWLEDGE);
                            redisLock.lock(10, "ScanIPQueue", true);
                            Queue storequeue = storesession.createQueue("ScanIPQueue");
                            MessageProducer storeproducer = storesession.createProducer(storequeue);
                            TextMessage message = storesession.createTextMessage(portResult);
                            storeproducer.send(message);
                            storesession.commit();
                            storesession.close();
                            storeproducer.close();
                            storeconnection.close();
                            redisLock.unlock("ScanIPQueue");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String portResult = "{\"name\":\"" + name + "\"," + "\"target\":\"" + ip + "\"," + "\"desc\":\"" + desc + "\"," + "\"date\":\"" + date + "\",\"port\":\"" + port + "\"}";
                    try {
                        redisLock.unlock(locknameRedis);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }if (allportlzr.equals("Yes")) {
            String locknameRedis = "protocolQueue";
            List<String> result = allportlzrobject.ScanPorts(target);
//          List<String> result=portScan.ScanPorts(port,target,"100","/PortScan/"+port+"Portresult.csv");
            for (String ip : result) {
                try {
                    String[] mess = ip.split(":");
                    String portResult = "{\"name\":\"" + name + "\"," + "\"target\":\"" + mess[0] + "\"," + "\"desc\":\"" + desc + "\"," + "\"date\":\"" + date +
                            "\",\"markres\":{\"iotMarkResult\":{\"ip\":\"" +mess[0]+":"+mess[1]+ "\",\"Protocol\":\""+mess[2]+"\",\"Marked\":\"false\",\"raw\":\"\"},\"Type\":\"Protocol\"}}";
                    Connection connection = activeMQConnectionFactory.createConnection();
                    connection.start();
                    Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
                    redisLock.lock(10, locknameRedis, true);
                    Queue queue = session.createQueue(locknameRedis);
                    MessageProducer producer = session.createProducer(queue);
                    TextMessage textMessage = session.createTextMessage(portResult);
                    producer.send(textMessage);
                    session.commit();
                    session.close();
                    producer.close();
                    connection.close();
                    redisLock.unlock(locknameRedis);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //String portResult = "{\"name\":\"" + name + "\"," + "\"target\":\"" + ip + "\"," + "\"desc\":\"" + desc + "\"," + "\"date\":\"" + date + "\",\"port\":\"" + port + "\"}";
                try {
                    redisLock.unlock(locknameRedis);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] argv) {

//        System.out.println(Pattern.matches(".*/.*", "166.254.194.0/24"));
//        System.out.println("{\"name\":\"" + 1 + "\"," + "\"target\":\"" + "129.226.10.238" + "\"," + "\"desc\":\"" + "desc" + "\"," + "\"date\":\"" + "date" + "\",\"port\":\"" + "80" + "\"," +
//                "\"excuteexploit\":\"" + "Yes" + "\"," +
//                "\"excutetlstest\":\"" + "Yes"
//                + "\"}");
        IPConsumer consumer = new IPConsumer();
        consumer.getIP();

    }
}
