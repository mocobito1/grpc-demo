package org.example.client.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@AllArgsConstructor
public class BookAuthorClientService {


    BookAuthorClient bookAuthorClient;


    public Object getAuthor(int authorId) {
        return bookAuthorClient.getAuthor(authorId);
    }

}
