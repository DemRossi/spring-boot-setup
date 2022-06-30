package com.exercise.springbootsetup.bookCategory;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "book_category")
@IdClass(BookCategoryPK.class)
public class BookCategory implements Serializable {
    @Id
    private Long book_id;
    @Id
    private Long category_id;
}
