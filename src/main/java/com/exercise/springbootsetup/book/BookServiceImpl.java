package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.author.Author;
import com.exercise.springbootsetup.category.Category;
import com.exercise.springbootsetup.exception.ServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
            internalBook.setAuthors(getAuthor(source.getAuthors()));
            internalBook.setCategories(getCategory(source.getCategories()));
        }

        return internalBook;
    }

    @Override
    public Optional<List<Book>> getBooks(String sortDir, String publishedAfter) {
        Optional<List<Book>> requestResult;

        if (StringUtils.isNotBlank(sortDir) && StringUtils.isNotBlank(publishedAfter)){
            // only published after date, sorted by title ASC|DESC
            requestResult = bookRepository.findAllByPublishedDateAfter(createZonedDateTime(publishedAfter), Sort.by(Sort.Direction.fromString(sortDir), "title"));
        }else if(StringUtils.isNotBlank(sortDir) && StringUtils.isBlank(publishedAfter)){
            // getAll sorted by title ASC|DESC
            requestResult =  Optional.of(bookRepository.findAll(Sort.by(Sort.Direction.fromString(sortDir), "title")));
        } else if (StringUtils.isBlank(sortDir) && StringUtils.isNotBlank(publishedAfter)) {
            // only published after date
            requestResult =  bookRepository.findAllByPublishedDateAfter(createZonedDateTime(publishedAfter));
        }else{
            // getAll
            requestResult = Optional.of(bookRepository.findAll());
        }

        return requestResult;
    }

    @Override
    public List<Book> saveAll( String filePath) throws ServiceException {
        return bookRepository.saveAll(this.getBooksFromFile(filePath));
    }

    private ZonedDateTime createZonedDateTime(final String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);

        return localDate.atStartOfDay(ZoneId.systemDefault());
    }

    @Override
    public Optional<Book> findBookByIsbn(String isbn) {
        return bookRepository.findBookByIsbn(isbn);
    }

    private Set<Author> getAuthor(List<String> authors){
        Set<Author> bookAuthorSet = new HashSet<>();

        if(authors != null){
            for (String author : authors) {
                if (authorSet.stream().anyMatch(author1 -> author1.getFullName().equals(author))){
                    bookAuthorSet.add(authorSet.stream().filter(author1 -> author1.getFullName().equals(author)).findFirst().get());
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
                if (categorySet.stream().anyMatch(c-> c.getCategoryName().equals(category))){
                    bookCategorySet.add(categorySet.stream().filter(c -> c.getCategoryName().equals(category)).findFirst().get());
                }else {
                    if (StringUtils.isNotBlank(category)){
                        Category newCategory = new Category(category);
                        categorySet.add(newCategory);

                        bookCategorySet.add(newCategory);
                    }
                }
            }
        }

        return bookCategorySet;
    }
}
