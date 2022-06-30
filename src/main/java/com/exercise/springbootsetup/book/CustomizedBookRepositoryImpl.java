package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.exception.ServiceException;
import com.exercise.springbootsetup.query.Query;
import com.exercise.springbootsetup.query.QueryUtil;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

public class CustomizedBookRepositoryImpl implements CustomizedBookRepository{

    @PersistenceContext
    EntityManager entityManager;

    // TODO: write test - DONE
    @Override
    public Optional<List<Book>> getBooks(Query filter) throws ServiceException {
        StringBuilder queryString = new StringBuilder("select b from Book b");
        Map<String, Object> params = new HashMap<>();

        if (filter.getPublishedAfter() != null){
            queryString.append(" where b.publishedDate between :date and DATETIME('now','localtime')");
            params.put("date", QueryUtil.createZonedDateTime(filter.getPublishedAfter()));
        }
        if (filter.getSortDir() != null){
            queryString.append(" order by b.title");
            if (Objects.equals(filter.getSortDir(), "desc")){
                queryString.append(" desc");
            }
        }

        return Optional.of(createAndExecuteSqlQuery(queryString.toString(), params));
    }

    private List<Book> createAndExecuteSqlQuery(String sqlQuery ,final Map<String, Object> params){
        javax.persistence.TypedQuery<Book> query = entityManager.createQuery(sqlQuery, Book.class);

        for (Map.Entry<String, Object> param : params.entrySet() ) {
            query.setParameter(param.getKey(), param.getValue());
        }
        return query.getResultList();
    }
}
