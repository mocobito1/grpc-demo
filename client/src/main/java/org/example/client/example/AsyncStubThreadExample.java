package org.example.client.example;

import com.example.Author;
import com.example.BookAuthorServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import org.example.client.utils.GrpcChannelUtil;

public class AsyncStubThreadExample {

    private final BookAuthorServiceGrpc.BookAuthorServiceStub asyncStub;

    public AsyncStubThreadExample(BookAuthorServiceGrpc.BookAuthorServiceStub asyncStub) {
        this.asyncStub = asyncStub;
    }

    public void run() {
        Runnable task1 = () -> {
            System.out.println("Thread 1 started (async call initiated)");
            asyncStub.getAuthor(Author.newBuilder().setAuthorId(1).build(),
                    new StreamObserver<>() {
                        @Override
                        public void onNext(Author value) {
                            try {
                                System.out.println("Thread 1 received: " + value);
                                Thread.sleep(1000); // Simulate extra processing
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            System.err.println("Thread 1 failed: " + t.getMessage());
                        }

                        @Override
                        public void onCompleted() {
                            System.out.println("Thread 1 completed");
                        }
                    });
        };

        Runnable task2 = () -> {
            System.out.println("Thread 2 started (async call initiated)");
            asyncStub.getAuthor(Author.newBuilder().setAuthorId(2).build(),
                    new StreamObserver<>() {
                        @Override
                        public void onNext(Author value) {
                            try {
                                System.out.println("Thread 2 received: " + value);
                                Thread.sleep(1000); // Simulate extra processing
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            System.err.println("Thread 2 failed: " + t.getMessage());
                        }

                        @Override
                        public void onCompleted() {
                            System.out.println("Thread 2 completed");
                        }
                    });
        };

        new Thread(task1).start();
        new Thread(task2).start();
        System.out.println("Main thread continues immediately (non-blocking)...");
    }

    public static void main(String[] args) throws InterruptedException {
       ManagedChannel channel = GrpcChannelUtil.createChannel("localhost", 9000);

        var asyncStub = BookAuthorServiceGrpc.newStub(channel);
        new AsyncStubThreadExample(asyncStub).run();

        System.out.println("Perform other tasks while async calls are in progress...");
        Thread.sleep(12000);
        System.out.println("End.===============================================");
        channel.shutdown();
    }
}
