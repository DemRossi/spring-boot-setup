package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.author.Author;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BookMapper {
    public List<Book> externalToInternalBooks(final List<com.exercise.springbootsetup.models.external.Book> source) {
        return source.stream().map(this::externalToInternalBook).collect(Collectors.toList());
    }

        public Book externalToInternalBook(final com.exercise.springbootsetup.models.external.Book source) {
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
            internalBook.setAuthors(source.getAuthors().stream()
                    .map(Author::new)
                    .filter(author -> StringUtils.isNotBlank(author.getFullName()))
                    .collect(Collectors.toList()));
//            internalBook.setCategories(makeCategories(source));
        }

        return internalBook;
    }

//    private List<Category> makeCategories(com.exercise.springbootsetup.models.external.Book source){
//        List<String> strCategories = source.getCategories();
//        List<Category> categoryList = new ArrayList<>();
//
//        if (!strCategories.isEmpty()){
//            Category category = new Category();
//
//            for (String c : strCategories) {
//                category.setCategoryName(c);
//                categoryList.add(category);
//            }
//        }
//        return  categoryList;
//    }
}
