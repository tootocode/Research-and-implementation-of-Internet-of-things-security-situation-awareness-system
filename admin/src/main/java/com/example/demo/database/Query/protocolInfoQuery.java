package com.example.demo.database.Query;

import com.example.demo.entity.InfoEntity;
import com.example.demo.entity.protocolInfo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class protocolInfoQuery extends AbstractQuery{
    public protocolInfoQuery(Connection connection) throws Exception{
        super(connection);
    }

    public List<protocolInfo> queryRecord(String sql){
        List<protocolInfo> list=new ArrayList<>();
        synchronized (this.connection){
            try{
                ResultSet resultSet = connection.createStatement().executeQuery(sql);
                while(resultSet.next()){
                    protocolInfo protocolInfo=new protocolInfo();
                    protocolInfo.setProtocol(resultSet.getString("protocol"));
                    protocolInfo.setDesc(resultSet.getString("des"));
                    list.add(protocolInfo);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return list;
    }
}
