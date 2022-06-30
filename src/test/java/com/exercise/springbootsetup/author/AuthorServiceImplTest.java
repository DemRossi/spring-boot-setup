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

    @InjectMocks
    AuthorServiceImpl authorService;

    @Test
    void getAuthorsWithAmountOfBooks() {
        Map<String, Long> authorBookCountMap = authorService.getAuthorAndAmountOfBooks();

        verify(authorRepository, times(1)).getAuthorAndAmountOfBooks();
        assertThat(authorBookCountMap).isNotNull();
    }

    @Test
    void getAuthorsWithAmountOfBooks_with_lists() {
        AuthorCountDTO dto1 = new AuthorCountDTO("Wes", 1L);
        AuthorCountDTO dto2 = new AuthorCountDTO("Jef", 2L);

        List<AuthorCountDTO> dtoList = new ArrayList<>();
        List<AuthorCountDTO> spyDtoList = Mockito.spy(dtoList);

        spyDtoList.add(dto1);
        spyDtoList.add(dto2);

        when(authorRepository.getAuthorAndAmountOfBooks()).thenReturn(spyDtoList);

        Map<String, Long> authorBookCountMap = authorService.getAuthorAndAmountOfBooks();

        assertThat(authorBookCountMap).isNotNull();
        assertThat(authorBookCountMap).isNotEmpty();
        assertThat(authorBookCountMap.size()).isEqualTo(2);
    }
}