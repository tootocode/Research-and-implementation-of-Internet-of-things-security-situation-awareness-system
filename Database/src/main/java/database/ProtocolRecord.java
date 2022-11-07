package database;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.SQLException;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ProtocolRecord extends AbstractRecord {
    ProtocolRecord(Connection connection) throws SQLException {
        super(connection);
    }

    public void addRecord(String ProtocolInfo){
        String table="findProtocol";
        JSONObject arr=(JSONObject) new JSONObject("{\"list\":"+ProtocolInfo+"}").get("list");
        String taskname=arr.getString("name");
        String taskdesc=arr.getString("desc");
        String taskdate=arr.getString("date");
        String target=arr.getString("target");
        JSONObject temp=(JSONObject) new JSONObject("{\"list\":"+arr.get("markres")+"}").get("list");
        temp=(JSONObject) new JSONObject("{\"list\":"+temp.get("iotMarkResult")+"}").get("list");
        String IP=temp.getString("ip");
        String protocol=temp.getString("Protocol");
        String info=ProtocolInfo;

        String sql="SELECT table_name FROM information_schema.TABLES WHERE table_name='"+table+"';";
        try {
            Statement statement=this.connection.createStatement();
            ResultSet set=statement.executeQuery(sql);
            if(!set.next()){
                this.connection.createStatement().executeUpdate("CREATE TABLE if NOT EXISTS "+table+" (IP text," +
                        "protocol text," +
                        "taskname text," +
                        "taskdes text," +
                        "taskdate text," +
                        "Raw TEXT," +
                        "Insert_date DATE " +
                        ");");
            }
            sql="SELECT * FROM "+table+" WHERE IP='"+IP+"' and Protocol='"+protocol+"' and taskname='"+taskname+"'"+
                    "and taskdate='"+taskdate+"'";
            ResultSet resultSet=connection.createStatement().executeQuery(sql);
            if(!resultSet.next()){
                PreparedStatement ptmt=connection.prepareStatement("INSERT INTO "+table+"(IP,protocol,taskname,taskdes,taskdate,Raw,Insert_date) values (?,?,?,?,?,?,?)");

                ptmt.setString(1,IP);
                ptmt.setString(2,protocol);
                ptmt.setString(3,taskname);
                ptmt.setString(4,taskdesc);
                ptmt.setString(5,taskdate);
                ptmt.setString(6,info);
                java.util.Date d=new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String idate = sdf.format(d);
                ptmt.setString(7,idate);
                ptmt.executeUpdate();
                ptmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

