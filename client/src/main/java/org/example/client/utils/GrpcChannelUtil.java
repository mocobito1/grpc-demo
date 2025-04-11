package org.example.client.utils;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

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
                .usePlaintext()
                .build();
    }
}
