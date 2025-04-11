package org.example.client.service;

import com.example.Author;
import com.example.BookAuthorServiceGrpc;
import com.google.protobuf.Descriptors;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.log4j.Log4j2;
import org.example.client.config.GrpcServerConfig;
import org.example.client.utils.GrpcChannelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Log4j2
public class BookAuthorClient {

    private final BookAuthorServiceGrpc.BookAuthorServiceBlockingStub bookAuthorServiceBlockingStub;

    public BookAuthorClient(GrpcServerConfig grpcServerConfig) {
        ManagedChannel channel = GrpcChannelUtil.createChannel(grpcServerConfig);
        bookAuthorServiceBlockingStub = BookAuthorServiceGrpc.newBlockingStub(channel);
    }

    public Map<Descriptors.FieldDescriptor, Object> getAuthor(int authorId) {
        Author authorRequest = Author.newBuilder().setAuthorId(authorId).build();
        Author authorResponse = bookAuthorServiceBlockingStub.getAuthor(authorRequest);
        getAuthors(authorId);
        return authorResponse.getAllFields();
    }

    public void getAuthors(int authorId) {
        int i = authorId + 1;
        long start = System.currentTimeMillis();
        log.info("Sending request 1...");
        log.info("Response 1: {}", bookAuthorServiceBlockingStub.getAuthor(Author.newBuilder().setAuthorId(i).build()));
        log.info("Sending request 2...");
        log.info("Response 2:  {}", bookAuthorServiceBlockingStub.getAuthor(Author.newBuilder().setAuthorId(i + 1).build()));
        long end = System.currentTimeMillis();
        log.info("Total time:  {} ms", (end - start));
    }
}
