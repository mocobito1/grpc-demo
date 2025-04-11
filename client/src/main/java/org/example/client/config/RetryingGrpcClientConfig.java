package org.example.client.config;

import com.example.BookAuthorServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.client.interceptor.RetryLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
public class RetryingGrpcClientConfig {

    @Bean
    public ManagedChannel managedChannel() {
        return ManagedChannelBuilder.forAddress("localhost", 9000)
                .usePlaintext()
                .intercept(new RetryLoggingInterceptor())
                .enableRetry()
                .defaultServiceConfig(Map.of(
                        "methodConfig", List.of(Map.of(
                                "name", List.of(Map.of("service", "com.example.BookAuthorService")),
                                "retryPolicy", Map.of(
                                        "maxAttempts", 4.0,
                                        "initialBackoff", "0.5s",
                                        "maxBackoff", "s",
                                        "backoffMultiplier", 2.0,
                                        "retryableStatusCodes", List.of("UNAVAILABLE", "DEADLINE_EXCEEDED")
                                )
                        ))
                ))
                .build();
    }

    @Bean
    public BookAuthorServiceGrpc.BookAuthorServiceBlockingStub bookAuthorBlockingStub(ManagedChannel channel) {
        return BookAuthorServiceGrpc.newBlockingStub(channel)
                .withInterceptors(new RetryLoggingInterceptor());
    }
}
