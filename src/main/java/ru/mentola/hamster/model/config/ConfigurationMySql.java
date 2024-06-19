package ru.mentola.hamster.model.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.mentola.annotatedconfigmodel.api.AbstractConfigModel;
import ru.mentola.annotatedconfigmodel.api.ConfigurationPath;

@AllArgsConstructor @Getter
public final class ConfigurationMySql extends AbstractConfigModel {
    @ConfigurationPath(path = "my-sql.host")
    private final String host;
    @ConfigurationPath(path = "my-sql.port")
    private final int port;
    @ConfigurationPath(path = "my-sql.pool-size")
    private final int poolSize;
    @ConfigurationPath(path = "my-sql.credentials.username")
    private final String username;
    @ConfigurationPath(path = "my-sql.credentials.password")
    private final String password;
    @ConfigurationPath(path = "my-sql.name")
    private final String name;

    public String getJdbcUrl() {
        return String.format("jdbc:mysql://%s:%s/%s", this.getHost(), this.getPort(), this.getName());
    }
}
