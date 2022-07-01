package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.exception.ServiceException;
import com.exercise.springbootsetup.query.Query;
import com.exercise.springbootsetup.query.QueryUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service(" bookService")
public class BookServiceImpl implements BookService{
    @Autowired
    private BookRepository bookRepository;

//    @Override
//    public Book save(Book book){
//        return bookRepository.save(book);
//    }
//

    //TODO: ResponseEntity in controller ipv service laag - DONE
    @Override
    public Optional<Book> deleteById(Query filter){
        Optional<Book> bookToDelete = bookRepository.findById(filter.getId());
        if (bookToDelete.isPresent()){
            bookRepository.deleteById(filter.getId());
        }
        return bookToDelete;
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
