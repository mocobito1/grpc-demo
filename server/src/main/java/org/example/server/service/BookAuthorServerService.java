package org.example.server.service;

import com.example.Author;
import com.example.BookAuthorServiceGrpc;
import io.grpc.Context;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.example.common.TempData;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
public class BookAuthorServerService extends BookAuthorServiceGrpc.BookAuthorServiceImplBase {

    private static final int SIMULATED_PROCESSING_DELAY_MS = 5000;

    @Override
    public void getAuthor(Author request, StreamObserver<Author> responseObserver) {
        logRequest(request);

        try {
            Optional<Author> foundAuthor = findAuthorById(request.getAuthorId());

            for (int i = 0; i < SIMULATED_PROCESSING_DELAY_MS / 1000; i++) {
                if (Context.current().isCancelled()) {
                    log.warn("Request for authorId {} was canceled by the client.", request.getAuthorId());
                    responseObserver.onError(new RuntimeException("Request canceled by client."));
                    return;
                }
//                Thread.sleep(1000); // Simulate processing in chunks
                simulateProcessingDelay();
            }

            foundAuthor.ifPresentOrElse(
                    responseObserver::onNext,
                    () -> log.warn("No author found for ID: {}", request.getAuthorId())
            );

            responseObserver.onCompleted();
        } catch (InterruptedException e) {
            log.error("Error during processing request for authorId {}: {}", request.getAuthorId(), e.getMessage());
            responseObserver.onError(e);
            Thread.currentThread().interrupt(); // Restore interrupt status
        }

        logCompletion(request);
    }

    private void logRequest(Author request) {
        log.info("Received request for authorId: {} on thread: {} at: {}",
                request.getAuthorId(), Thread.currentThread().getName(), LocalDateTime.now());
    }

    private void logCompletion(Author request) {
        log.info("Done handling authorId: {} at: {}", request.getAuthorId(), LocalDateTime.now());
    }

    private Optional<Author> findAuthorById(long authorId) {
        return TempData.getAuthorsFromTempDb()
                .stream()
                .filter(author -> author.getAuthorId() == authorId)
                .findFirst();
    }

    private void simulateProcessingDelay() throws InterruptedException {
        Thread.sleep(0);
    }
}