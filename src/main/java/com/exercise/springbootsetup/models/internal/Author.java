package com.exercise.springbootsetup.models.internal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Author {
    private String fullName;

    public Author(){}

    public Author(String fullName){
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "Author{" +
                "fullName='" + fullName + '\'' +
                '}';
    }
}
