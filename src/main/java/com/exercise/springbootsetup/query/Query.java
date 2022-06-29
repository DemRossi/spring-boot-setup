package com.exercise.springbootsetup.query;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Query {
    private String sortDir;
    private String publishedAfter;
    private String isbn;
    private String filePath;
}
