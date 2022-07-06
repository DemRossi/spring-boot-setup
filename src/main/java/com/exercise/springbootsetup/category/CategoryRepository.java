package com.exercise.springbootsetup.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "select new com.exercise.springbootsetup.category.CategoryCountDTO (c.categoryName, count(bc.book_id)) from Category c left outer join books_categories bc on c.id = bc.category_id group by c.id")
    List<CategoryCountDTO> getCategoryAndAmountOfBooks();

    Optional<Category> findCategoryByCategoryName(String categoryName);
}
