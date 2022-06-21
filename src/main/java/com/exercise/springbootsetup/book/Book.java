package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.author.Author;
import com.exercise.springbootsetup.category.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Author> authors;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Category> categories;

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
