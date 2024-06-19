package ru.mentola.hamster.pool;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jetbrains.annotations.Nullable;
import ru.mentola.hamster.model.config.ConfigurationMySql;

import java.sql.Connection;
import java.sql.SQLException;

public final class ConnectionSqlPool {
    private final HikariConfig config;
    private HikariDataSource source;

    public ConnectionSqlPool(final ConfigurationMySql model) {
        this.config = new HikariConfig();
        this.config.setJdbcUrl(model.getJdbcUrl());
        this.config.setUsername(model.getUsername());
        this.config.setPassword(model.getPassword());
        this.config.setMaximumPoolSize(model.getPoolSize());
    }

    public void connect() {
        this.source = new HikariDataSource(this.config);
    }

    public void disconnect() {
        if (this.source != null)
            this.source.close();
    }

    @Nullable
    public Connection getConnection() throws SQLException {
        if (this.source != null)
            return source.getConnection();
        return null;
    }
}
