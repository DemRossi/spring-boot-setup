package com.exercise.springbootsetup.category;

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
class CategoryServiceImplTest {
    @Mock
    CategoryRepository categoryRepository;

    @Mock
    CategoryCountDTO dto1;

    @Mock
    CategoryCountDTO dto2;

    @InjectMocks
    CategoryServiceImpl categoryService;

    @Test
    void getCategoriesWithAmountOfBooks() {
        Map<String, Long> categoryBookCountMap = categoryService.getCategoryAndAmountOfBooks();

        assertThat(categoryBookCountMap).isNotNull();
        verify(categoryRepository, times(1)).getCategoryAndAmountOfBooks();
    }

    @Test
    void getCategoriesWithAmountOfBooks_with_lists() {
        when(dto1.getCategoryName()).thenReturn("Java");
        when(dto1.getBookCount()).thenReturn(1L);

        when(dto2.getCategoryName()).thenReturn("Spring Boot");
        when(dto2.getBookCount()).thenReturn(2L);

        List<CategoryCountDTO> dtoList = new ArrayList<>();

        dtoList.add(dto1);
        dtoList.add(dto2);

        when(categoryRepository.getCategoryAndAmountOfBooks()).thenReturn(dtoList);

        Map<String, Long> authorBookCountMap = categoryService.getCategoryAndAmountOfBooks();

        assertThat(authorBookCountMap).isNotNull();
        assertThat(authorBookCountMap).isNotEmpty();
        assertThat(authorBookCountMap.size()).isEqualTo(2);
    }
}