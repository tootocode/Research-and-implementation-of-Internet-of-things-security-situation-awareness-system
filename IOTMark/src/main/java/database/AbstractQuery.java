package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AbstractQuery implements Query{
    protected final Connection connection;
    AbstractQuery(Connection connection) throws SQLException {
        this.connection=connection;
    }
    public boolean query(String querystr, String table, String KeyValue) {
        return false;
    }

    @Override
    public List<String> query(String Vendor, String DeviceType, String Device, String table) {
        return null;
    }
    public List<String> query(String table,String Value){
        return null;
    }

    public void addRecord(String Devicetype, String Vendor, String Device) {
    }
}
