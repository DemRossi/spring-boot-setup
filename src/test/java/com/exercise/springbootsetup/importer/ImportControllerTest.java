package com.exercise.springbootsetup.importer;

import com.exercise.springbootsetup.book.Book;
import com.exercise.springbootsetup.exception.ServiceException;
import com.exercise.springbootsetup.query.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImportControllerTest {
    final String JSON_PATH = "src/main/resources/files/books.json";

    @Mock
    private ImportServiceImpl importService;

    @InjectMocks
    private ImportController importController;

    @Test
    public void saveBooks() throws ServiceException {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);

        ResponseEntity<List<Book>> savedBooksResponse = importController.saveBooks();
        int content = savedBooksResponse.getStatusCodeValue();

        assertThat(content).isNotNull();
        assertThat(content).isEqualTo(201);
        assertThat(savedBooksResponse.getBody()).isNotNull();
        verify(importService, times(1)).saveAllBooks(queryCaptor.capture());
    }

    @Test
    public void getBookByIsbn() throws Exception {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
        ResponseEntity<List<Book>>  getImportResponse = importController.saveBooks();

        int status = getImportResponse.getStatusCodeValue();

        verify(importService, times(1)).saveAllBooks(queryCaptor.capture());
        assertThat(status).isEqualTo(201);
        assertThat(queryCaptor.getValue())
                .isNotNull()
                .extracting(Query::getSortDir, Query::getPublishedAfter, Query::getIsbn, Query::getFilePath)
                .containsExactly(null, null, null, JSON_PATH);
    }
}