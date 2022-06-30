package com.exercise.springbootsetup.bookAuthor;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "book_author")
@IdClass(BookAuthorPK.class)
public class BookAuthor implements Serializable {
    @Id
    private Long book_id;
    @Id
    private Long author_id;
}
