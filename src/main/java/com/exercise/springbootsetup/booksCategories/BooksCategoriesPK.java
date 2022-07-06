package com.exercise.springbootsetup.booksCategories;

import java.io.Serializable;

public class BooksCategoriesPK implements Serializable {
    private Long book_id;
    private Long category_id;

    public BooksCategoriesPK() {
    }

    public BooksCategoriesPK(Long book_id, Long category_id) {
        this.book_id = book_id;
        this.category_id = category_id;
    }
}
