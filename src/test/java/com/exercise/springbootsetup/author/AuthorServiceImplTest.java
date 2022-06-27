package com.exercise.springbootsetup.author;

import com.exercise.springbootsetup.bookAuthor.BookAuthor;
import com.exercise.springbootsetup.bookAuthor.BookAuthorServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {
    @Mock
    AuthorRepository authorRepository;

    @Mock
    BookAuthorServiceImpl bookAuthorService;

    @InjectMocks
    AuthorServiceImpl authorService;

    static List<Author> authors = new ArrayList<>();
    static List<BookAuthor> bookAuthors = new ArrayList<>();

    @BeforeAll
    public static void setup(){
        Author a1 = new Author();
        a1.setId(1L);
        a1.setFullName("Test Tester");

        Author a2 = new Author();
        a2.setId(2L);
        a2.setFullName("Andere Tester");

        BookAuthor ba1 = BookAuthor.builder()
                .author_id(1L)
                .book_id(5L)
                .build();

        BookAuthor ba2 = BookAuthor.builder()
                .author_id(1L)
                .book_id(7L)
                .build();

        BookAuthor ba3 = BookAuthor.builder()
                .author_id(2L)
                .book_id(6L)
                .build();
        BookAuthor ba4 = BookAuthor.builder()
                .author_id(1L)
                .book_id(8L)
                .build();
        BookAuthor ba5 = BookAuthor.builder()
                .author_id(2L)
                .book_id(8L)
                .build();

        authors.add(a1);
        authors.add(a2);

        bookAuthors.add(ba1);
        bookAuthors.add(ba2);
        bookAuthors.add(ba3);
        bookAuthors.add(ba4);
        bookAuthors.add(ba5);
    }

    @Test
    void getAuthorsWithAmountOfBooks() {
        Map<String, Long> authorBookCountMap = authorService.getAuthorsWithAmountOfBooks();

        verify(authorRepository, times(1)).findAll();
        verify(bookAuthorService, times(1)).getBookAuthors();
    }

    @Test
    void getAuthorsWithAmountOfBooks_with_lists() {
        when(authorRepository.findAll()).thenReturn(authors);
        when(bookAuthorService.getBookAuthors()).thenReturn(bookAuthors);
        Map<String, Long> authorBookCountMap = authorService.getAuthorsWithAmountOfBooks();

        assertThat(authorBookCountMap).isNotNull();
        assertThat(authorBookCountMap).isNotEmpty();
        assertThat(authorBookCountMap.size()).isEqualTo(2);
    }
}