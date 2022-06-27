package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.exception.ServiceException;
import com.exercise.springbootsetup.query.Query;
import com.exercise.springbootsetup.query.QueryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    final static String JSON_PATH = "src/test/resources/file/test_json.json";
    final String DATE = "2011-04-01";

    @Mock
    private BookRepository bookRepository;

    @Mock
    private QueryServiceImpl queryService;

    @InjectMocks
    private BookServiceImpl bookService;

    BookServiceImplTest() {
    }

    // TODO: Verify gebruiken

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
    void findBookByIsbn(){
        Optional<Book> book = bookRepository.findBookByIsbn("1234567890");
        verify(bookRepository, times(1)).findBookByIsbn("1234567890");
    }

    @Test
    void externalToInternalBooks() {
        List<com.exercise.springbootsetup.models.external.Book> externalBookList = new ArrayList<>();
        com.exercise.springbootsetup.models.external.Book book1 = new com.exercise.springbootsetup.models.external.Book();
        book1.setIsbn("1234567890");
        book1.setTitle("Dit is een titel");
        book1.setPageCount(123);
        book1.setPublishedDate(createZonedDateTime("2014-04-01"));

        com.exercise.springbootsetup.models.external.Book book2 = new com.exercise.springbootsetup.models.external.Book();
        book2.setIsbn("0987654321");
        book2.setTitle("Dit is een andere titel");
        book2.setPageCount(456);
        book2.setPublishedDate(createZonedDateTime("2011-01-11"));

        externalBookList.add(book1);
        externalBookList.add(book2);

        List<Book> internalBookList = bookService.externalToInternalBooks(externalBookList);

        assertThat(internalBookList).isNotNull();
        assertThat(internalBookList.size()).isEqualTo(2);

    }

    @Test
    void externalToInternalBook() {
        com.exercise.springbootsetup.models.external.Book externalBook = new com.exercise.springbootsetup.models.external.Book();
        externalBook.setIsbn("1234567890");
        externalBook.setTitle("Dit is een titel");
        externalBook.setPageCount(123);
        externalBook.setPublishedDate(createZonedDateTime("2014-04-01"));

        Book internalBook = bookService.externalToInternalBook(externalBook);

        assertThat(internalBook.getIsbn()).isEqualTo("1234567890");
    }


    @Test
    void getBooks_both_params_null() throws ServiceException {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);

        Query filter = Query.builder()
                .sortDir(queryService.createSort(null))
                .publishedAfter(queryService.createZonedDateTime(null))
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
        when(queryService.createSort("asc")).thenReturn("ASC");

        Query filter = Query.builder()
                .sortDir(queryService.createSort("asc"))
                .publishedAfter(null)
                .build();

        Optional<List<Book>> getBooks = bookService.getBooks(filter);

        assertThat(getBooks).isNotNull();
        verify(bookRepository, times(1)).getBooks(queryCaptor.capture());
        assertThat(queryCaptor.getValue())
                .isNotNull()
                .extracting(Query::getSortDir, Query::getPublishedAfter)
                .containsExactly("ASC", null);
    }

    @Test
    void getBooks_sortDir_filled_in_wrong() throws ServiceException {
        Query filter = Query.builder()
                .sortDir(queryService.createSort("qwerty"))
                .publishedAfter(queryService.createZonedDateTime(null))
                .build();

        Optional<List<Book>> getBooks = bookService.getBooks(filter);

        assertThat(getBooks).isEmpty();
    }

    @Test
    void getBooks_date_filled_in() throws ServiceException {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
        when(queryService.createZonedDateTime(DATE)).thenReturn(ZonedDateTime.parse("2011-04-01T00:00+02:00[Europe/Paris]"));

        Query filter = Query.builder()
                .sortDir(null)
                .publishedAfter(queryService.createZonedDateTime(DATE))
                .build();

        Optional<List<Book>> getBooks = bookService.getBooks(filter);

        assertThat(getBooks).isNotNull();
        verify(bookRepository, times(1)).getBooks(queryCaptor.capture());
        assertThat(queryCaptor.getValue())
                .isNotNull()
                .extracting(Query::getSortDir, Query::getPublishedAfter)
                .containsExactly( null,ZonedDateTime.parse("2011-04-01T00:00+02:00[Europe/Paris]") );
    }

    @Test
    void getBooks_date_filled_in_wrong() throws ServiceException {
        Query filter = Query.builder()
                .sortDir(queryService.createSort(null))
                .publishedAfter(queryService.createZonedDateTime("qwerty"))
                .build();

        Optional<List<Book>> getBooks = bookService.getBooks( filter);
        assertThat(getBooks).isEmpty();
    }

    @Test
    void getBooks_both_filled_in() throws ServiceException {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
        when(queryService.createSort("asc")).thenReturn("ASC");
        when(queryService.createZonedDateTime(DATE)).thenReturn(ZonedDateTime.parse("2011-04-01T00:00+02:00[Europe/Paris]"));

        Query filter = Query.builder()
                .sortDir(queryService.createSort("asc"))
                .publishedAfter(queryService.createZonedDateTime(DATE))
                .build();

        Optional<List<Book>> getBooks = bookService.getBooks(filter);

        assertThat(getBooks).isNotNull();
        verify(bookRepository, times(1)).getBooks(queryCaptor.capture());
        assertThat(queryCaptor.getValue())
                .isNotNull()
                .extracting(Query::getSortDir, Query::getPublishedAfter)
                .containsExactly("ASC", ZonedDateTime.parse("2011-04-01T00:00+02:00[Europe/Paris]"));
    }


    private ZonedDateTime createZonedDateTime(final String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);

        return localDate.atStartOfDay(ZoneId.systemDefault());
    }
}