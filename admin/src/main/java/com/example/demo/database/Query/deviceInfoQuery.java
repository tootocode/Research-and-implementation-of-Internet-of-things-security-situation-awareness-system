package com.example.demo.database.Query;

import com.example.demo.entity.InfoEntity;
import com.example.demo.entity.deviceInfo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class deviceInfoQuery extends AbstractQuery{
    public deviceInfoQuery(Connection connection) throws SQLException {
        super(connection);
    }

    public List<deviceInfo> queryRecord(String sql){
        List<deviceInfo> list=new ArrayList<>();
        synchronized (this.connection){
            try{
                ResultSet resultSet = connection.createStatement().executeQuery(sql);
                while(resultSet.next()){
                    deviceInfo deviceInfo=new deviceInfo();
                    deviceInfo.setDevice(resultSet.getString("device"));
                    deviceInfo.setDevicetype(resultSet.getString("devicetype"));
                    deviceInfo.setVendor(resultSet.getString("vendor"));
                    list.add(deviceInfo);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return list;
    }
}
