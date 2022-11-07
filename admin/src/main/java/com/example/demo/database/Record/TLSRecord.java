package com.example.demo.database.Record;

import java.sql.*;

public class TLSRecord extends AbstractRecord{
    TLSRecord(Connection connection) throws SQLException {
        super(connection);
    }
    public void addRecord(String tls,String desc){
        String table="tlsInfo";
        synchronized (connection){
            String sql="SELECT table_name FROM information_schema.TABLES WHERE table_name='"+table+"';";
            Statement statement= null;
            try {
                statement = this.connection.createStatement();
                ResultSet set=statement.executeQuery(sql);
                if(!set.next()){
                    this.connection.createStatement().executeUpdate("CREATE TABLE if NOT EXISTS "+table+" (tls text,des text);");
                }
                sql="SELECT * from "+table+" where tls='"+tls+"';";
                ResultSet resultSet=connection.createStatement().executeQuery(sql);
                if(!resultSet.next()){
                    PreparedStatement ptmt=connection.prepareStatement("insert into "+table+"(tls,des) values (?,?)");
                    ptmt.setString(1,tls);
                    ptmt.setString(2,desc);
                    ptmt.executeUpdate();
                    ptmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
