package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.AbstractTest;
import com.exercise.springbootsetup.query.Query;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class BookControllerTest extends AbstractTest {
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

    @InjectMocks
    private BookController bookController;

    // TODO: Verify gebruiken - DONE
    @Test
    public void getBooks() throws Exception {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);

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
                .containsExactly("asc", null);
    }

    @Test
    public void getBooks_sort_by_title_desc() throws Exception {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);

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
                .containsExactly("desc", null);
    }

    @Test
    public void getBooks_sort_by_title_wrong_sort() throws Exception {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);

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

        String uri = "/api/book?publishedAfter=2011-04-01";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        bookController.getBooks(null,DATE);
        int status = mvcResult.getResponse().getStatus();

        verify(bookService, times(1)).getBooks(queryCaptor.capture());
        assertThat(queryCaptor.getValue())
                .isNotNull()
                .extracting(Query::getSortDir, Query::getPublishedAfter)
                .containsExactly(null, DATE);
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

        String uri = "/api/book?publishedAfter=2014-06-03&sort=asc";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        bookController.getBooks("asc", DATE);
        int status = mvcResult.getResponse().getStatus();

        verify(bookService, times(1)).getBooks(queryCaptor.capture());
        assertThat(queryCaptor.getValue())
                .isNotNull()
                .extracting(Query::getSortDir, Query::getPublishedAfter)
                .containsExactly("asc", DATE);
        assertThat(status).isEqualTo(200);
    }

    @Test
    public void getBookByIsbn() throws Exception {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
        ResponseEntity<Optional<Book>> getBookResponse = bookController.getBookByIsbn("123456789");

        int status = getBookResponse.getStatusCodeValue();

        verify(bookService, times(1)).findBookByIsbn(queryCaptor.capture());
        assertThat(status).isEqualTo(200);
        assertThat(queryCaptor.getValue())
                .isNotNull()
                .extracting(Query::getSortDir, Query::getPublishedAfter, Query::getIsbn)
                .containsExactly(null, null, "123456789");
    }
}