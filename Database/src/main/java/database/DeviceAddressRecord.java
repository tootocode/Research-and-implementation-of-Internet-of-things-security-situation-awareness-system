package database;

import org.json.JSONObject;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DeviceAddressRecord extends AbstractRecord {
    DeviceAddressRecord(Connection connection) throws SQLException {
        super(connection);
    }

    public void addRecord(String IP,String country,String province,String city){
        String table="IOTaddress";
        synchronized (connection){
            String sql="SELECT table_name FROM information_schema.TABLES WHERE table_name='"+table+"';";

            try {
                Statement statement=this.connection.createStatement();
                ResultSet set=statement.executeQuery(sql);
                System.out.println(set.next());
                if(!set.next()){
                    this.connection.createStatement().executeUpdate("CREATE TABLE if NOT EXISTS "+table+" (IP text," +
                            "country text," +
                            "province text," +
                            "city text);");
                }
                sql="SELECT * FROM "+table+" WHERE IP='"+IP+"';";
                ResultSet resultSet=connection.createStatement().executeQuery(sql);
                System.out.println(resultSet.next());
                if(!resultSet.next()){

                    PreparedStatement ptmt=connection.prepareStatement("INSERT INTO "+table+"(IP,country,province,city) values (?,?,?,?);");

                    ptmt.setString(1,IP);
                    ptmt.setString(2,country);
                    ptmt.setString(3,province);
                    ptmt.setString(4,city);

                    ptmt.executeUpdate();
                    ptmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
