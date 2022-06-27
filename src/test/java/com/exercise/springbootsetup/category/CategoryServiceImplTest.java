package com.exercise.springbootsetup.category;

import com.exercise.springbootsetup.bookCategory.BookCategory;
import com.exercise.springbootsetup.bookCategory.BookCategoryServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    BookCategoryServiceImpl bookCategoryService;

    @InjectMocks
    CategoryServiceImpl categoryService;

    static List<Category> categories = new ArrayList<>();
    static List<BookCategory> bookCategories = new ArrayList<>();

    @BeforeAll
    public static void setup(){
        Category c1 = new Category();
        c1.setId(1L);
        c1.setCategoryName("Internet");

        Category c2 = new Category();
        c2.setId(2L);
        c2.setCategoryName("Java");

        BookCategory ba1 = BookCategory.builder()
                .category_id(1L)
                .book_id(5L)
                .build();

        BookCategory ba2 = BookCategory.builder()
                .category_id(1L)
                .book_id(7L)
                .build();

        BookCategory ba3 = BookCategory.builder()
                .category_id(2L)
                .book_id(6L)
                .build();
        BookCategory ba4 = BookCategory.builder()
                .category_id(1L)
                .book_id(8L)
                .build();
        BookCategory ba5 = BookCategory.builder()
                .category_id(2L)
                .book_id(8L)
                .build();

        categories.add(c1);
        categories.add(c2);

        bookCategories.add(ba1);
        bookCategories.add(ba2);
        bookCategories.add(ba3);
        bookCategories.add(ba4);
        bookCategories.add(ba5);
    }

    @Test
    void getCategoriesWithAmountOfBooks() {
        Map<String, Long> categoryBookCountMap = categoryService.getCategoriesWithAmountOfBooks();

        assertThat(categoryBookCountMap).isNotNull();
        verify(categoryRepository, times(1)).findAll();
        verify(bookCategoryService, times(1)).getBookCategories();
    }

    @Test
    void etCategoriesWithAmountOfBooks_with_lists() {
        when(categoryRepository.findAll()).thenReturn(categories);
        when(bookCategoryService.getBookCategories()).thenReturn(bookCategories);
        Map<String, Long> authorBookCountMap = categoryService.getCategoriesWithAmountOfBooks();

        assertThat(authorBookCountMap).isNotNull();
        assertThat(authorBookCountMap).isNotEmpty();
        assertThat(authorBookCountMap.size()).isEqualTo(2);
    }
}