package de.felix_klauke.fallout.core.provider;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.felix_klauke.fallout.core.config.FalloutCoreConfig;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class DataSourceProvider implements Provider<DataSource> {

    private final HikariDataSource dataSource;

    @Inject
    public DataSourceProvider(FalloutCoreConfig coreConfig) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(coreConfig.getJdbcUrl());
        hikariConfig.setUsername(coreConfig.getDatabaseUser());
        hikariConfig.setPassword(coreConfig.getDatabasePassword());
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(hikariConfig);

        createTables();
    }

    private void createTables() {
        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("create table fallout_kingdoms (\n" +
                    "  id          int auto_increment primary key,\n" +
                    "  uniqueId    varchar(36)  not null,\n" +
                    "  name        varchar(16)  not null,\n" +
                    "  description varchar(255) null,\n" +
                    "  constraint fallout_kingdoms_id_uindex\n" +
                    "  unique (id),\n" +
                    "  constraint fallout_kingdoms_uniqueId_uindex\n" +
                    "  unique (uniqueId),\n" +
                    "  constraint fallout_kingdoms_name_uindex\n" +
                    "  unique (name)\n" +
                    ") engine = InnoDB;");
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public DataSource get() {
        return dataSource;
    }
}
