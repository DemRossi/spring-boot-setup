package com.exercise.springbootsetup.author;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
public class Author {
    public Author(){}

    public Author(String fullName){
        this.fullName = fullName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fullName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author)) return false;
        Author author = (Author) o;
        return fullName.equals(author.fullName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fullName);
    }

    @Override
    public String toString() {
        return "Author{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
