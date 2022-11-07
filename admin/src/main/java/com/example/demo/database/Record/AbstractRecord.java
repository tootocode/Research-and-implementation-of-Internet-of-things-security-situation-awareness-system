package com.example.demo.database.Record;

import java.sql.Connection;
import java.sql.SQLException;

public class AbstractRecord implements Record{
    protected final Connection connection;
    AbstractRecord(Connection connection) throws SQLException {
        this.connection=connection;
    }

    @Override
    public void addRecord(String markRes) {

    }

    @Override
    public void addRecord(String name, String desc) {

    }

    @Override
    public void addRecord(String vendor, String name, String devicetype) {

    }

    @Override
    public void addRecord(String name, String target, String bug, String desc) {

    }
}
