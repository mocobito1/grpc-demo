package org.example.client.example;

import com.example.Author;
import com.example.BookAuthorServiceGrpc;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.TimeUnit;

public class TimeoutClientExample {

    public static void main(String[] args) throws Exception {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9000)
                .usePlaintext()
                .build();

//        callBlockingStub(channel);
//        callNonBlockingStub(channel);
        callFutureStub(channel);
        System.out.println("---------------------------------");
        System.out.println("Do something...");
        System.out.println("---------------------------------");
        Thread.sleep(1000); // Simulate processing in chunks

        // Shutdown the channel
        channel.shutdown();
    }

    private static void callBlockingStub(ManagedChannel channel) {
        BookAuthorServiceGrpc.BookAuthorServiceBlockingStub blockingStub = BookAuthorServiceGrpc.newBlockingStub(channel).
                withDeadlineAfter(2, TimeUnit.SECONDS);
        try {
            Author response = blockingStub.getAuthor(Author.newBuilder().setAuthorId(1).build());
            System.out.println("Blocking Response: " + response);
        } catch (Exception e) {
            System.err.println("Blocking Error: " + e.getMessage());
        }
    }

    private static void callNonBlockingStub(ManagedChannel channel) {
        BookAuthorServiceGrpc.BookAuthorServiceStub nonBlockingStub = BookAuthorServiceGrpc.newStub(channel);
        nonBlockingStub.getAuthor(Author.newBuilder().setAuthorId(2).build(), new StreamObserver<>() {
            @Override
            public void onNext(Author value) {
                System.out.println("Non-Blocking Response: " + value);
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Non-Blocking Error: " + t.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Non-Blocking Completed");
            }
        });
    }

    private static void callFutureStub(ManagedChannel channel) {
        BookAuthorServiceGrpc.BookAuthorServiceFutureStub futureStub = BookAuthorServiceGrpc.newFutureStub(channel);
        ListenableFuture<Author> futureResponse = futureStub.getAuthor(Author.newBuilder().setAuthorId(3).build());
        try {
            Author response = futureResponse.get(2, TimeUnit.SECONDS); // Wait for 2 seconds
            System.out.println("Future Response: " + response);
        } catch (Exception e) {
            System.err.println("Future Error: " + e.getMessage());
        }
    }
}