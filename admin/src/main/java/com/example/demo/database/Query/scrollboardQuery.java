package com.example.demo.database.Query;

import com.example.demo.entity.devicetaskInfo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class scrollboardQuery extends AbstractQuery{
    public scrollboardQuery(Connection connection) throws SQLException {
        super(connection);
    }

    public List<devicetaskInfo> queryRecord() throws SQLException {
        List<devicetaskInfo> res = new ArrayList<>();
        synchronized (this.connection){
            String sql = "SELECT * From IOTMark order by taskdate desc ";
            int count = 0;
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            while (resultSet.next()&&count<10) {
                    devicetaskInfo devicetaskInfo=new devicetaskInfo();
                    devicetaskInfo.setDevice(resultSet.getString("Device"));
                    devicetaskInfo.setDevicetype(resultSet.getString("DeviceType"));
                    devicetaskInfo.setVendor(resultSet.getString("Vendor"));
                    devicetaskInfo.setTarget(resultSet.getString("IP"));
                    res.add(devicetaskInfo);
                    count++;
            }
        }
        return res;
    }
}
