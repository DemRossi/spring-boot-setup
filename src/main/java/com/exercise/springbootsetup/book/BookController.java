package com.exercise.springbootsetup.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/import-books")
    public String saveBooks() throws Exception {
        // Import data: this case file -> could be api!
        BookService bookService = new BookService();

        bookRepository.saveAll(bookService.getBooksFromFile());
        return "Books are saved in DB!!!";
    }

    @GetMapping("/get/book/{id}")
    public Optional<Book> getBook(@PathVariable Long id) throws Exception {
        return bookRepository.findById(id);
    }

    @GetMapping("/get/books")
    public List<Book> getBook() throws Exception {
        return bookRepository.findAll();
    }
}
