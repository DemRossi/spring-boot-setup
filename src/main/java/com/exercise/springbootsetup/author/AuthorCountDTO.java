package com.exercise.springbootsetup.author;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorCountDTO {
    private String fullName;
    private Long bookCount;
}
