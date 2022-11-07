package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class QueryFactory {
    private static Map<Class<?extends AbstractQuery>, Query> QueryMap=new HashMap<>();
    static {
         String sqlitePath="/home/ysf/Project_service/IOTMark/DeviceMark.sqlite";
//        String sqlitePath="/DeviceMark.sqlite";
        try {
            Connection connection= DriverManager.getConnection("jdbc:sqlite:"+sqlitePath);
            QueryMap.put(GeneralQuery.class,new GeneralQuery(connection));
            QueryMap.put(DeviceQuery.class,new DeviceQuery(connection));
            QueryMap.put(GeneralQuerys.class,new GeneralQuerys(connection));
            QueryMap.put(RuleInsert.class,new RuleInsert(connection));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static Query get(Class<? extends AbstractQuery>clazz){
        return QueryMap.get(clazz);
    }
}
