package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeviceQuery extends AbstractQuery{
    DeviceQuery(Connection connection) throws SQLException {
        super(connection);
    }
    @Override
    public boolean query(String querystr, String table, String KeyValue) {
        return false;
    }

    public List<String> query(String Vendor, String DeviceType, String Device, String table){
        List<String> returnValue=new ArrayList<>();
        if(!Device.equals(Vendor)||!Device.equals(DeviceType)){
            String sql="SELECT * FROM "+table+" WHERE Device='"+Device+"'";
            synchronized (this.connection){
                try{
                    ResultSet resultSet=connection.createStatement().executeQuery(sql);
                    if(resultSet.next()){
                        String vendor=resultSet.getString("Vendor");
                        String deviceType=resultSet.getString("DeviceType");
                        String device=resultSet.getString("Device");
                        returnValue.add(vendor);
                        returnValue.add(deviceType);
                        returnValue.add(device);
                    }
                }catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return returnValue;
    }
}
