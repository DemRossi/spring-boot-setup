package com.exercise.springbootsetup.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Map<String, Long> getCategoryAndAmountOfBooks() {
        return categoryRepository.getCategoryAndAmountOfBooks().stream()
                .collect(Collectors.toMap(CategoryCountDTO::getCategoryName, CategoryCountDTO::getBookCount));
    }
}
