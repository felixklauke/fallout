package de.felix_klauke.fallout.core.config;

/**
 * @author Felix Klauke <info@felix-klauke.de>
 */
public class FalloutCoreConfig {

    private final String jdbcUrl;
    private final String databaseUser;
    private final String databasePassword;

    public FalloutCoreConfig(String jdbcUrl, String databaseUser, String databasePassword) {
        this.jdbcUrl = jdbcUrl;
        this.databaseUser = databaseUser;
        this.databasePassword = databasePassword;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }
}
