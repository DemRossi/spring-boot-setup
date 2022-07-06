package com.exercise.springbootsetup.booksAuthors;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "books_authors")
@IdClass(BooksAuthorsPK.class)
public class BooksAuthors implements Serializable {
    @Id
    private Long book_id;
    @Id
    private Long author_id;
}
