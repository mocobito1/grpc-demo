package org.example.client.utils;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.client.config.GrpcServerConfig;

import java.util.concurrent.TimeUnit;

public class GrpcChannelUtil {
    /**
     * Creates a gRPC channel to the specified host and port.
     *
     * @param host the hostname or IP address of the server
     * @param port the port number of the server
     * @return a ManagedChannel instance
     */
    public static ManagedChannel createChannel(String host, int port) {
        return ManagedChannelBuilder.forAddress(host, port)
                .keepAliveTime(30, TimeUnit.SECONDS)            // Gửi ping mỗi 30s nếu không có hoạt động
                .keepAliveTimeout(5, TimeUnit.SECONDS)          // Nếu không nhận được ACK trong 5s → đóng kết nối
                .keepAliveWithoutCalls(true)                    // Vẫn ping kể cả không có RPC call đang mở
                .usePlaintext()
                .build();
    }
    public static ManagedChannel createChannel() {
        return ManagedChannelBuilder.forAddress(GrpcServerConfig.HOST, GrpcServerConfig.PORT)
                .keepAliveTime(30, TimeUnit.SECONDS)
                .keepAliveTimeout(5, TimeUnit.SECONDS)
                .keepAliveWithoutCalls(true)
                .usePlaintext()
                .build();
    }
}
