package com.example.demo.database.Query;

import com.example.demo.entity.deviceInfo;
import com.example.demo.entity.tlstaskInfo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class tlstaskQuery extends AbstractQuery{
    public tlstaskQuery(Connection connection) throws SQLException {
        super(connection);
    }
    public List<tlstaskInfo> queryRecord(String sql,String tlsbug){
        List<tlstaskInfo> list=new ArrayList<>();
        synchronized (this.connection){
            try{
                ResultSet resultSet = connection.createStatement().executeQuery(sql);
                while(resultSet.next()){
                    tlstaskInfo tlstaskInfo=new tlstaskInfo();
                    tlstaskInfo.setTlsbug(tlsbug);
                    tlstaskInfo.setPostdate(resultSet.getString("taskdate"));
                    tlstaskInfo.setTaskname(resultSet.getString("taskname"));
                    tlstaskInfo.setTarget(resultSet.getString("host"));
                    list.add(tlstaskInfo);
                }
                resultSet.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return list;
    }
}
