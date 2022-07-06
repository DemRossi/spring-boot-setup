package com.exercise.springbootsetup.booksAuthors;

import java.io.Serializable;

public class BooksAuthorsPK implements Serializable {
    private Long book_id;
    private Long author_id;

    public BooksAuthorsPK() {
    }

    public BooksAuthorsPK(Long book_id, Long author_id) {
        this.book_id = book_id;
        this.author_id = author_id;
    }
}
