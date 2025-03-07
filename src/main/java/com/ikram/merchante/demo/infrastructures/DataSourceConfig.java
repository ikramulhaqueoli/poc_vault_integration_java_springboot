package com.ikram.merchante.demo.infrastructures;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private static HikariDataSource dataSource;

    @Bean
    public DataSource dataSource() {
        String postgresHost = VaultCredentialsManager.getCredential("me-test-postgresql-db:host-url");
        String postgresDbName = VaultCredentialsManager.getCredential("me-test-postgresql-db:db-name");
        String postgresUrl = String.format("jdbc:postgresql://%s/%s", postgresHost, postgresDbName);
        String username = VaultCredentialsManager.getCredential("me-test-postgresql-db:username");
        String password = VaultCredentialsManager.getCredential("me-test-postgresql-db:password");

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(postgresUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName("org.postgresql.Driver");

        return dataSource = new HikariDataSource(config);
    }

    public static void refreshCredentials() {
        String username = VaultCredentialsManager.getCredential("me-test-postgresql-db:username");
        String password = VaultCredentialsManager.getCredential("me-test-postgresql-db:password");
        dataSource.getHikariConfigMXBean().setUsername(username);
        dataSource.getHikariConfigMXBean().setPassword(password);
        dataSource.getHikariPoolMXBean().softEvictConnections();
    }
}
