package de.felix_klauke.fallout.core.provider;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import de.felix_klauke.fallout.core.config.FalloutCoreConfig;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;

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
    }

    @Override
    public DataSource get() {
        return dataSource;
    }
}
