package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BookController {
    @Autowired
    private BookServiceImpl bookService;

    @GetMapping ("/import-books")
    public ResponseEntity<List<Book>> saveBooks() throws ServiceException {
        // Import data: this case file -> could be api!
        final String JSON_PATH = "src/main/resources/files/books.json";
        List<Book> books = bookService.saveAll(JSON_PATH);
        return new ResponseEntity<>(books, HttpStatus.CREATED);
    }

    @GetMapping("/book")
    public Optional<List<Book>> getBooks(
            @RequestParam(value = "sort", required = false) String sortDir,
            @RequestParam(value = "publishedAfter", required = false) String date
    ) throws ServiceException {
        return bookService.getBooks(sortDir, date);
    }

    @GetMapping("/book/{isbn}")
    public Optional<Book> getBookByIsbn(@PathVariable String isbn) {
        return bookService.findBookByIsbn(isbn);
    }

}
