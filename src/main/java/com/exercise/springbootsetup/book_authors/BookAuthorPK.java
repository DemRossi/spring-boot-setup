package com.exercise.springbootsetup.book_authors;

import java.io.Serializable;

public class BookAuthorPK implements Serializable {
    private Long book_id;
    private Long author_id;

    public BookAuthorPK() {
    }

    public BookAuthorPK(Long book_id, Long author_id) {
        this.book_id = book_id;
        this.author_id = author_id;
    }
}
