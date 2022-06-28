package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.author.Author;
import com.exercise.springbootsetup.category.Category;
import com.exercise.springbootsetup.exception.ServiceException;
import com.exercise.springbootsetup.query.Query;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service(" bookService")
public class BookServiceImpl implements BookService{
    @Autowired
    private BookRepository bookRepository;

    Set<Author> authorSet = new HashSet<>();
    Set<Category> categorySet = new HashSet<>();

    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();

    @Override
    public List<Book> saveAll( String filePath) throws ServiceException {
        return bookRepository.saveAll(this.getBooksFromFile(filePath));
    }

    @Override
    public List<Book> getBooksFromFile(final String filePath) throws ServiceException {
        try{
            if (StringUtils.isBlank(filePath)){
                throw new FileNotFoundException("No path given");
            }
            return this.externalToInternalBooks(Arrays.asList(mapper.readValue(new FileReader(filePath), com.exercise.springbootsetup.models.external.Book[].class)));
        }catch (IOException e){
            throw new ServiceException("Exception while retrieving books from file: " + e.getMessage(), e);
        }
    }

    // TODO: Make mapper -> out of service
    @Override
    public List<Book> externalToInternalBooks(List<com.exercise.springbootsetup.models.external.Book> source) {
        return source.stream().map(this::externalToInternalBook).collect(Collectors.toList());
    }

    // TODO: Make mapper -> out of service
    @Override
    public Book externalToInternalBook(com.exercise.springbootsetup.models.external.Book source) {
        Book internalBook = null;

        if (source != null){
            internalBook = new Book();
            internalBook.setTitle(source.getTitle());
            internalBook.setIsbn(source.getIsbn());
            internalBook.setPageCount(source.getPageCount());
            internalBook.setPublishedDate(source.getPublishedDate());
            internalBook.setThumbnailUrl(source.getThumbnailUrl());
            internalBook.setShortDescription(source.getShortDescription());
            internalBook.setLongDescription(source.getLongDescription());
            internalBook.setStatus(source.getStatus());
            internalBook.setAuthor(getAuthor(source.getAuthors()));
            internalBook.setCategory(getCategory(source.getCategories()));
        }

        return internalBook;
    }

    // TODO: refactor to filterObject (of query object)
    @Override
    public Optional<List<Book>> getBooks(Query filter) {
        return bookRepository.getBooks(filter);
    }


    @Override
    public Optional<Book> findBookByIsbn(Query filter) throws ServiceException{
        Optional<Book> book = bookRepository.findBookByIsbn(filter.getIsbn());
        if(book.isEmpty()){
            throw new ServiceException("Book not found: Check the ISBN, if correct the book doesn't exist in our DB");
        }
        return book;
    }

    // TODO: refactor max 7 lijnen -> submethodes indien nodig (ctrl alt m) - DONE
    private Set<Author> getAuthor(List<String> authors){
        Set<Author> authorSetPerBook = new HashSet<>();

        if(authors != null){
            for (String authorName : authors) {
                Optional<Author> possibleAuthor = getOptionalAuthor(authorName);
                Author author = possibleAuthor.orElseGet(() -> createNewAuthor(authorName));

                authorSetPerBook.add(author);
            }
        }

        return authorSetPerBook;
    }

    private Author createNewAuthor( String authorName) {
        Author newAuthor = null;
        if (StringUtils.isNotBlank(authorName)){
            newAuthor = new Author(authorName);
            authorSet.add(newAuthor);
        }
        return newAuthor;
    }

    private Optional<Author> getOptionalAuthor(String author) {
        return authorSet.stream().filter(author1 -> author1.getFullName().equalsIgnoreCase(author)).findFirst();
    }

    // TODO: refactor max 7 lijnen -> submethodes indien nodig (ctrl alt m) - DONE
    private Set<Category> getCategory(List<String> categories){
        Set<Category> categorySetPerBook = new HashSet<>();

        if(categories != null){
            for (String categoryName : categories) {
                Optional<Category> possibleCategory = getOptionalCategory(categoryName);
                Category category = possibleCategory.orElseGet(() -> createNewCategory(categoryName));

                categorySetPerBook.add(category);
            }
        }

        return categorySetPerBook;
    }

    private Category createNewCategory(String categoryName) {
        Category newCategory = new Category(StringUtils.capitalize(categoryName));
        categorySet.add(newCategory);

        return newCategory;
    }

    private Optional<Category> getOptionalCategory(String category) {
        return categorySet.stream().filter(c -> c.getCategoryName().equalsIgnoreCase(category)).findFirst();
    }

    //TODO: validate function
}
