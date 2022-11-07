package com.example.demo.database.Query;

import com.example.demo.entity.devicetaskInfo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class devicetypeAnalyseQuery extends AbstractQuery{
    devicetypeAnalyseQuery(Connection connection) throws SQLException {
        super(connection);
    }
    public List<devicetaskInfo> queryRecord(String sql){
        List<devicetaskInfo> list=new ArrayList<>();
        synchronized (this.connection){
            try{
                ResultSet resultSet = connection.createStatement().executeQuery(sql);
                while(resultSet.next()){
                    devicetaskInfo devicetaskInfo=new devicetaskInfo();
                    devicetaskInfo.setDevice(resultSet.getString("Device"));
                    devicetaskInfo.setVendor(resultSet.getString("Vendor"));
                    devicetaskInfo.setDevicetype(resultSet.getString("DeviceType"));
                    devicetaskInfo.setDes(resultSet.getString("taskdes"));
                    devicetaskInfo.setPostdate(resultSet.getString("taskdate"));
                    devicetaskInfo.setTaskname(resultSet.getString("taskname"));
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
