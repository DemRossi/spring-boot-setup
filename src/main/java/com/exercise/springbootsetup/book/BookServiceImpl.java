package com.exercise.springbootsetup.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service(" bookService")
public class BookServiceImpl implements BookService{
    private static final String JSON_PATH = "src/main/resources/files/books.json";

    @Autowired
    private BookRepository bookRepository;

    ObjectMapper mapper = JsonMapper.builder()
            .addModule(new ParameterNamesModule())
            .addModule(new Jdk8Module())
            .addModule(new JavaTimeModule())
            .build();

    @Override
    public List<Book> getBooksFromFile() throws IOException {
        return this.externalToInternalBooks(Arrays.asList(mapper.readValue(new FileReader(JSON_PATH), com.exercise.springbootsetup.models.external.Book[].class)));
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
//            internalBook.setAuthors(source.getAuthors().stream()
//                    .map(Author::new)
//                    .filter(author -> StringUtils.isNotBlank(author.getFullName()))
//                    .collect(Collectors.toList()));


//            internalBook.setAuthors(source.getAuthors().stream()
//                    .filter(author -> StringUtils.isNotBlank(author.getFullName()))
//                    .sorted(Comparator.comparing(Author::getFullName))
//                    .collect(Collectors.toCollection(LinkedList::new)));

//            internalBook.setAuthors(findOrCreateAuthor(source.getAuthors().stream()
//                    .map(AuthorDTO::new)// --> proper usage of a DTO??
//                    .filter(author -> StringUtils.isNotBlank(author.getFullName()))
//                    .collect(Collectors.toList())));

//            internalBook.setCategories(makeCategories(source));
        }

        return internalBook;
    }

    @Override
    public Optional<List<Book>> getBooks(String sortDir, String publishedAfter) {
        Optional<List<Book>> requestResult;

        if (StringUtils.isNotBlank(sortDir) && StringUtils.isNotBlank(publishedAfter)){
            // only published after date, sorted by title ASC|DESC
            requestResult = bookRepository.findAllByPublishedDateAfter(createZonedDateTime(publishedAfter), Sort.by(Sort.Direction.fromString(sortDir), "title"));;
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

    private ZonedDateTime createZonedDateTime(final String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);

        return localDate.atStartOfDay(ZoneId.systemDefault());
    }
}
