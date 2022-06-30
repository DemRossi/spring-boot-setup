package com.exercise.springbootsetup.category;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryCountDTO {
    private String categoryName;
    private Long bookCount;
}
