package com.example.demo.database.Query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class countNumberQuery extends AbstractQuery{
    public countNumberQuery(Connection connection) throws SQLException {
        super(connection);
    }
    public int queryRecord(String sql) throws SQLException {
        int res=0;
        synchronized (this.connection){
            ResultSet resultSet=connection.createStatement().executeQuery(sql);
            if(resultSet.next())
                res=resultSet.getInt("number");
        }
        return res;
    }
}
