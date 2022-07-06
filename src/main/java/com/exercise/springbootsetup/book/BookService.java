package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.exception.ServiceException;
import com.exercise.springbootsetup.query.Query;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<Book> findBookByIsbn(Query filter) throws ServiceException;

    Optional<List<Book>> getBooks(Query filter) throws ServiceException;

    Book save(Book book) throws ServiceException;

    Optional<Book> deleteById(Query filter) throws ServiceException;

    void validate(Query filter) throws ServiceException;
}
