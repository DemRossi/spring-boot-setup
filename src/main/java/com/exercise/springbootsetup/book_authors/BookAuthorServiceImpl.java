package com.exercise.springbootsetup.book_authors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(" bookAuthorService")
public class BookAuthorServiceImpl implements BookAuthorService {
    @Autowired
    BookAuthorRepository bookAuthorRepository;

    @Override
    public List<BookAuthor> getBookAuthors() {
        return bookAuthorRepository.findAll();
    }
}
