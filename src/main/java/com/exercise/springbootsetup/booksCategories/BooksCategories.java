package com.exercise.springbootsetup.booksCategories;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "books_categories")
@IdClass(BooksCategoriesPK.class)
public class BooksCategories implements Serializable {
    @Id
    private Long book_id;
    @Id
    private Long category_id;
}
