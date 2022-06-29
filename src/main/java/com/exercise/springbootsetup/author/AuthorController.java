package com.exercise.springbootsetup.author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthorController {

    @Autowired
    private AuthorServiceImpl authorService;

    @GetMapping("/author")
    public Map<String, Long> getAuthorsWithAmountOfBooks() {
        return authorService.getAuthorAndAmountOfBooks();
    }
}
