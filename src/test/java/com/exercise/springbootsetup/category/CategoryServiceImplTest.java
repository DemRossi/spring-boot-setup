package com.exercise.springbootsetup.category;

import com.exercise.springbootsetup.bookCategory.BookCategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    CategoryRepository categoryRepository;

    @Mock
    BookCategoryServiceImpl bookCategoryService;

    @InjectMocks
    CategoryServiceImpl categoryService;


    @Test
    void getCategoriesWithAmountOfBooks() {
        Map<String, Long> categoryBookCountMap = categoryService.getCategoriesWithAmountOfBooks();

        assertThat(categoryBookCountMap).isNotNull();
        verify(categoryRepository, times(1)).findAll();
        verify(bookCategoryService, times(1)).getBookCategories();
    }
}