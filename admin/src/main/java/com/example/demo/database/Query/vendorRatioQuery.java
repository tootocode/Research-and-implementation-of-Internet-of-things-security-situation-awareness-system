package com.example.demo.database.Query;

import com.example.demo.entity.vendorratio;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class vendorRatioQuery extends AbstractQuery{
    public vendorRatioQuery(Connection connection) throws SQLException {
        super(connection);
    }

    public vendorratio queryRecord(String sql,String devicetype,int devicenum) throws SQLException {
        vendorratio result=new vendorratio();
        synchronized (this.connection){
            ResultSet resultSet=connection.createStatement().executeQuery(sql);
            int i=0;
            while(i<1&&resultSet.next()){
                vendorratio vendorratio=new vendorratio();
                vendorratio.setDevicenum(devicenum);
                vendorratio.setDeviceType(devicetype);
                vendorratio.setVendor(resultSet.getString("Vendor"));
                vendorratio.setVendornum(resultSet.getInt("num"));
                result=vendorratio;
                i++;
            }
        }
        return result;
    }
}
