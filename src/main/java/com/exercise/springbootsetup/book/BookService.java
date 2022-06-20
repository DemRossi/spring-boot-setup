package com.exercise.springbootsetup.book;

import java.io.IOException;
import java.util.List;

public interface BookService {
    List<Book> getBooksFromFile() throws IOException;

    List<Book> externalToInternalBooks(final List<com.exercise.springbootsetup.models.external.Book> source);

    Book externalToInternalBook(final com.exercise.springbootsetup.models.external.Book source);
}
