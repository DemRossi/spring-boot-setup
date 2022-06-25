package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.query.Query;

import java.util.List;
import java.util.Optional;

public interface CustomizedBookRepository {
    Optional<List<Book>> getBooks(Query filter);
}
