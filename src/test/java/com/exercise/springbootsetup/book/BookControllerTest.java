package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.AbstractTest;
import com.exercise.springbootsetup.exception.ServiceException;
import com.exercise.springbootsetup.query.Query;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
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
import static org.mockito.Mockito.*;

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
    public void saveBook_with_correct_body_expect_book_saved() throws ServiceException {
        Book book = mock(Book.class);

        when(bookService.save(book)).thenReturn(book);
        bookController.saveBook(book);

        verify(bookService, times(1)).save(book);
    }

    @Test
    public void deleteBook_correct_id_expect_book_removed_message(){
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);

        when(bookService.deleteById(queryCaptor.capture())).thenReturn(Optional.of(mock(Book.class)));
        ResponseEntity<String> deleteBookResponse = bookController.deleteBook(1L);

        verify(bookService, times(1)).deleteById(queryCaptor.capture());
        assertThat(deleteBookResponse.getBody()).contains("Removed book with title:");
        assertThat(deleteBookResponse.getStatusCodeValue()).isEqualTo(200);

    }

    @Test
    public void deleteBook_correct_id_expect_book_not_found_message(){
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);

        ResponseEntity<String> deleteBookResponse = bookController.deleteBook(1L);

        verify(bookService, times(1)).deleteById(queryCaptor.capture());
        assertThat(deleteBookResponse.getBody()).contains("Book with ID");
        assertThat(deleteBookResponse.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void deleteBook_incorrect_id_type_expect_exception() throws Exception {
        String uri = "/api/book/bla";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        String expectedMessage = "Failed to convert value of type";
        String actualMessage = mvcResult.getResponse().getContentAsString();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
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

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}