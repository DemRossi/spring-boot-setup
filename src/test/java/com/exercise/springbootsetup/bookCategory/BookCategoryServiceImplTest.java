package com.exercise.springbootsetup.bookCategory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookCategoryServiceImplTest {
    @Mock
    BookCategoryRepository bookCategoryRepository;

    @InjectMocks
    BookCategoryServiceImpl bookCategoryService;

    @Test
    void getBookCategories() {
        BookCategory bookCategory = mock(BookCategory.class);

        List<BookCategory> list = Arrays.asList(bookCategory,bookCategory);
        when(bookCategoryRepository.findAll()).thenReturn(list);

        List<BookCategory> bookCategories = bookCategoryService.getBookCategories();

        assertThat(bookCategories).isNotNull();
        assertThat(bookCategories).isNotEmpty();
    }
}