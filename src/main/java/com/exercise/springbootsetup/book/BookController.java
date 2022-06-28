package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.exception.ServiceException;
import com.exercise.springbootsetup.query.Query;
import com.exercise.springbootsetup.query.QueryServiceImpl;
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

    @Autowired
    private QueryServiceImpl queryService;

    @GetMapping ("/import-books")
    public ResponseEntity<List<Book>> saveBooks() throws ServiceException {
        // Import data: this case file -> could be api!
        final String JSON_PATH = "src/main/resources/files/books.json";
        List<Book> books = bookService.saveAll(JSON_PATH);
        return new ResponseEntity<>(books, HttpStatus.CREATED);
    }

    @GetMapping("/book")
    public ResponseEntity<Optional<List<Book>>> getBooks(
            @RequestParam(value = "sort", required = false) String sortDir,
            @RequestParam(value = "publishedAfter", required = false) String date
    ) throws ServiceException {
        // Make query Object
        Query filter = Query.builder()
                .sortDir(queryService.createSort(sortDir))
                .publishedAfter(queryService.createZonedDateTime(date))
                .build();
        // give QueryObj to service
        // check in service if object is right
        // if right give to repository, else error
        // in repository make sql query based on non-null members of queryObj
        return new ResponseEntity<>(bookService.getBooks(filter), HttpStatus.OK);
    }

    @GetMapping("/book/{isbn}")
    public ResponseEntity<Optional<Book>> getBookByIsbn(@PathVariable String isbn) throws ServiceException {
        // TODO: met Query Object - DONE
        Query filter = Query.builder()
                .isbn(isbn)
                .build();
        return new ResponseEntity<>(bookService.findBookByIsbn(filter), HttpStatus.OK);
    }
}
