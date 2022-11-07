package database.Query;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

abstract class AbstractQuery implements Query{
    protected final Connection connection;
    AbstractQuery(Connection connection) throws SQLException {
        this.connection=connection;
    }

}
