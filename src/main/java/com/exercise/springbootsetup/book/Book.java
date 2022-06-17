package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.author.Author;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String isbn;
    private int pageCount;
    private Date publishedDate;
    private String thumbnailUrl;
    private String shortDescription;
    private String longDescription;
    private String status;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Author> authors;

//    @ManyToMany(cascade = CascadeType.ALL)
//    private List<Category> categories;

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
//                ", categories=" + categories +
                '}';
    }
}
