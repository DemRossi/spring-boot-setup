package com.exercise.springbootsetup.bookAuthor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BookAuthorServiceImplTest {
    @Mock
    BookAuthorRepository bookAuthorRepository;

    @InjectMocks
    BookAuthorServiceImpl bookAuthorService;

    @Test
    void getBookAuthors() {
        List<BookAuthor> bookAuthors = bookAuthorService.getBookAuthors();

        assertThat(bookAuthors).isNotNull();
    }
}