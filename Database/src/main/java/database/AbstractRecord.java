package database;

import java.sql.Connection;
import java.sql.SQLException;

public class AbstractRecord implements Record {
    protected final Connection connection;
    AbstractRecord(Connection connection) throws SQLException {
        this.connection=connection;
    }

    @Override
    public void addRecord(String markRes) {

    }

    @Override
    public void addRecord(String IP,String country,String province,String city){

    }
}
