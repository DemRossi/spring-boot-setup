package com.exercise.springbootsetup.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select new com.exercise.springbootsetup.category.CategoryCountDTO (c.categoryName, count(bc.book_id)) from Category c left outer join book_category bc on c.id = bc.category_id group by c.id")
    List<CategoryCountDTO> getCategoryAndAmountOfBooks();
}
