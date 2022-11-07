package com.example.demo.database.Record;

public interface Record {
    void addRecord(String markRes);

    void addRecord(String name, String desc);

    void addRecord(String vendor, String name, String devicetype);

    void addRecord(String name, String target, String bug, String desc);
}
