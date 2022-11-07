package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GeneralQuery extends AbstractQuery{
    GeneralQuery(Connection connection) throws SQLException {
        super(connection);
    }

    public boolean query(String querystr,String table,String Keyvalue){
        synchronized (this.connection){             //同步锁
            String sql="SELECT name FROM sqlite_master WHERE type='table' AND name='"+table+"'";
            try{
                Statement statement=this.connection.createStatement();
                ResultSet set=statement.executeQuery(sql);
                if(!set.next()){
                    return false;
                }
                sql="SELECT * FROM "+table+" where "+Keyvalue+"='"+querystr+"';";
                ResultSet resultSet=connection.createStatement().executeQuery(sql);
                if(resultSet.next()){
                    return true;
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
            return false;
        }
    }

}
