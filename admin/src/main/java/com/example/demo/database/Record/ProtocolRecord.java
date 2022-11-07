package com.example.demo.database.Record;

import java.sql.*;

public class ProtocolRecord extends AbstractRecord{
    ProtocolRecord(Connection connection) throws SQLException {
        super(connection);
    }
    public void addRecord(String protocol,String desc){
        String table="protocolInfo";
        synchronized (connection){
            String sql="SELECT table_name FROM information_schema.TABLES WHERE table_name='"+table+"';";
            Statement statement= null;
            try {
                statement = this.connection.createStatement();
                ResultSet set=statement.executeQuery(sql);
                if(!set.next()){
                    this.connection.createStatement().executeUpdate("CREATE TABLE if NOT EXISTS "+table+" (protocol text,des text);");
                }
                sql="SELECT * from "+table+" where protocol='"+protocol+"';";
                ResultSet resultSet=connection.createStatement().executeQuery(sql);
                if(!resultSet.next()){
                    PreparedStatement ptmt=connection.prepareStatement("insert into "+table+"(protocol,des) values (?,?)");
                    ptmt.setString(1,protocol);
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
