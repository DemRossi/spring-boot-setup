package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.exception.ServiceException;
import com.exercise.springbootsetup.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BookController {
    @Autowired
    private BookServiceImpl bookService;

    @GetMapping("/book")
    public ResponseEntity<Optional<List<Book>>> getBooks(
            @RequestParam(value = "sort", required = false) String sortDir,
            @RequestParam(value = "publishedAfter", required = false) String date
    ) throws ServiceException {
        Query filter = Query.builder()
                .sortDir(sortDir)
                .publishedAfter(date)
                .build();
        bookService.validate(filter);
        return new ResponseEntity<>(bookService.getBooks(filter), HttpStatus.OK);
    }

//    @PostMapping("/book")
//    public ResponseEntity<Book> saveBook( @RequestBody Book book ){
//        // TODO: met Query Object??
//        //TODO: check if author exist, yes -> use author, no -> make author. Idem categories
//        Book newBook = bookService.save(book);
//        return ResponseEntity.created(URI.create(String.format("/book/%s", book.getIsbn())))
//                .body(newBook);
//    }
//
//    @DeleteMapping("/book/{id}")
//    public ResponseEntity<String>  deleteBook(@PathVariable Long id) {
//        // TODO: met Query Object
//        bookService.deleteById(id);
//        return ResponseEntity.ok("Removed book from db");
//    }

    @GetMapping("/book/{isbn}")
    public ResponseEntity<Optional<Book>> getBookByIsbn(@PathVariable String isbn) throws ServiceException {
        // TODO: met Query Object - DONE
        Query filter = Query.builder()
                .isbn(isbn)
                .build();
        return new ResponseEntity<>(bookService.findBookByIsbn(filter), HttpStatus.OK);
    }
}
