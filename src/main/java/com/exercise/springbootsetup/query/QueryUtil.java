package com.exercise.springbootsetup.query;

import com.exercise.springbootsetup.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class QueryUtil{
    final static String WHILE_GETTING_BOOKS_EXCEPTION = "Exception while getting books: ";

    // TODO: naar QueryUtil - DONE
    public static void checkSortingDirection(String sortDir) throws ServiceException {
        try{
            if(!StringUtils.equalsAnyIgnoreCase(sortDir, "asc", "desc")){
                throw new IllegalArgumentException("Sort parameter can only be asc or desc");
            }
        }catch (Exception e){
            throw new ServiceException(WHILE_GETTING_BOOKS_EXCEPTION + e.getMessage(), e);
        }
    }

    public static void checkZonedDateTime(final String date) throws ServiceException {
        try {
            if (!GenericValidator.isDate(date,"yyyy-MM-dd", true)){
                throw new IllegalArgumentException("Something is wrong with the date format");
            }
        }catch(Exception e){
            throw new ServiceException(WHILE_GETTING_BOOKS_EXCEPTION + e.getMessage(), e);
        }
    }

    public static ZonedDateTime createZonedDateTime(final String date) throws ServiceException {
        checkZonedDateTime(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);

        return localDate.atStartOfDay(ZoneId.systemDefault());
    }

    public static void checkFilePath(final String filePath) throws ServiceException {
        try{
            if(!StringUtils.containsAny(filePath, "/")){
                throw new IllegalArgumentException("Invalid path");
            }
            if (!StringUtils.endsWith(filePath, ".json")){
                throw new IllegalArgumentException("Given file isn't a json");
            }
        }catch (Exception e){
            throw new ServiceException("Something is wrong with the file path: " + e.getMessage(), e);
        }

    }
}
