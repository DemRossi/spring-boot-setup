package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.exception.ServiceException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getBooksFromFile(final String filePath) throws IOException, ServiceException;

    List<Book> saveAll(String filePath) throws ServiceException;

    Optional<Book> findBookByIsbn(String isbn);

    List<Book> externalToInternalBooks(final List<com.exercise.springbootsetup.models.external.Book> source);

    Book externalToInternalBook(final com.exercise.springbootsetup.models.external.Book source);

    Optional<List<Book>> getBooks(String sortDir, String publishedAfter);
}
