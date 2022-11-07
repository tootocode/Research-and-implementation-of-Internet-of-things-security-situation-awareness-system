package database.Query;

import database.entity.devicetaskInfo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class deviceaddressQuery extends AbstractQuery{
    public deviceaddressQuery(Connection connection) throws SQLException {
        super(connection);
    }
    public List<devicetaskInfo> queryRecord(String sql){
        List<devicetaskInfo> list=new ArrayList<>();

        synchronized (this.connection){
            try{
                ResultSet resultSet = connection.createStatement().executeQuery(sql);
                while(resultSet.next()){
                    devicetaskInfo devicetaskInfo=new devicetaskInfo();

                    devicetaskInfo.setTarget(resultSet.getString("IP"));

                    list.add(devicetaskInfo);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return list;
    }
}