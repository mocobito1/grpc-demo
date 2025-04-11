package org.example.client.example;

import io.grpc.ManagedChannel;
import io.grpc.health.v1.HealthCheckRequest;
import io.grpc.health.v1.HealthCheckResponse;
import io.grpc.health.v1.HealthGrpc;
import org.example.client.utils.GrpcChannelUtil;

public class HealthCheckClient {
    public static void main(String[] args) {
        ManagedChannel channel = GrpcChannelUtil.createChannel("localhost", 9000);

        HealthGrpc.HealthBlockingStub healthStub = HealthGrpc.newBlockingStub(channel);

        // Create a health check request
        HealthCheckRequest request = HealthCheckRequest.newBuilder()
                .setService("") // Empty string checks the overall server health
                .build();

        // Perform the health check
        HealthCheckResponse response = healthStub.check(request);

        // Print the health status
        System.out.println("Health status: " + response.getStatus());

        channel.shutdown();
    }
}
