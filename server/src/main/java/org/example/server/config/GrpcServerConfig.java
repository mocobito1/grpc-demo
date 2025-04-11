package org.example.server.config;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.protobuf.services.HealthStatusManager;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;
import org.example.server.service.BookAuthorServerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@Log4j2
public class GrpcServerConfig {

    private final BookAuthorServerService bookAuthorService;
    private final HealthStatusManager healthStatusManager = new HealthStatusManager();

    @Value("${grpc.server.port}")
    private int grpcPort;

    private Server server;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public GrpcServerConfig(BookAuthorServerService bookAuthorService) {
        this.bookAuthorService = bookAuthorService;
    }

    @PostConstruct
    public void startGrpcServer() {
        healthStatusManager.setStatus(BookAuthorServerService.class.getName(), HealthCheckResponse.ServingStatus.SERVING);

        server = ServerBuilder
                .forPort(grpcPort)
                .addService(bookAuthorService)
                .addService(healthStatusManager.getHealthService())
                .executor(Executors.newFixedThreadPool(2))
                .build();

        executorService.submit(() -> {
            try {
                log.info("Starting gRPC server on port {}", grpcPort);
                server.start();
                server.awaitTermination();
            } catch (IOException e) {
                log.error("IOException occurred while starting or running the gRPC server", e);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Thread was interrupted while waiting for gRPC server termination", e);
            }
        });
    }

    @PreDestroy
    public void stopGrpcServer() {
        if (server != null && !server.isShutdown()) {
            server.shutdown();
            log.info("gRPC server shut down successfully.");
        }
    }

    @PreDestroy
    public void stopExecutorService() {
        executorService.shutdownNow();
    }
}