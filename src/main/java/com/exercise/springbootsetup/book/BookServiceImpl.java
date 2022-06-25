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
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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

    @Override
    public List<Book> externalToInternalBooks(List<com.exercise.springbootsetup.models.external.Book> source) {
        return source.stream().map(this::externalToInternalBook).collect(Collectors.toList());
    }

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
    public List<Book> saveAll( String filePath) throws ServiceException {
        return bookRepository.saveAll(this.getBooksFromFile(filePath));
    }

    @Override
    public Optional<Book> findBookByIsbn(String isbn) throws ServiceException{
        Optional<Book> book = bookRepository.findBookByIsbn(isbn);
        if(book.isEmpty()){
            throw new ServiceException("Book not found: Check the ISBN, if correct the book doesn't exist in our DB");
        }
        return book;
    }

    private Set<Author> getAuthor(List<String> authors){
        Set<Author> bookAuthorSet = new HashSet<>();

        if(authors != null){
            for (String author : authors) {
                Optional<Author> possibleAuthor = authorSet.stream().filter(author1 -> author1.getFullName().equalsIgnoreCase(author)).findFirst();
                if (possibleAuthor.isPresent()){
                    bookAuthorSet.add(possibleAuthor.get());
                }else {
                    if (StringUtils.isNotBlank(author)){
                        Author newAuthor = new Author(author);
                        authorSet.add(newAuthor);

                        bookAuthorSet.add(newAuthor);
                    }
                }
            }
        }

        return bookAuthorSet;
    }

    private Set<Category> getCategory(List<String> categories){
        Set<Category> bookCategorySet = new HashSet<>();

        if(categories != null){
            for (String category : categories) {
                Optional<Category> possibleCategory = categorySet.stream().filter(c -> c.getCategoryName().equalsIgnoreCase(category)).findFirst();
                if (possibleCategory.isPresent()){
                    bookCategorySet.add(possibleCategory.get());
                }else {
                    if (StringUtils.isNotBlank(category)){
                        Category newCategory = new Category(StringUtils.capitalize(category));
                        categorySet.add(newCategory);

                        bookCategorySet.add(newCategory);
                    }
                }
            }
        }

        return bookCategorySet;
    }
}
