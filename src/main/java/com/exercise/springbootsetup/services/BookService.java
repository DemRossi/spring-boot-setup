package com.exercise.springbootsetup.services;

import com.exercise.springbootsetup.mapper.BookMapper;
import com.exercise.springbootsetup.models.external.Book;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class BookService {
    private static final String JSON_PATH = "src/main/resources/files/books.json";

    BookMapper bookMapper = new BookMapper();

    ObjectMapper mapper = new ObjectMapper();

    public List<com.exercise.springbootsetup.models.internal.Book> getBooks() throws IOException {
//        if(StringUtils.isNotBlank(source)){
//            internalBook =
//        }
        return bookMapper.externalToInternalBooks(Arrays.asList(mapper.readValue(new FileReader(JSON_PATH), Book[].class)));
    }

}
