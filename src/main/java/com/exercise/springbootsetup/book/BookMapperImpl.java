package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.author.Author;
import com.exercise.springbootsetup.category.Category;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BookMapperImpl implements BookMapper{
    private final Set<Author> authorSet = new HashSet<>();
    private final Set<Category> categorySet = new HashSet<>();

    // TODO: Make mapper -> out of service - DONE
    @Override
    public List<Book> externalToInternalBooks(List<com.exercise.springbootsetup.book.external.Book> source) {
        return source.stream().map(this::externalToInternalBook).collect(Collectors.toList());
    }

    // TODO: Make mapper -> out of service - DONE
    @Override
    public Book externalToInternalBook(com.exercise.springbootsetup.book.external.Book source) {
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
        Category newCategory = null;
        if (StringUtils.isNotBlank(categoryName)) {
            newCategory = new Category(StringUtils.capitalize(categoryName));
            categorySet.add(newCategory);
        }

        return newCategory;
    }

    private Optional<Category> getOptionalCategory(String category) {
        return categorySet.stream().filter(c -> c.getCategoryName().equalsIgnoreCase(category)).findFirst();
    }
}
