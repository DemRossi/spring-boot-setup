package com.exercise.springbootsetup.book;

import org.springframework.beans.factory.annotation.Autowired;
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
    public Optional<List<Book>> getBooks(
            @RequestParam(value = "sort", required = false) String sortDir,
            @RequestParam(value = "publishedAfter", required = false) String date
    ) {
        return bookServiceImpl.getBooks(sortDir, date);
    }

//    @GetMapping("/book/date")
//    public Optional<List<Book>> getBooksAfterPublishedDate(
//            @RequestParam(value = "publishedAfter", required = false) String date
//    ) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        LocalDate localDate = LocalDate.parse(date, formatter);
//
//        ZonedDateTime publishedAfter = localDate.atStartOfDay(ZoneId.systemDefault());
//
//        return bookRepository.findAllByPublishedDateAfter(publishedAfter);
//
//    }

    @GetMapping("/book/{isbn}")
    public Optional<Book> getBookByIsbn(@PathVariable String isbn) {
        return bookRepository.findBookByIsbn(isbn);
    }

}
