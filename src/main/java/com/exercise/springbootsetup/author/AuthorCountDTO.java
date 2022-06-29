package com.exercise.springbootsetup.author;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorCountDTO {
    private String fullName;
    private Long bookCount;
}
