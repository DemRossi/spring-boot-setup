package com.exercise.springbootsetup.bookCategory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BookCategoryServiceImplTest {
    @Mock
    BookCategoryRepository bookCategoryRepository;

    @InjectMocks
    BookCategoryServiceImpl bookCategoryService;

    @Test
    void getBookCategories() {
        List<BookCategory> bookCategories = bookCategoryService.getBookCategories();

        assertThat(bookCategories).isNotNull();
    }
}