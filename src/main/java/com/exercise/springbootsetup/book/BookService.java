package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.exception.ServiceException;
import com.exercise.springbootsetup.query.Query;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getBooksFromFile(final String filePath) throws IOException, ServiceException;

    List<Book> saveAll(String filePath) throws ServiceException;

    Optional<Book> findBookByIsbn(Query filter) throws ServiceException;

    Optional<List<Book>> getBooks(Query filter) throws ServiceException;

    Book save(Book book) throws ServiceException;

    void deleteById(Long Id) throws ServiceException;

    void validate(Query filter) throws ServiceException;
}
