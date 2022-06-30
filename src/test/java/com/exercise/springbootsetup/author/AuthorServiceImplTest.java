package com.exercise.springbootsetup.author;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceImplTest {
    @Mock
    AuthorRepository authorRepository;

    @Mock
    AuthorCountDTO dto1;

    @Mock
    AuthorCountDTO dto2;

    @InjectMocks
    AuthorServiceImpl authorService;

    @Test
    void getAuthorAndAmountOfBooks() {
        Map<String, Long> authorBookCountMap = authorService.getAuthorAndAmountOfBooks();

        verify(authorRepository, times(1)).getAuthorAndAmountOfBooks();
        assertThat(authorBookCountMap).isNotNull();
    }

    @Test
    void getAuthorAndAmountOfBooks_with_list_expect_map() {
        when(dto1.getFullName()).thenReturn("Wes");
        when(dto1.getBookCount()).thenReturn(1L);

        when(dto2.getFullName()).thenReturn("Jef");
        when(dto2.getBookCount()).thenReturn(2L);

        List<AuthorCountDTO> dtoList = new ArrayList<>();

        dtoList.add(dto1);
        dtoList.add(dto2);

        when(authorRepository.getAuthorAndAmountOfBooks()).thenReturn(dtoList);

        Map<String, Long> authorBookCountMap = authorService.getAuthorAndAmountOfBooks();

        assertThat(authorBookCountMap).isNotNull();
        assertThat(authorBookCountMap).isNotEmpty();
        assertThat(authorBookCountMap.size()).isEqualTo(2);
        assertThat(authorBookCountMap.get("Wes")).isEqualTo(1L);
    }
}