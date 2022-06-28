package com.exercise.springbootsetup.book;

import java.util.List;

public interface BookMapper {
    List<Book> externalToInternalBooks(final List<com.exercise.springbootsetup.book.external.Book> source);

    Book externalToInternalBook(final com.exercise.springbootsetup.book.external.Book source);
}
