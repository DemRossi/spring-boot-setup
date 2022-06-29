package com.exercise.springbootsetup.importer;

import com.exercise.springbootsetup.book.Book;
import com.exercise.springbootsetup.exception.ServiceException;
import com.exercise.springbootsetup.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ImportController {
    @Autowired
    private ImportServiceImpl importService;

    @GetMapping("/import/books")
    public ResponseEntity<List<Book>> saveBooks() throws ServiceException {
        // Import data: this case file -> could be api!
        // TODO: Move to importerController - DONE
        // TODO: met Query Object - DONE
        Query filter = Query.builder()
                .filePath("src/main/resources/files/books.json")
                .build();
        importService.validate(filter);
        List<Book> books = importService.saveAllBooks(filter);
        return new ResponseEntity<>(books, HttpStatus.CREATED);
    }
}
