package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.exception.ServiceException;
import com.exercise.springbootsetup.query.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    final static String JSON_PATH = "src/test/resources/file/test_json.json";
    final String DATE = "2011-04-01";

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    BookServiceImplTest() {
    }

    @Test
    void getBooksFromFile() throws ServiceException {
        List<Book> importedBookList = bookService.getBooksFromFile(JSON_PATH);

        assertThat(importedBookList).isNotNull();
    }

    @Test
    void getBooksFromFile_null() {
        Exception exception = assertThrows(ServiceException.class, () -> {
            List<Book> importedBookList = bookService.getBooksFromFile(null);
        });

        String expectedMessage = "No path given";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getBooksFromFile_wrong_path() {
        Exception exception = assertThrows(ServiceException.class, () -> {
            List<Book> importedBookList = bookService.getBooksFromFile("wrong/path/to/file.json");
        });

        String expectedMessage = "Exception while retrieving books from file:";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void saveAll() throws ServiceException {
        List<Book> savedBooks = bookService.saveAll(JSON_PATH);

        verify(bookRepository, times(1)).saveAll(bookService.getBooksFromFile(JSON_PATH));
    }

    @Test
    void findBookByIsbn() throws ServiceException {
        Query filter = mock(Query.class);
        when(bookRepository.findBookByIsbn("1234567890")).thenReturn(Optional.of(mock(Book.class)));
        when(filter.getIsbn()).thenReturn("1234567890");

        Optional<Book> book = bookService.findBookByIsbn(filter);

        verify(bookRepository, times(1)).findBookByIsbn(filter.getIsbn());
    }

    @Test
    void findBookByIsbn_no_result() {
        Query filter = mock(Query.class);
        when(bookRepository.findBookByIsbn("1234567890")).thenReturn(Optional.empty());
        when(filter.getIsbn()).thenReturn("1234567890");

        Exception exception = assertThrows(ServiceException.class, () -> {
            Optional<Book> book = bookService.findBookByIsbn(filter);
        });

        String expectedMessage = "Book not found: Check the ISBN, if correct the book doesn't exist in our DB";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        verify(bookRepository, times(1)).findBookByIsbn(filter.getIsbn());
    }

    @Test
    void getBooks_both_params_null() throws ServiceException {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);

        Query filter = Query.builder()
                .sortDir(null)
                .publishedAfter(null)
                .build();

        Optional<List<Book>> getBooks = bookService.getBooks(filter);

        assertThat(getBooks).isNotNull();
        verify(bookRepository, times(1)).getBooks(queryCaptor.capture());
        assertThat(queryCaptor.getValue())
                .isNotNull()
                .extracting(Query::getSortDir, Query::getPublishedAfter)
                .containsExactly(null, null);
    }

    @Test
    void getBooks_sortDir_filled_in() throws ServiceException {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);

        Query filter = Query.builder()
                .sortDir("asc")
                .publishedAfter(null)
                .build();

        Optional<List<Book>> getBooks = bookService.getBooks(filter);

        assertThat(getBooks).isNotNull();
        verify(bookRepository, times(1)).getBooks(queryCaptor.capture());
        assertThat(queryCaptor.getValue())
                .isNotNull()
                .extracting(Query::getSortDir, Query::getPublishedAfter)
                .containsExactly("asc", null);
    }

    @Test
    void getBooks_sortDir_filled_in_wrong() throws ServiceException {
        Query filter = Query.builder()
                .sortDir("qwerty")
                .publishedAfter(null)
                .build();

        Optional<List<Book>> getBooks = bookService.getBooks(filter);

        assertThat(getBooks).isEmpty();
    }

    @Test
    void getBooks_date_filled_in() throws ServiceException {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);

        Query filter = Query.builder()
                .sortDir(null)
                .publishedAfter(DATE)
                .build();

        Optional<List<Book>> getBooks = bookService.getBooks(filter);

        assertThat(getBooks).isNotNull();
        verify(bookRepository, times(1)).getBooks(queryCaptor.capture());
        assertThat(queryCaptor.getValue())
                .isNotNull()
                .extracting(Query::getSortDir, Query::getPublishedAfter)
                .containsExactly( null, DATE);
    }

    @Test
    void getBooks_date_filled_in_wrong() throws ServiceException {
        Query filter = Query.builder()
                .sortDir(null)
                .publishedAfter("qwerty")
                .build();

        Optional<List<Book>> getBooks = bookService.getBooks( filter);
        assertThat(getBooks).isEmpty();
    }

    @Test
    void getBooks_both_filled_in() throws ServiceException {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);

        Query filter = Query.builder()
                .sortDir("asc")
                .publishedAfter(DATE)
                .build();

        Optional<List<Book>> getBooks = bookService.getBooks(filter);

        assertThat(getBooks).isNotNull();
        verify(bookRepository, times(1)).getBooks(queryCaptor.capture());
        assertThat(queryCaptor.getValue())
                .isNotNull()
                .extracting(Query::getSortDir, Query::getPublishedAfter)
                .containsExactly("asc", DATE);
    }

    @Test
    void validate_nothing_filled_in_expect_no_error() {
        Query filter = mock(Query.class);

        assertDoesNotThrow(() -> bookService.validate(filter));
    }

    @Test
    void validate_all_correctly_filled_in_expect_no_error() {
        Query filter = mock(Query.class);

        when(filter.getSortDir()).thenReturn("asc");
        when(filter.getPublishedAfter()).thenReturn(DATE);

        assertDoesNotThrow(() -> bookService.validate(filter));
    }

    @Test
    void validate_sort_direction_filled_in_expect_no_error() {
        Query filter = mock(Query.class);

        when(filter.getSortDir()).thenReturn("asc");

        assertDoesNotThrow(() -> bookService.validate(filter));
    }

    @Test
    void validate_date_filled_in_expect_no_error() {
        Query filter = mock(Query.class);

        when(filter.getPublishedAfter()).thenReturn(DATE);

        assertDoesNotThrow(() -> bookService.validate(filter));
    }

    @Test
    void validate_wrong_sort_direction_filled_in_expect_error() {
        Query filter = mock(Query.class);
        when(filter.getSortDir()).thenReturn("jef");

        Exception exception = assertThrows(ServiceException.class, () ->
            bookService.validate(filter)
        );

        String expectedMessage = "Sort parameter can only be asc or desc";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validate_wrong_date_filled_in_expect_error() {
        Query filter = mock(Query.class);
        when(filter.getPublishedAfter()).thenReturn("qwerty");

        Exception exception = assertThrows(ServiceException.class, () ->
            bookService.validate(filter)
        );

        String expectedMessage = "Something is wrong with the date format";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}