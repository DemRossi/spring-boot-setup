package com.exercise.springbootsetup.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>,CustomizedBookRepository {
    Optional<Book> findBookByIsbn(String isbn);
}
