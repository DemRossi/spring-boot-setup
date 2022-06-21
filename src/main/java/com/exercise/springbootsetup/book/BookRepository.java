package com.exercise.springbootsetup.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findBookByIsbn(String isbn);

    Optional<List<Book>> findAllByPublishedDateAfter(ZonedDateTime publishedDate);
}
