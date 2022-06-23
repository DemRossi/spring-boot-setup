package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.AbstractTest;
import com.exercise.springbootsetup.exception.ServiceException;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class BookControllerTest extends AbstractTest {

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @MockBean
    private BookRepository bookRepository;

    @Mock
    private BookServiceImpl bookService;

    @InjectMocks
    private BookController bookController;

    @Test
    public void saveBooks() throws ServiceException {
        ResponseEntity<List<Book>> savedBooksResponse = bookController.saveBooks();

        int content = savedBooksResponse.getStatusCodeValue();

        assertThat(content).isNotNull();
        assertThat(content).isEqualTo(201);
        assertThat(savedBooksResponse.getBody()).isNotNull();
    }

    @Test
    public void getBooks() throws Exception {
        String uri = "/api/book";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(200);
    }

    @Test
    public void getBooks_sort_by_title_asc() throws Exception {
        String uri = "/api/book?sort=asc";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(200);
    }

    @Test
    public void getBooks_sort_by_title_desc() throws Exception {
        String uri = "/api/book?sort=desc";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(200);
    }

    @Test
    public void getBooks_sort_by_title_wrong_sort() throws Exception {
        String uri = "/api/book?sort=jef";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        assertThat(status).isEqualTo(400);
        assertThat(content).contains("Exception while getting books: Can only use asc or desc for sorting");
    }

    @Test
    public void getBooks_after_published_date() throws Exception {
        String uri = "/api/book?publishedAfter=2014-06-03";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertThat(status).isEqualTo(200);
    }

    @Test
    public void getBooks_after_published_date_wrong_input() throws Exception {
        String uri = "/api/book?publishedAfter=qwerty";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        assertThat(status).isEqualTo(400);
        assertThat(content).contains("Exception while getting books: Something is wrong with the date format");
    }

    @Test
    public void getBooks_after_published_date_sort_by_title_asc() throws Exception {
        String uri = "/api/book?publishedAfter=2014-06-03&sort=asc";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();

        assertThat(status).isEqualTo(200);
    }

}