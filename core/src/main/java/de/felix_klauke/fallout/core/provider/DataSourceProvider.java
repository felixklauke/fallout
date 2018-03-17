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
            PreparedStatement preparedStatement = connection.prepareStatement("create table if not exists fallout_kingdoms (\n" +
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

            preparedStatement = connection.prepareStatement("create table if not exists fallout_land_holdings\n" +
                    "(\n" +
                    "  id             int auto_increment\n" +
                    "    primary key,\n" +
                    "  world          varchar(255) not null,\n" +
                    "  posX           int          not null,\n" +
                    "  posZ           int          not null,\n" +
                    "  kindomUniqueId varchar(36)  not null\n" +
                    ")\n" +
                    "  engine = InnoDB");
            preparedStatement.executeUpdate();
            preparedStatement.close();

            preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS fallout_kingdom_members\n" +
                    "(\n" +
                    "    id int PRIMARY KEY AUTO_INCREMENT,\n" +
                    "    uniqueId VARCHAR(36) NOT NULL,\n" +
                    "    kingdomUniqueId VARCHAR(36),\n" +
                    "    rankId int\n" +
                    ");");
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
