package com.exercise.springbootsetup.book;

import com.exercise.springbootsetup.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CustomizedBookRepositoryImpl implements CustomizedBookRepository{

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<List<Book>> getBooks(Query filter) {
        StringBuilder queryString = new StringBuilder("select * from book");
        Map<String, Object> params = new HashMap<>();

        if (filter.getPublishedAfter() != null){
            queryString.setLength(0);
            queryString.append("select * from book where published_date between :date and DATETIME('now','localtime')");
            params.put("date", filter.getPublishedAfter());
        }
        if (filter.getSortDir() != null){
            queryString.append(" order by case when :direction = 'ASC' then title end ASC, " +
                    "case when :direction = 'DESC' then title end DESC");
            params.put("direction", filter.getSortDir());
        }

        return Optional.of(createAndExecuteSqlQuery(queryString.toString(), params));
    }

    @SuppressWarnings("unchecked")
    private List<Book> createAndExecuteSqlQuery(String sqlQuery ,final Map<String, Object> params){
        javax.persistence.Query query = entityManager.createNativeQuery(sqlQuery, Book.class);

        if (!params.isEmpty()){
            for (Map.Entry<String, Object> param : params.entrySet() ) {
                query.setParameter(param.getKey(), param.getValue());
            }
        }
        return query.getResultList();
    }
}
