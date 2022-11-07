package database;

public interface Record {
    void addRecord(String markRes);

    void addRecord(String ip, String country, String province, String city);
}
