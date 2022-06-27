package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.AbstractTest;
import com.exercise.springbootsetup.exception.ServiceException;
import com.exercise.springbootsetup.query.Query;
import com.exercise.springbootsetup.query.QueryServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class BookControllerTest extends AbstractTest {
    final String JSON_PATH = "src/main/resources/files/books.json";
    final String SORT_DIR_WRONG = "JEFF";
    final String DATE = "2011-04-01";
    final String NOT_DATE = "qwerty";

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Mock
    private BookServiceImpl bookService;

    @Mock
    private QueryServiceImpl queryService;

    @InjectMocks
    private BookController bookController;

    // TODO: Virify gebruiken

    @Test
    public void saveBooks() throws ServiceException {
        ResponseEntity<List<Book>> savedBooksResponse = bookController.saveBooks();
        int content = savedBooksResponse.getStatusCodeValue();

        assertThat(content).isNotNull();
        assertThat(content).isEqualTo(201);
        assertThat(savedBooksResponse.getBody()).isNotNull();
        verify(bookService, times(1)).saveAll(JSON_PATH);

    }

    @Test
    public void getBooks() throws Exception {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
        when(queryService.createSort(null)).thenReturn(null);
        when(queryService.createZonedDateTime(null)).thenReturn(null);

        String uri = "/api/book";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        bookController.getBooks(null,null);

        assertThat(status).isEqualTo(200);
        verify(bookService, times(1)).getBooks(queryCaptor.capture());
        assertThat(queryCaptor.getValue())
                .isNotNull()
                .extracting(Query::getSortDir, Query::getPublishedAfter)
                .containsExactly(null, null);
    }

    @Test
    public void getBooks_sort_by_title_asc() throws Exception {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
        when(queryService.createSort("asc")).thenReturn("ASC");
        when(queryService.createZonedDateTime(null)).thenReturn(null);

        String uri = "/api/book?sort=asc";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        bookController.getBooks("asc", null);

        assertThat(status).isEqualTo(200);
        verify(bookService, times(1)).getBooks(queryCaptor.capture());
        assertThat(queryCaptor.getValue())
                .isNotNull()
                .extracting(Query::getSortDir, Query::getPublishedAfter)
                .containsExactly("ASC", null);
    }

    @Test
    public void getBooks_sort_by_title_desc() throws Exception {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
        when(queryService.createSort("desc")).thenReturn("DESC");
        when(queryService.createZonedDateTime(null)).thenReturn(null);

        String uri = "/api/book?sort=desc";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        bookController.getBooks("desc", null);

        assertThat(status).isEqualTo(200);
        verify(bookService, times(1)).getBooks(queryCaptor.capture());
        assertThat(queryCaptor.getValue())
                .isNotNull()
                .extracting(Query::getSortDir, Query::getPublishedAfter)
                .containsExactly("DESC", null);
    }

    @Test
    public void getBooks_sort_by_title_wrong_sort() throws Exception {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
        when(queryService.createSort(SORT_DIR_WRONG)).thenReturn(SORT_DIR_WRONG);
        when(queryService.createZonedDateTime(null)).thenReturn(null);

        String uri = "/api/book?sort=jef";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        bookController.getBooks(SORT_DIR_WRONG,null);
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        verify(bookService, times(1)).getBooks(queryCaptor.capture());
        assertThat(queryCaptor.getValue())
                .isNotNull()
                .extracting(Query::getSortDir, Query::getPublishedAfter)
                .containsExactly(SORT_DIR_WRONG, null);
        assertThat(status).isEqualTo(400);
        assertThat(content).contains("Exception while getting books: Sort parameter can only be asc or desc");
    }

    @Test
    public void getBooks_after_published_date() throws Exception {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
        when(queryService.createSort(null)).thenReturn(null);
        when(queryService.createZonedDateTime(DATE)).thenReturn(ZonedDateTime.parse("2011-04-01T00:00+02:00[Europe/Paris]"));

        String uri = "/api/book?publishedAfter=2011-04-01";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        bookController.getBooks(null,DATE);
        int status = mvcResult.getResponse().getStatus();

        verify(bookService, times(1)).getBooks(queryCaptor.capture());
        assertThat(queryCaptor.getValue())
                .isNotNull()
                .extracting(Query::getSortDir, Query::getPublishedAfter)
                .containsExactly(null, ZonedDateTime.parse("2011-04-01T00:00+02:00[Europe/Paris]"));
        assertThat(status).isEqualTo(200);
    }

    @Test
    public void getBooks_after_published_date_wrong_input() throws Exception {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);

        String uri = "/api/book?publishedAfter=qwerty";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        bookController.getBooks(null, NOT_DATE);
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        verify(bookService, times(1)).getBooks(queryCaptor.capture());
        assertThat(status).isEqualTo(400);
        assertThat(content).contains("Exception while getting books: Something is wrong with the date format");
    }

    @Test
    public void getBooks_after_published_date_sort_by_title_asc() throws Exception {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
        when(queryService.createSort("asc")).thenReturn("ASC");
        when(queryService.createZonedDateTime(DATE)).thenReturn(ZonedDateTime.parse("2011-04-01T00:00+02:00[Europe/Paris]"));

        String uri = "/api/book?publishedAfter=2014-06-03&sort=asc";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        bookController.getBooks("asc", DATE);
        int status = mvcResult.getResponse().getStatus();

        verify(bookService, times(1)).getBooks(queryCaptor.capture());
        assertThat(queryCaptor.getValue())
                .isNotNull()
                .extracting(Query::getSortDir, Query::getPublishedAfter)
                .containsExactly("ASC", ZonedDateTime.parse("2011-04-01T00:00+02:00[Europe/Paris]"));
        assertThat(status).isEqualTo(200);
    }

    @Test
    public void getBookByIsbn() throws Exception {
        ResponseEntity<Optional<Book>> getBookResponse = bookController.getBookByIsbn("123456789");
        int status = getBookResponse.getStatusCodeValue();

        verify(bookService, times(1)).findBookByIsbn("123456789");
        assertThat(status).isEqualTo(200);
    }

}