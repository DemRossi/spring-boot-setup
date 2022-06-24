package com.exercise.springbootsetup.author;

import com.exercise.springbootsetup.bookAuthor.BookAuthorServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {
    @Mock
    AuthorRepository authorRepository;

    @Mock
    BookAuthorServiceImpl bookAuthorService;

    @InjectMocks
    AuthorServiceImpl authorService;

    @Test
    void getAuthorsWithAmountOfBooks() {
        Map<String, Long> authorBookCountMap = authorService.getAuthorsWithAmountOfBooks();

        assertThat(authorBookCountMap).isNotNull();
        verify(authorRepository, times(1)).findAll();
        verify(bookAuthorService, times(1)).getBookAuthors();
    }
}