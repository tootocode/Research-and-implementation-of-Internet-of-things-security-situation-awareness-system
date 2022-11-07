package com.example.demo.database.Query;

import com.example.demo.entity.InfoEntity;
import com.example.demo.entity.TLSInfo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class tlsbugInfoQuery extends AbstractQuery{
    public tlsbugInfoQuery(Connection connection) throws SQLException {
        super(connection);
    }

    public List<TLSInfo> queryRecord(String sql){
        List<TLSInfo> list=new ArrayList<>();
        synchronized (this.connection){
            try{
                ResultSet resultSet = connection.createStatement().executeQuery(sql);
                while(resultSet.next()){
                    TLSInfo tlsInfo=new TLSInfo();
                    tlsInfo.setDesc(resultSet.getString("des"));
                    tlsInfo.setTlsname(resultSet.getString("tls"));
                    list.add(tlsInfo);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return list;
    }
}
