package com.exercise.springbootsetup.category;

import com.exercise.springbootsetup.bookAuthor.BookAuthor;
import com.exercise.springbootsetup.bookCategory.BookCategory;
import com.exercise.springbootsetup.bookCategory.BookCategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookCategoryServiceImpl bookCategoryService;

    @Override
    public Map<String, Long> getCategoriesWithAmountOfBooks() {
        //  bookCategoryRepository.findAll() needs an ID, since it's a many-to-many table it can't make an ID and update
        //  all others entities once created => Made ID class
        return countAndCreateMap(categoryRepository.findAll(), bookCategoryService.getBookCategories());
    }

    private Map<String, Long> countAndCreateMap(List<Category> categoryList, List<BookCategory> bookAuthorList){
        Map<String, Long> categoryAndBookCount = new HashMap<>();

        for (Category category : categoryList) {
            Long amountOfBooks = bookAuthorList.stream().filter(ba-> Objects.equals(ba.getCategory_id(), category.getId())).count();
            categoryAndBookCount.put(category.getCategoryName(), amountOfBooks);
        }

        return categoryAndBookCount;
    }
}
