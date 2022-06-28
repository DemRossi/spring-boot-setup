package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.exception.ServiceException;
import com.exercise.springbootsetup.query.Query;
import com.exercise.springbootsetup.query.QueryUtil;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service(" bookService")
public class BookServiceImpl implements BookService{
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookMapperImpl bookMapper;

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
            return bookMapper.externalToInternalBooks(Arrays.asList(mapper.readValue(new FileReader(filePath), com.exercise.springbootsetup.book.external.Book[].class)));
        }catch (IOException e){
            throw new ServiceException("Exception while retrieving books from file: " + e.getMessage(), e);
        }
    }

    @Override
    public Book save(Book book){
        return bookRepository.save(book);
    }

    @Override
    public void deleteById(Long id){
        bookRepository.deleteById(id);
    }

    @Override
    public Optional<List<Book>> getBooks(Query filter) throws ServiceException {
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

    //TODO: validate function - DONE
    @Override
    public void validate(Query filter) throws ServiceException {
        if (StringUtils.isNotBlank(filter.getSortDir())){
            QueryUtil.checkSortingDirection(filter.getSortDir());
        }
        if(StringUtils.isNotBlank(filter.getPublishedAfter())){
            QueryUtil.checkZonedDateTime(filter.getPublishedAfter());
        }
    }
}
