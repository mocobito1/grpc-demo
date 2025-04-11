package org.example.client.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.health.v1.HealthCheckRequest;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.health.v1.HealthGrpc;
import lombok.extern.slf4j.Slf4j;
import org.example.client.config.GrpcServerConfig;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GrpcHealthCheckService {

    private final ManagedChannel channel;
    private final HealthGrpc.HealthBlockingStub healthStub;


    public GrpcHealthCheckService() {
        this.channel = ManagedChannelBuilder.forAddress(GrpcServerConfig.HOST, GrpcServerConfig.PORT)
                .usePlaintext()
                .build();
        this.healthStub = HealthGrpc.newBlockingStub(channel);
    }

    @Scheduled(fixedRateString = "${grpc.healthcheck.interval:5000}")
    public void checkHealth() {
        try {

            log.warn("Channel state: {}", channel.getState(true));
            HealthCheckRequest request = HealthCheckRequest.newBuilder()
                    .setService("") // Empty string checks overall server health
                    .build();

            HealthCheckResponse response = healthStub.check(request);
            log.info("Health status: {}", response.getStatus());
        } catch (io.grpc.StatusRuntimeException e) {
            if (e.getStatus().getCode() == io.grpc.Status.Code.UNAVAILABLE) {
                log.warn("Server is unavailable: {}", e.getMessage());
            } else {
                log.error("Health check failed: {}", e.getMessage());
            }
        } catch (Exception e) {
            log.error("Unexpected error during health check: {}", e.getMessage());
        }
    }

    public void shutdown() {
        try {
            channel.shutdown().awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS);
            log.info("Channel successfully shut down.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Channel shutdown interrupted: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error occurred during channel shutdown: {}", e.getMessage());
        }

    }
}