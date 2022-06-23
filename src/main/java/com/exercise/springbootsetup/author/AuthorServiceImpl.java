package com.exercise.springbootsetup.author;

import com.exercise.springbootsetup.book_authors.BookAuthor;
import com.exercise.springbootsetup.book_authors.BookAuthorRepository;
import com.exercise.springbootsetup.book_authors.BookAuthorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service("authorService")
public class AuthorServiceImpl implements AuthorService{
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookAuthorServiceImpl bookAuthorService;

    @Override
    public Map<String, Long> getAuthorsWithAmountOfBooks() {
        //  bookAuthorRepository.findAll() needs an ID, since it's a many-to-many table it can't make an ID and update
        //  all others entities once created => Made ID class
        return countAndCreateMap(authorRepository.findAll(), bookAuthorService.getBookAuthors());
    }

    private Map<String, Long> countAndCreateMap(List<Author> authorList, List<BookAuthor> bookAuthorList){
        Map<String, Long> authorAndBookCount = new HashMap<>();

        for (Author author : authorList) {
            Long amountOfBooks = bookAuthorList.stream().filter(ba-> Objects.equals(ba.getAuthor_id(), author.getId())).count();
            authorAndBookCount.put(author.getFullName(), amountOfBooks);
        }

        return authorAndBookCount;
    }
}
