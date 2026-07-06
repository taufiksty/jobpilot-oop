package repository.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

public class MysqlRepositoryImpl {
    protected final DataSource dataSource;

    public MysqlRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected String getStringOrNull(ResultSet result, String column) {
        try {
            return result.getString(column);
        } catch (SQLException e) {
            return null;
        }
    }

    protected <T> T getObjectOrNull(ResultSet result, String column, Class<T> type) {
        try {
            return result.getObject(column, type);
        } catch (SQLException e) {
            return null;
        }
    }
}
