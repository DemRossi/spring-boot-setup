package com.exercise.springbootsetup.book;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.ZonedDateTime;

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

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime publishedDate;
    private String thumbnailUrl;
    private String shortDescription;
    private String longDescription;
    private String status;

//    @ManyToMany(cascade = CascadeType.ALL)
//    private List<Author> authors;

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
//                ", authors=" + authors +
//                ", categories=" + categories +
                '}';
    }
}
