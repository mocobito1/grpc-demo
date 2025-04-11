package org.example.client.service;

import com.example.Author;
import com.example.BookAuthorServiceGrpc;
import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class AuthorClientService {

    private final BookAuthorServiceGrpc.BookAuthorServiceBlockingStub stub;

    public AuthorClientService(BookAuthorServiceGrpc.BookAuthorServiceBlockingStub stub) {
        this.stub = stub;
    }

    public void getAuthor(int authorId) {
        try {
            log.info("Calling getAuthor for ID: {}", authorId);
            var response = stub.getAuthor(Author.newBuilder().setAuthorId(authorId).build());

            log.info("✅ Got author: {}", response.getAuthorId());
        } catch (StatusRuntimeException e) {
            log.error("❌ RPC failed: {}", e.getStatus());
        }
    }
}
