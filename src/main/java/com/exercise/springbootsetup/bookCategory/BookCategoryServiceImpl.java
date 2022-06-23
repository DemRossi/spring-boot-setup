package com.exercise.springbootsetup.bookCategory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(" bookCategoryService")
public class BookCategoryServiceImpl implements BookCategoryService {
    @Autowired
    BookCategoryRepository bookCategoryRepository;

    @Override
    public List<BookCategory> getBookCategories() {
        return bookCategoryRepository.findAll();
    }
}
