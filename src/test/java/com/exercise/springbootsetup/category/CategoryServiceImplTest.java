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
        CategoryCountDTO dto1 = new CategoryCountDTO("test", 1L);
        CategoryCountDTO dto2 = new CategoryCountDTO("Anders", 2L);
        List<CategoryCountDTO> dtoList = new ArrayList<>();
        List<CategoryCountDTO> spyDtoList = Mockito.spy(dtoList);

        spyDtoList.add(dto1);
        spyDtoList.add(dto2);

        when(categoryRepository.getCategoryAndAmountOfBooks()).thenReturn(spyDtoList);

        Map<String, Long> authorBookCountMap = categoryService.getCategoryAndAmountOfBooks();

        assertThat(authorBookCountMap).isNotNull();
        assertThat(authorBookCountMap).isNotEmpty();
        assertThat(authorBookCountMap.size()).isEqualTo(2);
    }
}