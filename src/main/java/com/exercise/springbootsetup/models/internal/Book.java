package com.exercise.springbootsetup.models.internal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    private String title;
    private String isbn;
    private int pageCount;
    private Date publishedDate;
    private String thumbnailUrl;
    private String shortDescription;
    private String longDescription;
    private String status;
    private List<Author> authors;
    private List<Category> categories;

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", isbn='" + isbn + '\'' +
                ", pageCount=" + pageCount +
                ", publishedDate=" + publishedDate +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", status='" + status + '\'' +
                ", authors=" + authors +
                ", categories=" + categories +
                '}';
    }
}
