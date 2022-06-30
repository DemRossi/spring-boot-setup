package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.exception.ServiceException;
import com.exercise.springbootsetup.query.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomizedBookRepositoryImplTest {
    @Mock
    EntityManager entityManager;

    @Mock
    Book book1;

    @Mock
    Book book2;

    @Mock
    TypedQuery<Book> bookTypedQuery;

    @InjectMocks
    CustomizedBookRepositoryImpl customizedBookRepository;

    @Test
    void getBooks_empty_filter() throws ServiceException {
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);

        Query filter = mock(Query.class);
        given(entityManager.createQuery(anyString(), ArgumentMatchers.<Class<Book>>any())).willReturn(bookTypedQuery);

        when(bookTypedQuery.getResultList()).thenReturn(books);

        customizedBookRepository.getBooks(filter);

        verify(entityManager).createQuery("select b from Book b", Book.class);
    }

    @Test
    void getBooks_sort_dir_filter() throws ServiceException {
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);

        Query filter = mock(Query.class);
        when(filter.getSortDir()).thenReturn("desc");
        given(entityManager.createQuery(anyString(), ArgumentMatchers.<Class<Book>>any())).willReturn(bookTypedQuery);

        when(bookTypedQuery.getResultList()).thenReturn(books);

        customizedBookRepository.getBooks(filter);

        verify(entityManager).createQuery("select b from Book b order by b.title desc", Book.class);
    }

    @Test
    void getBooks_after_date_filter() throws ServiceException {
        ZonedDateTime dateTime = ZonedDateTime.parse("2011-04-01T00:00+02:00[Europe/Paris]");
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);

        Query filter = mock(Query.class);
        when(filter.getPublishedAfter()).thenReturn("2011-04-01");
        given(entityManager.createQuery(anyString(), ArgumentMatchers.<Class<Book>>any())).willReturn(bookTypedQuery);
        when(bookTypedQuery.setParameter("date", dateTime)).thenReturn(bookTypedQuery);
        when(bookTypedQuery.getResultList()).thenReturn(books);

        customizedBookRepository.getBooks(filter);

        verify(entityManager).createQuery("select b from Book b where b.publishedDate between :date and DATETIME('now','localtime')", Book.class);
    }

    @Test
    void getBooks_after_date_and_sort_dir_filter() throws ServiceException {
        ZonedDateTime dateTime = ZonedDateTime.parse("2011-04-01T00:00+02:00[Europe/Paris]");
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);

        Query filter = mock(Query.class);
        when(filter.getPublishedAfter()).thenReturn("2011-04-01");
        given(entityManager.createQuery(anyString(), ArgumentMatchers.<Class<Book>>any())).willReturn(bookTypedQuery);
        when(bookTypedQuery.setParameter("date", dateTime)).thenReturn(bookTypedQuery);
        when(filter.getSortDir()).thenReturn("desc");
        given(entityManager.createQuery(anyString(), ArgumentMatchers.<Class<Book>>any())).willReturn(bookTypedQuery);
        when(bookTypedQuery.getResultList()).thenReturn(books);

        customizedBookRepository.getBooks(filter);

        verify(entityManager).createQuery("select b from Book b where b.publishedDate between :date and DATETIME('now','localtime') order by b.title desc", Book.class);
    }
}