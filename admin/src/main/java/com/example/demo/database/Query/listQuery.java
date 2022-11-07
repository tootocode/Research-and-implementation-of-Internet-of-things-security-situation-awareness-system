package com.example.demo.database.Query;

import com.example.demo.entity.listInfo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class listQuery extends AbstractQuery{
    public listQuery(Connection connection) throws SQLException {
        super(connection);
    }
    public List<listInfo> queryRecord(String sql,String type) throws SQLException {
        List<listInfo> list=new ArrayList<>();
        synchronized (this.connection){
            ResultSet resultSet=connection.createStatement().executeQuery(sql);
            int i=1;
            while (resultSet.next()&&i<=10){
                listInfo listInfo=new listInfo();
                listInfo.setRank(i);
                listInfo.setName(resultSet.getString(type));
                listInfo.setNum(resultSet.getInt("num"));
                list.add(listInfo);
                i++;
            }
        }
        return list;
    }
}
