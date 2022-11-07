package com.example.demo.database.Query;

import com.example.demo.entity.datavdevicenum;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class datavdeviceQuery extends AbstractQuery{
    public datavdeviceQuery(Connection connection) throws SQLException {
        super(connection);
    }
    public List<datavdevicenum> queryRecord() throws SQLException {
        String sql="select DeviceType ,count(IOTMark.IP) as num from IOTMark group by DeviceType";
        List<datavdevicenum> result=new ArrayList<>();
        synchronized (this.connection){
            ResultSet resultSet=connection.createStatement().executeQuery(sql);
            while(resultSet.next()){
                datavdevicenum datavdevicenum=new datavdevicenum();
                datavdevicenum.setDeviceType(resultSet.getString("DeviceType"));
                datavdevicenum.setNum(resultSet.getInt("num"));
                result.add(datavdevicenum);
            }
        }
        return result;
    }
}
