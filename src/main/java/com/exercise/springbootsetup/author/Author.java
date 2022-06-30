package com.exercise.springbootsetup.author;

import lombok.Getter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Entity
public class Author {
    public Author(){}

    public Author(String fullName){
        this.fullName = fullName;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ID_SEQ")
    private Long id;

    private String fullName;

    @Override
    public int hashCode() {
        return Objects.hash(fullName);
    }
}
