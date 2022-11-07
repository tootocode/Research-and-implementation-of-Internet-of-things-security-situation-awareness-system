package database;

import org.json.JSONObject;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DeviceIPRecord extends AbstractRecord {
    DeviceIPRecord(Connection connection) throws SQLException {
        super(connection);
    }

    public void addRecord(String markRes){
        String table="IOTMark";
        JSONObject arr=(JSONObject) new JSONObject("{\"list\":"+markRes+"}").get("list");
        String taskname=arr.getString("name");
        //{"name":"Test","target":"129.2.27.68","desc":"","date":"2021-02-28","markres":{"iotMarkResult":{"ip":"129.2.27.68:44818","DeviceType":"Programmable Logic Controller","Vendor":"Rockwell Automation/Allen-Bradley","Device":"l1756-L61/BLOGIX5561","Protocol":"Ethernet/IP","Marked":"true"},"Type":"Iot"}}
        String target=arr.getString("target");
        String desc=arr.getString("desc");
        String date=arr.getString("date");
        JSONObject temp=(JSONObject) new JSONObject("{\"list\":"+arr.get("markres")+"}").get("list");
        temp=(JSONObject) new JSONObject("{\"list\":"+temp.get("iotMarkResult")+"}").get("list");
        String IP=temp.getString("ip");
        String vendor=(String) temp.get("Vendor");
        String device=temp.getString("Device");
        String deviceType=temp.getString("DeviceType");
        synchronized (connection){
            String sql="SELECT table_name FROM information_schema.TABLES WHERE table_name='"+table+"';";
            try {
                Statement statement=this.connection.createStatement();
                ResultSet set=statement.executeQuery(sql);
                if(!set.next()){
                    this.connection.createStatement().executeUpdate("CREATE TABLE if NOT EXISTS "+table+" (IP text," +
                            "DeviceType text," +
                            "Vendor text," +
                            "taskname text," +
                            "taskdes text," +
                            "taskdate text," +
                            "Insert_date DATE," +
                            "Device text);");
                }
                sql="SELECT * FROM "+table+" WHERE IP='"+IP+"' and taskname='"+taskname+"'"+" and taskdate='"+date+"'";
                ResultSet resultSet=connection.createStatement().executeQuery(sql);
                if(!resultSet.next()){
                    PreparedStatement ptmt=connection.prepareStatement("INSERT INTO "+table+"(IP,DeviceType,Vendor,taskname,taskdes,taskdate,Device,Insert_date) values (?,?,?,?,?,?,?,?)");

                    ptmt.setString(1,IP);
                    ptmt.setString(2,deviceType);
                    ptmt.setString(3,vendor);
                    ptmt.setString(4,taskname);
                    ptmt.setString(5,desc);
                    ptmt.setString(6,date);
                    ptmt.setString(7,device);
                    java.util.Date d=new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String idate = sdf.format(d);
                    ptmt.setString(8,idate);
                    ptmt.executeUpdate();
                    ptmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
