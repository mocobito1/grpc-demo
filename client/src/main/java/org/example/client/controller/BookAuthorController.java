package org.example.client.controller;

import lombok.AllArgsConstructor;
import org.example.client.service.BookAuthorClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
@AllArgsConstructor
public class BookAuthorController {
    final BookAuthorClientService bookAuthorClientService;

    @GetMapping("/author/{id}")
    public Object getAuthor(@PathVariable String id) {
        return bookAuthorClientService.getAuthor(Integer.parseInt(id));
    }
}
