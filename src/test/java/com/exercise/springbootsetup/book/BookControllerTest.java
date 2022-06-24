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
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class BookControllerTest extends AbstractTest {
    final String JSON_PATH = "src/main/resources/files/books.json";
    final String SORT_DIR_WRONG = "JEFF";
    final String DATE = "2011-04-01";
    final String NOT_DATE = "BLA BLA";


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
        String uri = "/api/book";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        ResponseEntity<Optional<List<Book>>> getBooksResponse = bookController.getBooks(null,null);

        assertThat(status).isEqualTo(200);
        verify(bookService, times(1)).getBooks(null,null);
    }

    @Test
    public void getBooks_sort_by_title_asc() throws Exception {
        String uri = "/api/book?sort=asc";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        ResponseEntity<Optional<List<Book>>> getBooksResponse = bookController.getBooks(Sort.Direction.ASC.name(), null);

        assertThat(status).isEqualTo(200);
        verify(bookService, times(1)).getBooks(Sort.Direction.ASC.name(),null);
    }

    @Test
    public void getBooks_sort_by_title_desc() throws Exception {
        String uri = "/api/book?sort=desc";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        ResponseEntity<Optional<List<Book>>> getBooksResponse = bookController.getBooks(Sort.Direction.DESC.name(), null);

        assertThat(status).isEqualTo(200);
        verify(bookService, times(1)).getBooks(Sort.Direction.DESC.name(),null);
    }

    @Test
    public void getBooks_sort_by_title_wrong_sort() throws Exception {
        String uri = "/api/book?sort=jef";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        ResponseEntity<Optional<List<Book>>> getBooksResponse = bookController.getBooks(SORT_DIR_WRONG,null);
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        verify(bookService, times(1)).getBooks(SORT_DIR_WRONG,null);
        assertThat(status).isEqualTo(400);
        assertThat(content).contains("Exception while getting books: Can only use asc or desc for sorting");
    }

    @Test
    public void getBooks_after_published_date() throws Exception {
        String uri = "/api/book?publishedAfter=2014-06-03";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        ResponseEntity<Optional<List<Book>>> getBooksResponse = bookController.getBooks(null,DATE);
        int status = mvcResult.getResponse().getStatus();

        verify(bookService, times(1)).getBooks(null,DATE);
        assertThat(status).isEqualTo(200);
    }

    @Test
    public void getBooks_after_published_date_wrong_input() throws Exception {
        String uri = "/api/book?publishedAfter=qwerty";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        ResponseEntity<Optional<List<Book>>> getBooksResponse = bookController.getBooks(null,NOT_DATE);
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();

        verify(bookService, times(1)).getBooks(null, NOT_DATE);
        assertThat(status).isEqualTo(400);
        assertThat(content).contains("Exception while getting books: Something is wrong with the date format");
    }

    @Test
    public void getBooks_after_published_date_sort_by_title_asc() throws Exception {
        String uri = "/api/book?publishedAfter=2014-06-03&sort=asc";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON)).andReturn();

        ResponseEntity<Optional<List<Book>>> getBooksResponse = bookController.getBooks(Sort.Direction.ASC.name(), DATE);
        int status = mvcResult.getResponse().getStatus();

        verify(bookService, times(1)).getBooks(Sort.Direction.ASC.name(), DATE);
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