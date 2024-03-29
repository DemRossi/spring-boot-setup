package com.exercise.springbootsetup.category;

import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
public class Category {
    public Category() {
    }

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
    private Long id;

    private String categoryName;

    @Override
    public int hashCode() {
        return Objects.hash(categoryName);
    }
}
