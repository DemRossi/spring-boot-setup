package com.exercise.springbootsetup.importer;

import com.exercise.springbootsetup.book.Book;
import com.exercise.springbootsetup.book.BookMapperImpl;
import com.exercise.springbootsetup.book.BookRepository;
import com.exercise.springbootsetup.exception.ServiceException;
import com.exercise.springbootsetup.query.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImportServiceImplTest {
    final String JSON_PATH = "src/test/resources/file/test_json.json";
    final String HTML_PATH = "src/test/resources/file/test_json.html";
    final String NO_SEPARATOR_PATH = "srctestresourcesfiletest_json.html";

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapperImpl bookMapper;

    @InjectMocks
    private ImportServiceImpl importService;

    @Test
    void getBooksFromFile() throws ServiceException {
        Book b1 = mock(Book.class);
        Book b2 = mock(Book.class);
        List<Book> books = new ArrayList<>();
        books.add(b1);
        books.add(b2);

        when(bookMapper.externalToInternalBooks(any())).thenReturn(books);

        List<Book> importedBookList = importService.getBooksFromFile(JSON_PATH);

        assertThat(importedBookList).isNotNull();
        assertThat(importedBookList).isNotEmpty();
        assertThat(importedBookList.size()).isEqualTo(2);
    }

    @Test
    void getBooksFromFile_null() {
        Exception exception = assertThrows(ServiceException.class, () -> {
            List<Book> importedBookList = importService.getBooksFromFile(null);
        });

        String expectedMessage = "No path given";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void getBooksFromFile_wrong_path() {
        Exception exception = assertThrows(ServiceException.class, () -> {
            List<Book> importedBookList = importService.getBooksFromFile("wrong/path/to/file.json");
        });

        String expectedMessage = "Exception while retrieving books from file:";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void saveAllBooks_verify_book_repository_call() throws ServiceException {
        Query filter = mock(Query.class);
        when(filter.getFilePath()).thenReturn(JSON_PATH);

        List<Book> savedBooks = importService.saveAllBooks(filter);

        verify(bookRepository, times(1)).saveAll(importService.getBooksFromFile(JSON_PATH));
    }

    @Test
    void validate_correct_filepath_expect_no_error(){
        Query filter = mock(Query.class);
        when(filter.getFilePath()).thenReturn(JSON_PATH);

        assertDoesNotThrow(() -> importService.validate(filter));

    }

    @Test
    void validate_not_a_json_expect_error() {
        Query filter = mock(Query.class);
        when(filter.getFilePath()).thenReturn(HTML_PATH);

        Exception exception = assertThrows(ServiceException.class, () ->
            importService.validate(filter)
        );

        String expectedMessage = "Given file isn't a json";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validate_no_separators_expect_error() {
        Query filter = mock(Query.class);
        when(filter.getFilePath()).thenReturn(NO_SEPARATOR_PATH);

        Exception exception = assertThrows(ServiceException.class, () ->
                importService.validate(filter)
        );

        String expectedMessage = "Invalid path";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}