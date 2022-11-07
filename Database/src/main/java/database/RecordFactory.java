package database;

import queue.ActivemqConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

public class RecordFactory {
    public static DataBaseConfig dataBaseConfig=new DataBaseConfig("/Config");
//    public static DataBaseConfig dataBaseConfig=new DataBaseConfig("/Config");
    private static Map<Class<? extends AbstractRecord>,Record> RecordMap=new HashMap<Class<? extends AbstractRecord>, Record>();
    static {
        String driver = "com.mysql.cj.jdbc.Driver";
        try {
            Class.forName(driver);
            String mysqlPath="jdbc:mysql://"+dataBaseConfig.DataBaseIP+"/DeviceMark?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            Connection connectionmysql= DriverManager.getConnection(mysqlPath,"root","123456");
            RecordMap.put(DeviceIPRecord.class,new DeviceIPRecord(connectionmysql));
            RecordMap.put(IPRecord.class,new IPRecord(connectionmysql));
            RecordMap.put(ProtocolRecord.class,new ProtocolRecord(connectionmysql));
            RecordMap.put(DeviceAddressRecord.class,new DeviceAddressRecord(connectionmysql));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Record get(Class<? extends AbstractRecord> clazz){
        return RecordMap.get(clazz);
    }
}
