package com.exercise.springbootsetup.bookAuthor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookAuthorServiceImplTest {
    @Mock
    BookAuthorRepository bookAuthorRepository;

    @InjectMocks
    BookAuthorServiceImpl bookAuthorService;

    @Test
    void getBookAuthors() {
        BookAuthor bookAuthor = mock(BookAuthor.class);

        List<BookAuthor> list = Collections.singletonList(bookAuthor);
        when(bookAuthorRepository.findAll()).thenReturn(list);

        List<BookAuthor> bookAuthors = bookAuthorService.getBookAuthors();

        assertThat(bookAuthors).isNotNull();
        assertThat(bookAuthors).isNotEmpty();
    }
}