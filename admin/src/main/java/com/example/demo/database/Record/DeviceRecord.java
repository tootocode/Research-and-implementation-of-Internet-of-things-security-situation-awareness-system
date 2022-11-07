package com.example.demo.database.Record;

import java.sql.*;

public class DeviceRecord extends AbstractRecord{
    DeviceRecord(Connection connection) throws SQLException {
        super(connection);
    }
    public void addRecord(String vendor,String device,String devicetype){
        String table="deviceInfo";
        String table2="devicetypetable";
        String table3="vendortable";
        synchronized (connection){
            String sql="SELECT table_name FROM information_schema.TABLES WHERE table_name='"+table+"';";
            Statement statement= null;
            try {
                statement = this.connection.createStatement();
                ResultSet set=statement.executeQuery(sql);
                if(!set.next()){
                    this.connection.createStatement().executeUpdate("CREATE TABLE if NOT EXISTS "+table+" (device text,vendor text,devicetype text);");
                }
                sql="SELECT * from "+table+" where device='"+device+"';";
                ResultSet resultSet=connection.createStatement().executeQuery(sql);
                if(!resultSet.next()){
                    PreparedStatement ptmt=connection.prepareStatement("insert into "+table+"(device,vendor,devicetype) values (?,?,?)");
                    ptmt.setString(1,device);
                    ptmt.setString(2,vendor);
                    ptmt.setString(3,devicetype);
                    ptmt.executeUpdate();
                    ptmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            sql="SELECT table_name FROM information_schema.TABLES WHERE table_name='"+table2+"';";
            try {
                statement = this.connection.createStatement();
                ResultSet set=statement.executeQuery(sql);
                if(!set.next()){
                    this.connection.createStatement().executeUpdate("CREATE TABLE if NOT EXISTS "+table2+" (vendor text);");
                }
                sql="SELECT * from "+table2+" where vendor='"+vendor+"';";
                ResultSet resultSet=connection.createStatement().executeQuery(sql);
                if(!resultSet.next()){
                    PreparedStatement ptmt=connection.prepareStatement("insert into "+table2+"(vendor) values (?)");
                    ptmt.setString(1,vendor);
                    ptmt.executeUpdate();
                    ptmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            sql="SELECT table_name FROM information_schema.TABLES WHERE table_name='"+table3+"';";
            try {
                statement = this.connection.createStatement();
                ResultSet set=statement.executeQuery(sql);
                if(!set.next()){
                    this.connection.createStatement().executeUpdate("CREATE TABLE if NOT EXISTS "+table3+" (devicetype text);");
                }
                sql="SELECT * from "+table3+" where devicetype='"+vendor+"';";
                ResultSet resultSet=connection.createStatement().executeQuery(sql);
                if(!resultSet.next()){
                    PreparedStatement ptmt=connection.prepareStatement("insert into "+table3+"(devicetype) values (?)");
                    ptmt.setString(1,devicetype);
                    ptmt.executeUpdate();
                    ptmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
