package com.exercise.springbootsetup.models.external;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Book {
    private String title;
    private String isbn;
    private int pageCount;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime publishedDate;

    private String thumbnailUrl;
    private String shortDescription;
    private String longDescription;
    private String status;
    private List<String> authors;
    private List<String> categories;
}
