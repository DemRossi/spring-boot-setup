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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BookMapperImplTest {
    @InjectMocks
    BookMapperImpl bookMapper;

    @Test
    void externalToInternalBooks() {
        List<com.exercise.springbootsetup.book.external.Book> externalBookList = new ArrayList<>();
        com.exercise.springbootsetup.book.external.Book book1 = new com.exercise.springbootsetup.book.external.Book();
        book1.setIsbn("1234567890");
        book1.setTitle("Dit is een titel");
        book1.setPageCount(123);
        book1.setPublishedDate(createZonedDateTime("2014-04-01"));

        com.exercise.springbootsetup.book.external.Book book2 = new com.exercise.springbootsetup.book.external.Book();
        book2.setIsbn("0987654321");
        book2.setTitle("Dit is een andere titel");
        book2.setPageCount(456);
        book2.setPublishedDate(createZonedDateTime("2011-01-11"));

        externalBookList.add(book1);
        externalBookList.add(book2);

        List<Book> internalBookList = bookMapper.externalToInternalBooks(externalBookList);

        assertThat(internalBookList).isNotNull();
        assertThat(internalBookList.size()).isEqualTo(2);
        assertThat(internalBookList.get(0).getIsbn()).isEqualTo("1234567890");
    }

    @Test
    void externalToInternalBook() {
        com.exercise.springbootsetup.book.external.Book externalBook = new com.exercise.springbootsetup.book.external.Book();
        externalBook.setIsbn("1234567890");
        externalBook.setTitle("Dit is een titel");
        externalBook.setPageCount(123);
        externalBook.setPublishedDate(createZonedDateTime("2014-04-01"));

        Book internalBook = bookMapper.externalToInternalBook(externalBook);

        assertThat(internalBook.getIsbn()).isEqualTo("1234567890");
    }

    private ZonedDateTime createZonedDateTime(final String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);

        return localDate.atStartOfDay(ZoneId.systemDefault());
    }
}