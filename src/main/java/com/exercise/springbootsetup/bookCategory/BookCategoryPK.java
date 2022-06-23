package com.exercise.springbootsetup.bookCategory;

import java.io.Serializable;

public class BookCategoryPK implements Serializable {
    private Long book_id;
    private Long category_id;

    public BookCategoryPK() {
    }

    public BookCategoryPK(Long book_id, Long category_id) {
        this.book_id = book_id;
        this.category_id = category_id;
    }
}
