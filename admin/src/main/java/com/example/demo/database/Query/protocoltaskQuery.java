package com.example.demo.database.Query;

import com.example.demo.entity.protocolInfo;
import com.example.demo.entity.protocoltaskInfo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class protocoltaskQuery extends AbstractQuery{
    public protocoltaskQuery(Connection connection) throws SQLException {
        super(connection);
    }
    public List<protocoltaskInfo> queryRecord(String sql){
        List<protocoltaskInfo> list=new ArrayList<>();
        synchronized (this.connection){
            try{
                ResultSet resultSet = connection.createStatement().executeQuery(sql);
                while(resultSet.next()){
                    protocoltaskInfo protocoltaskInfo=new protocoltaskInfo();
                    protocoltaskInfo.setProtocol(resultSet.getString("protocol"));
                    protocoltaskInfo.setDes(resultSet.getString("taskdes"));
                    protocoltaskInfo.setPostdate(resultSet.getString("taskdate"));
                    protocoltaskInfo.setRaw(resultSet.getString("Raw"));
                    protocoltaskInfo.setTarget(resultSet.getString("IP"));
                    protocoltaskInfo.setTaskname(resultSet.getString("taskname"));
                    list.add(protocoltaskInfo);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return list;
    }
}
