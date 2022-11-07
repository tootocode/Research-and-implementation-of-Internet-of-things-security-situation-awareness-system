package database;

import java.sql.*;

public class RuleInsert extends AbstractQuery{
    RuleInsert(Connection connection) throws SQLException {
        super(connection);

        synchronized (this.connection){
            String table="Device";
            String sql="SELECT * FROM sqlite_master WHERE name='"+table+"';";
            Statement statement = this.connection.createStatement();
            ResultSet set = statement.executeQuery(sql);
            if (!set.next()) {
                this.connection.createStatement()
                        .executeUpdate("CREATE TABLE if NOT EXISTS Device(DeviceType varchar(20)," +
                                "Vendor varchar(20)," +
                                "Device varchar(100)" +
                                ");" );
            }
        }
    }

    public void addRecord(String Vendor,String Devicetype,String Device){
        synchronized (connection){
            String sql="select * from Device where Vendor='"+Vendor+"'"+"and DeviceType='"+Devicetype+"'"+"and Device='"+Device+"';";

            System.out.println(Vendor);
            System.out.println(Devicetype);
            try {
                ResultSet resultSet=connection.createStatement().executeQuery(sql);

                if(!resultSet.next()){
                    PreparedStatement ptmt=connection.prepareStatement("insert into Device(" +
                            "DeviceType,Vendor,Device)" +
                            "values (?,?,?);");

                    ptmt.setString(1,Devicetype);
                    ptmt.setString(2,Vendor);
                    ptmt.setString(3,Device);
                    ptmt.executeUpdate();
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
