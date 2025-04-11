package org.example.client.example;

import com.example.BookAuthorServiceGrpc;
import com.example.Author;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.concurrent.TimeUnit;

public class TimeoutClientExample {

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9000)
                .usePlaintext()
                .build();

        var stub = BookAuthorServiceGrpc.newBlockingStub(channel);

        try {
            var response = stub.withDeadlineAfter(2, TimeUnit.SECONDS)
                    .getAuthor(Author.newBuilder().setAuthorId(1).build());
            System.out.println("Author: " + response);
        } catch (StatusRuntimeException e) {
            System.err.println(e.getStatus());
        }

        channel.shutdown();
    }
}
