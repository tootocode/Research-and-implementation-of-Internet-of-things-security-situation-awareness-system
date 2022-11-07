package database;

import org.json.JSONObject;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IPRecord extends AbstractRecord {
    IPRecord(Connection connection) throws SQLException {
        super(connection);
    }
    public void addRecord(String PortInfo){
        String table="ScanedIP";
        JSONObject arr=(JSONObject) new JSONObject("{\"list\":"+PortInfo+"}").get("list");
        String taskname=arr.getString("name");
        String taskdesc=arr.getString("desc");
        String taskdate=arr.getString("date");
        String IP=arr.getString("target");
        synchronized (connection){
            String sql="SELECT table_name FROM information_schema.TABLES WHERE table_name='"+table+"';";
            Statement statement= null;
            try {
                statement = this.connection.createStatement();
                ResultSet set=statement.executeQuery(sql);
                if(!set.next()){
                    this.connection.createStatement().executeUpdate("CREATE TABLE if NOT EXISTS "+table+" (IP varchar (100)," +
                            "taskname text,"+
                            "taskdesc text,"+
                            "taskdate text,"+
                            "Insert_date DATE "+
                            ");");
                }
                sql="SELECT * from "+table+" where IP='"+IP+"' and taskname='"+taskname+"'"+" and taskdate='"+taskdate+"'"+";";
                ResultSet resultSet=connection.createStatement().executeQuery(sql);
                if(!resultSet.next()){
                    PreparedStatement ptmt=connection.prepareStatement("insert into "+table+"(IP,taskname,taskdesc,taskdate,Insert_date) values (?,?,?,?,?)");
                    ptmt.setString(1,IP);
                    ptmt.setString(2,taskname);
                    ptmt.setString(3,taskdesc);
                    ptmt.setString(4,taskdate);
                    java.util.Date d=new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String idate = sdf.format(d);
                    ptmt.setString(5,idate);
                    ptmt.executeUpdate();
                    ptmt.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
