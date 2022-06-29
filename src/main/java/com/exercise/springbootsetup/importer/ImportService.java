package com.exercise.springbootsetup.importer;

import com.exercise.springbootsetup.book.Book;
import com.exercise.springbootsetup.exception.ServiceException;
import com.exercise.springbootsetup.query.Query;

import java.io.IOException;
import java.util.List;

public interface ImportService {
    List<Book> saveAllBooks(Query filter) throws ServiceException;

    List<Book> getBooksFromFile(final String filePath) throws IOException, ServiceException;

    void validate(Query filter) throws ServiceException;
}
