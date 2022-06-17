package com.exercise.springbootsetup.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service(" bookService")
public class BookService {
    private static final String JSON_PATH = "src/main/resources/files/books.json";

    BookMapper bookMapper = new BookMapper();

    ObjectMapper mapper = new ObjectMapper();

    public List<Book> getBooksFromFile() throws IOException {
        return bookMapper.externalToInternalBooks(Arrays.asList(mapper.readValue(new FileReader(JSON_PATH), com.exercise.springbootsetup.models.external.Book[].class)));
    }

}
