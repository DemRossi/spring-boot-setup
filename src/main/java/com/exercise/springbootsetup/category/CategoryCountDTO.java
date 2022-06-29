package com.exercise.springbootsetup.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCountDTO {
    private String categoryName;
    private Long bookCount;
}
