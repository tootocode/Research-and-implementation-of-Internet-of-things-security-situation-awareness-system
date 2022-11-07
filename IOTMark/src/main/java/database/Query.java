package database;

import java.util.List;

public interface Query {
    public boolean query(String querystr,String table,String KeyValue);
    public List<String> query(String Vendor, String DeviceType, String Device, String table);
    public List<String> query(String table,String Value);
    public void addRecord(String Devicetype, String Vendor, String Device);
}
