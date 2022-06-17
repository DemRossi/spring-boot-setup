package com.exercise.springbootsetup.controllers;

import com.exercise.springbootsetup.models.internal.Book;
import com.exercise.springbootsetup.services.BookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/hello")
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name){
        return String.format("Hello %s", name);
    }

    @GetMapping("/import-books")
    public List<Book> books() throws IOException {
        BookService bookService = new BookService();

        return bookService.getBooks();
    }

}
