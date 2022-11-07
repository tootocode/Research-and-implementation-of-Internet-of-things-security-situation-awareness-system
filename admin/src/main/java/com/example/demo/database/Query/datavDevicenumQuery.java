package com.example.demo.database.Query;

import com.example.demo.entity.datavdevicenum;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class datavDevicenumQuery extends AbstractQuery{
    public datavDevicenumQuery(Connection connection) throws SQLException {
        super(connection);
    }

    public List<datavdevicenum> queryRecord(String sql) throws SQLException {
        List<datavdevicenum> list=new ArrayList<>();
        synchronized (this.connection){
            ResultSet resultSet=connection.createStatement().executeQuery(sql);
            int i=0;
            while(i<6&&resultSet.next()){
                datavdevicenum datavdevicenum=new datavdevicenum();
                datavdevicenum.setDeviceType(resultSet.getString("DeviceType"));
                datavdevicenum.setNum(resultSet.getInt("num"));
                list.add(datavdevicenum);
                i++;
            }
        }
        return list;
    }
}
