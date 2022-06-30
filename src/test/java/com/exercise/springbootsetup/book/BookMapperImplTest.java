package com.exercise.springbootsetup.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookMapperImplTest {
    @InjectMocks
    BookMapperImpl bookMapper;

    @Test
    void externalToInternalBooks() {
        List<String> testList = Arrays.asList("tester", "test");

        List<com.exercise.springbootsetup.book.external.Book> externalBookList = new ArrayList<>();
        com.exercise.springbootsetup.book.external.Book book1 = mock(com.exercise.springbootsetup.book.external.Book.class);
        when(book1.getIsbn()).thenReturn("1234567890");
        when(book1.getTitle()).thenReturn("titel");
        when(book1.getPageCount()).thenReturn(123);
        when(book1.getPublishedDate()).thenReturn(createZonedDateTime("2014-04-01"));
        when(book1.getThumbnailUrl()).thenReturn("www.whatever.be");
        when(book1.getShortDescription()).thenReturn("short");
        when(book1.getLongDescription()).thenReturn("Long");
        when(book1.getStatus()).thenReturn("published");
        when(book1.getAuthors()).thenReturn(testList);
        when(book1.getCategories()).thenReturn(testList);

        com.exercise.springbootsetup.book.external.Book book2 = mock(com.exercise.springbootsetup.book.external.Book.class);

        externalBookList.add(book1);
        externalBookList.add(book2);

        List<Book> internalBookList = bookMapper.externalToInternalBooks(externalBookList);

        assertThat(internalBookList).isNotNull();
        assertThat(internalBookList.size()).isEqualTo(2);
        assertThat(internalBookList.get(0).getIsbn()).isEqualTo("1234567890");
        assertThat(internalBookList.get(0).getTitle()).isEqualTo("titel");
        assertThat(internalBookList.get(0).getPageCount()).isEqualTo(123);
        assertThat(internalBookList.get(0).getPublishedDate()).isEqualTo(createZonedDateTime("2014-04-01"));
        assertThat(internalBookList.get(0).getThumbnailUrl()).isEqualTo("www.whatever.be");
        assertThat(internalBookList.get(0).getShortDescription()).isEqualTo("short");
        assertThat(internalBookList.get(0).getLongDescription()).isEqualTo("Long");
        assertThat(internalBookList.get(0).getStatus()).isEqualTo("published");
        assertThat(internalBookList.get(0).getAuthor().size()).isEqualTo(2);
        assertThat(internalBookList.get(0).getCategory().size()).isEqualTo(2);
    }

    @Test
    void externalToInternalBook() {
        com.exercise.springbootsetup.book.external.Book externalBook = mock(com.exercise.springbootsetup.book.external.Book.class);
        when(externalBook.getIsbn()).thenReturn("1234567890");

        Book internalBook = bookMapper.externalToInternalBook(externalBook);

        assertThat(internalBook.getIsbn()).isEqualTo("1234567890");
    }

    private ZonedDateTime createZonedDateTime(final String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);

        return localDate.atStartOfDay(ZoneId.systemDefault());
    }
}