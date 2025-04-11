package org.example.client.example;

import com.example.Author;
import com.example.BookAuthorServiceGrpc;
import com.google.common.util.concurrent.*;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.Executors;

public class FutureStubExample {
    public static void main(String[] args) throws InterruptedException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9000)
                .usePlaintext()
                .build();
        var futureStub = BookAuthorServiceGrpc.newFutureStub(channel);

        // First async call
        var future1 = futureStub.getAuthor(Author.newBuilder().setAuthorId(1).build());
        Futures.addCallback(future1, new FutureCallback<>() {
            @Override
            public void onSuccess(Author result) {
                System.out.println("First call result: " + result);
            }
            @Override
            public void onFailure(Throwable t) {
                System.err.println("First call failed: " + t.getMessage());
            }
        }, Executors.newSingleThreadExecutor());

        // Second async call
        var future2 = futureStub.getAuthor(Author.newBuilder().setAuthorId(2).build());
        Futures.addCallback(future2, new FutureCallback<>() {
            @Override
            public void onSuccess(Author result) {
                System.out.println("Second call result: " + result);
            }
            @Override
            public void onFailure(Throwable t) {
                System.err.println("Second call failed: " + t.getMessage());
            }
        }, Executors.newSingleThreadExecutor());

        System.out.println("Main thread continues immediately...");
        Thread.sleep(10000);
        channel.shutdown();
    }
}
