package ru.medvedev.application.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "application.sftp")
public class SftpProperties {
    private String host;
    private Integer port;
    private String user;
    private String password;
    private String receiveDirectory;
}
