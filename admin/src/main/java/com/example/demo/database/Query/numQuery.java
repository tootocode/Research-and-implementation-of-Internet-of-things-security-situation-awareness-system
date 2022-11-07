package com.example.demo.database.Query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class numQuery extends AbstractQuery{
    public numQuery(Connection connection) throws SQLException {
        super(connection);
    }

    public int queryRecord(String sql) throws SQLException {
        int result=0;
        synchronized (this.connection){
            ResultSet resultSet=connection.createStatement().executeQuery(sql);
            if(resultSet.next()){
                result=resultSet.getInt("num");
            }
        }
        return result;
    }
}
