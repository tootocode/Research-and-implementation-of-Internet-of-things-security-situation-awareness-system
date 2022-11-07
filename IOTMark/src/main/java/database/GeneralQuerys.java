package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class GeneralQuerys extends AbstractQuery{
    GeneralQuerys(Connection connection) throws SQLException {
        super(connection);
    }
    public List<String> query(String table, String Value){
        List<String> result=new ArrayList<>();
        synchronized (this.connection){
            String sql="SELECT name FROM sqlite_master WHERE type='table' AND name='"+table+"';";
            try {
                Statement statement=connection.createStatement();
                ResultSet set=statement.executeQuery(sql);
                if(!set.next()){
                    return result;
                }
                sql="SELECT * FROM "+table;
                ResultSet resultSet=connection.createStatement().executeQuery(sql);
                while(resultSet.next()){
                    String ip=resultSet.getString(Value);
                    result.add(ip);
                }
                set.close();
                resultSet.close();
                statement.close();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
