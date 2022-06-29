package com.exercise.springbootsetup.author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service("authorService")
public class AuthorServiceImpl implements AuthorService{
    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public Map<String, Long> getAuthorAndAmountOfBooks() {
        return authorRepository.getAuthorAndAmountOfBooks().stream()
                .collect(Collectors.toMap(AuthorCountDTO::getFullName, AuthorCountDTO::getBookCount));
    }
}
