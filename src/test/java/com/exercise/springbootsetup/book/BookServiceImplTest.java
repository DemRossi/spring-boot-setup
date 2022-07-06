package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.author.Author;
import com.exercise.springbootsetup.author.AuthorRepository;
import com.exercise.springbootsetup.category.Category;
import com.exercise.springbootsetup.category.CategoryRepository;
import com.exercise.springbootsetup.exception.ServiceException;
import com.exercise.springbootsetup.query.Query;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    final String DATE = "2011-04-01";

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    BookServiceImplTest() {
    }

    @Test
    void save_with_new_isbn_expect_repository_called_once() throws ServiceException {
        Book book = mock(Book.class);

        when(book.getIsbn()).thenReturn("123345677890");
        when(bookRepository.findBookByIsbn(book.getIsbn())).thenReturn(Optional.empty());
        when(bookRepository.save(book)).thenReturn(book);

        bookService.save(book);

        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void save_with_existing_author_expect_saved_with_existing_author() throws ServiceException {
        Book book = mock(Book.class);
        Author bodyAuthor = mock(Author.class);
        Author existingAuthor = mock(Author.class);

        Set<Author> authors = new HashSet<>();
        authors.add(bodyAuthor);

        Set<Author> existingAuthors = new HashSet<>();
        existingAuthors.add(existingAuthor);

        when(book.getIsbn()).thenReturn("123345677890");
        when(bookRepository.findBookByIsbn(book.getIsbn())).thenReturn(Optional.empty());
        when(book.getAuthors()).thenReturn(authors);
        when(bodyAuthor.getFullName()).thenReturn("tester");
        when(authorRepository.findAuthorByFullName(bodyAuthor.getFullName())).thenReturn(Optional.of(existingAuthor));
        when(bookRepository.save(book)).thenReturn(book);

        Book savedBook = bookService.save(book);

        when(savedBook.getAuthors()).thenReturn(existingAuthors);

        verify(bookRepository, times(1)).save(book);
        assertThat(savedBook.getAuthors()).contains(existingAuthor);
    }

    @Test
    void save_with_new_author_expect_saved_with_new_author() throws ServiceException {
        Book book = mock(Book.class);
        Author bodyAuthor = mock(Author.class);

        Set<Author> authors = new HashSet<>();
        authors.add(bodyAuthor);

        when(book.getIsbn()).thenReturn("123345677890");
        when(bookRepository.findBookByIsbn(book.getIsbn())).thenReturn(Optional.empty());
        when(book.getAuthors()).thenReturn(authors);
        when(bodyAuthor.getFullName()).thenReturn("tester");
        when(authorRepository.findAuthorByFullName(bodyAuthor.getFullName())).thenReturn(Optional.empty());
        when(bookRepository.save(book)).thenReturn(book);

        Book savedBook = bookService.save(book);

        verify(bookRepository, times(1)).save(book);
        assertThat(savedBook.getAuthors()).contains(bodyAuthor);
    }

    @Test
    void save_with_existing_category_expect_saved_with_existing_category() throws ServiceException {
        Book book = mock(Book.class);
        Category bodyCategory = mock(Category.class);
        Category existingCategory = mock(Category.class);

        Set<Category> categories = new HashSet<>();
        categories.add(bodyCategory);

        Set<Category> existingCategories = new HashSet<>();
        existingCategories.add(existingCategory);

        when(book.getIsbn()).thenReturn("123345677890");
        when(bookRepository.findBookByIsbn(book.getIsbn())).thenReturn(Optional.empty());

        when(book.getCategories()).thenReturn(categories);
        when(bodyCategory.getCategoryName()).thenReturn("tester");
        when(categoryRepository.findCategoryByCategoryName(bodyCategory.getCategoryName())).thenReturn(Optional.of(existingCategory));

        when(bookRepository.save(book)).thenReturn(book);

        Book savedBook = bookService.save(book);

        when(savedBook.getCategories()).thenReturn(existingCategories);

        verify(bookRepository, times(1)).save(book);
        assertThat(savedBook.getCategories()).contains(existingCategory);
    }

    @Test
    void save_with_new_category_expect_saved_with_new_category() throws ServiceException {
        Book book = mock(Book.class);
        Category bodyCategory = mock(Category.class);

        Set<Category> categories = new HashSet<>();
        categories.add(bodyCategory);

        when(book.getIsbn()).thenReturn("123345677890");
        when(bookRepository.findBookByIsbn(book.getIsbn())).thenReturn(Optional.empty());

        when(book.getCategories()).thenReturn(categories);
        when(bodyCategory.getCategoryName()).thenReturn("tester");
        when(categoryRepository.findCategoryByCategoryName(bodyCategory.getCategoryName())).thenReturn(Optional.empty());

        when(bookRepository.save(book)).thenReturn(book);

        Book savedBook = bookService.save(book);

        verify(bookRepository, times(1)).save(book);
        assertThat(savedBook.getCategories()).contains(bodyCategory);
    }

    @Test
    void save_with_duplicate_isbn_expect_exception() {
        Book book = mock(Book.class);

        when(book.getIsbn()).thenReturn("1933988673");
        when(bookRepository.findBookByIsbn(book.getIsbn())).thenReturn(Optional.of(book));

        Exception exception = assertThrows(ServiceException.class, () ->
            bookService.save(book)
        );

        String expectedMessage = "ISBN 1933988673 already exist in the database";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void deleteById_correct_id_expect_delete(){
        Query filter = mock(Query.class);
        when(filter.getId()).thenReturn(1L);
        when(bookRepository.findById(filter.getId()))
                .thenReturn(Optional.of(mock(Book.class)));

        Optional<Book> book = bookService.deleteById(filter);

        assertThat(book.isPresent()).isTrue();
    }

    @Test
    void deleteById_id_not_found_expect_empty_optional(){
        Query filter = mock(Query.class);
        when(filter.getId()).thenReturn(1L);

        Optional<Book> book = bookService.deleteById(filter);

        assertThat(book.isPresent()).isFalse();
    }

    @Test
    void getBooks_both_params_null() throws ServiceException {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);

        Query filter = mock(Query.class);
        when(filter.getSortDir()).thenReturn(null);
        when(filter.getPublishedAfter()).thenReturn(null);

        Optional<List<Book>> getBooks = bookService.getBooks(filter);

        assertThat(getBooks).isNotNull();
        verify(bookRepository, times(1)).getBooks(queryCaptor.capture());
        assertThat(queryCaptor.getValue())
                .isNotNull()
                .extracting(Query::getSortDir, Query::getPublishedAfter)
                .containsExactly(null, null);
    }

    @Test
    void getBooks_sortDir_filled_in() throws ServiceException {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);

        Query filter = mock(Query.class);
        when(filter.getSortDir()).thenReturn("asc");
        when(filter.getPublishedAfter()).thenReturn(null);

        Optional<List<Book>> getBooks = bookService.getBooks(filter);

        assertThat(getBooks).isNotNull();
        verify(bookRepository, times(1)).getBooks(queryCaptor.capture());
        assertThat(queryCaptor.getValue())
                .isNotNull()
                .extracting(Query::getSortDir, Query::getPublishedAfter)
                .containsExactly("asc", null);
    }

    @Test
    void getBooks_date_filled_in() throws ServiceException {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);

        Query filter = Query.builder()
                .sortDir(null)
                .publishedAfter(DATE)
                .build();

        Optional<List<Book>> getBooks = bookService.getBooks(filter);

        assertThat(getBooks).isNotNull();
        verify(bookRepository, times(1)).getBooks(queryCaptor.capture());
        assertThat(queryCaptor.getValue())
                .isNotNull()
                .extracting(Query::getSortDir, Query::getPublishedAfter)
                .containsExactly( null, DATE);
    }

    @Test
    void getBooks_both_filled_in() throws ServiceException {
        ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
        Query filter = mock(Query.class);
        when(filter.getSortDir()).thenReturn("asc");
        when(filter.getPublishedAfter()).thenReturn(DATE);

        Optional<List<Book>> getBooks = bookService.getBooks(filter);

        assertThat(getBooks).isNotNull();
        verify(bookRepository, times(1)).getBooks(queryCaptor.capture());
        assertThat(queryCaptor.getValue())
                .isNotNull()
                .extracting(Query::getSortDir, Query::getPublishedAfter)
                .containsExactly("asc", DATE);
    }

    @Test
    void findBookByIsbn() throws ServiceException {
        Query filter = mock(Query.class);
        when(bookRepository.findBookByIsbn("1234567890")).thenReturn(Optional.of(mock(Book.class)));
        when(filter.getIsbn()).thenReturn("1234567890");

        Optional<Book> book = bookService.findBookByIsbn(filter);
        when(book.get().getId()).thenReturn(1L);
        when(book.get().getIsbn()).thenReturn("1234567890");

        verify(bookRepository, times(1)).findBookByIsbn(filter.getIsbn());
        assertThat(book.get().getId()).isEqualTo(1L);
        assertThat(book.get().getIsbn()).isEqualTo("1234567890");
    }

    @Test
    void findBookByIsbn_no_result() {
        Query filter = mock(Query.class);
        when(bookRepository.findBookByIsbn("1234567890")).thenReturn(Optional.empty());
        when(filter.getIsbn()).thenReturn("1234567890");

        Exception exception = assertThrows(ServiceException.class, () -> {
            Optional<Book> book = bookService.findBookByIsbn(filter);
        });

        String expectedMessage = "Book not found: Check the ISBN, if correct the book doesn't exist in our DB";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        verify(bookRepository, times(1)).findBookByIsbn(filter.getIsbn());
    }

    @Test
    void validate_nothing_filled_in_expect_no_error() {
        Query filter = mock(Query.class);

        assertDoesNotThrow(() -> bookService.validate(filter));
    }

    @Test
    void validate_all_correctly_filled_in_expect_no_error() {
        Query filter = mock(Query.class);

        when(filter.getSortDir()).thenReturn("asc");
        when(filter.getPublishedAfter()).thenReturn(DATE);

        assertDoesNotThrow(() -> bookService.validate(filter));
    }

    @Test
    void validate_sort_direction_filled_in_expect_no_error() {
        Query filter = mock(Query.class);

        when(filter.getSortDir()).thenReturn("asc");

        assertDoesNotThrow(() -> bookService.validate(filter));
    }

    @Test
    void validate_date_filled_in_expect_no_error() {
        Query filter = mock(Query.class);

        when(filter.getPublishedAfter()).thenReturn(DATE);

        assertDoesNotThrow(() -> bookService.validate(filter));
    }

    @Test
    void validate_wrong_sort_direction_filled_in_expect_error() {
        Query filter = mock(Query.class);
        when(filter.getSortDir()).thenReturn("jef");

        Exception exception = assertThrows(ServiceException.class, () ->
            bookService.validate(filter)
        );

        String expectedMessage = "Sort parameter can only be asc or desc";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void validate_wrong_date_filled_in_expect_error() {
        Query filter = mock(Query.class);
        when(filter.getPublishedAfter()).thenReturn("qwerty");

        Exception exception = assertThrows(ServiceException.class, () ->
            bookService.validate(filter)
        );

        String expectedMessage = "Something is wrong with the date format";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}