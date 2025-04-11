package org.example.client.example;

import com.example.Author;
import com.example.BookAuthorServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class BlockingStubThreadExample {

    private final BookAuthorServiceGrpc.BookAuthorServiceBlockingStub stub;

    public BlockingStubThreadExample(BookAuthorServiceGrpc.BookAuthorServiceBlockingStub stub) {
        this.stub = stub;
    }

    public void run() {
        Runnable task1 = () -> {
            try {
                System.out.println("Thread 1 started");
                var response = stub.getAuthor(Author.newBuilder().setAuthorId(1).build());
                System.out.println("Thread 1 response: " + response);
                Thread.sleep(2000); // Simulate some processing time
            } catch (Exception e) {
                System.err.println("Thread 1 failed: " + e.getMessage());
            }
        };

        Runnable task2 = () -> {
            try {
                System.out.println("Thread 2 started");
                var response = stub.getAuthor(Author.newBuilder().setAuthorId(2).build());
                System.out.println("Thread 2 response: " + response);
                Thread.sleep(2000); // Simulate some processing time
            } catch (Exception e) {
                System.err.println("Thread 2 failed: " + e.getMessage());
            }
        };

        Thread t1 = new Thread(task1);
        t1.start();

        Thread t2 = new Thread(task2);
        t2.start();

        System.out.println("Main thread is free and continues running...");
    }
    public void runSecond() {
        System.out.println("Starting sequential blocking calls...");
        try {
            System.out.println("First call...");
            var response1 = stub.getAuthor(Author.newBuilder().setAuthorId(1).build());
            System.out.println("Received: " + response1);

            System.out.println("Second call...");
            var response2 = stub.getAuthor(Author.newBuilder().setAuthorId(2).build());
            System.out.println("Received: " + response2);

        } catch (Exception e) {
            System.err.println("Sequential call error: " + e.getMessage());
        }
        System.out.println("All blocking calls finished.");
    }

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9000)
                .usePlaintext()
                .build();

        var stub = BookAuthorServiceGrpc.newBlockingStub(channel);
        new BlockingStubThreadExample(stub).runSecond();

    }
}
