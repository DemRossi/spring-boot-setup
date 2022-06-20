package com.exercise.springbootsetup.book;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookServiceImpl bookServiceImpl;

    @PostMapping ("/import-books")
    public String saveBooks() throws Exception {
        // Import data: this case file -> could be api!
        bookRepository.saveAll(bookServiceImpl.getBooksFromFile());
        return "Books are saved in DB!!!";
    }

    @GetMapping("/book")
    public List<Book> getBooks(
            @RequestParam(value = "sort", required = false) String sortDir
    ) {
        return StringUtils.isNotBlank(sortDir) ?
                bookRepository.findAll(Sort.by(Sort.Direction.fromString(sortDir), "title")) :
                bookRepository.findAll();
    }

    @GetMapping("/book/{isbn}")
    public Optional<Book> getBookByIsbn(@PathVariable String isbn) {
        return bookRepository.findBookByIsbn(isbn);
    }

}
