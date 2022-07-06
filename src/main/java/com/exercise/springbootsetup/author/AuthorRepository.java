package com.exercise.springbootsetup.author;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query(value = "select new com.exercise.springbootsetup.author.AuthorCountDTO (a.fullName, count(ba.book_id)) from Author a left outer join books_authors ba on a.id = ba.author_id group by a.id")
    List<AuthorCountDTO> getAuthorAndAmountOfBooks();

    Optional<Author> findAuthorByFullName(String fullName);
}
