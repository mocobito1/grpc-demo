package org.example.client.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "grpc.server")
@Getter
@Setter
public class GrpcServerConfig {
    private String host;
    private int port;
}
