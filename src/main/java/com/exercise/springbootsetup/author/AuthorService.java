package com.exercise.springbootsetup.author;

import java.util.Map;

public interface AuthorService {
    Map<String, Long> getAuthorsWithAmountOfBooks();
}
