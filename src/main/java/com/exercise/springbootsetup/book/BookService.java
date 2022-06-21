package com.exercise.springbootsetup.book;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface BookService {
    List<Book> getBooksFromFile() throws IOException;

    List<Book> externalToInternalBooks(final List<com.exercise.springbootsetup.models.external.Book> source);

    Book externalToInternalBook(final com.exercise.springbootsetup.models.external.Book source);

    Optional<List<Book>> getBooks(String sortDir, String publishedAfter);
}
