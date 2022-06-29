package com.exercise.springbootsetup.importer;

import com.exercise.springbootsetup.book.Book;
import com.exercise.springbootsetup.book.BookMapperImpl;
import com.exercise.springbootsetup.book.BookRepository;
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

@Service("importService")
public class ImportServiceImpl implements ImportService{
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
    public List<Book> saveAllBooks(Query filter) throws ServiceException {
        return bookRepository.saveAll(this.getBooksFromFile(filter.getFilePath()));
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
    public void validate(Query filter) throws ServiceException{
        if (StringUtils.isNotBlank(filter.getFilePath())){
            QueryUtil.checkFilePath(filter.getFilePath());
        }
    }
}
