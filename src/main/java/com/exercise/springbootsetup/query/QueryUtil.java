package com.exercise.springbootsetup.query;

import com.exercise.springbootsetup.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class QueryUtil{
    final static String GENERAL_EXCEPTION_MESSAGE = "Exception while getting books: ";

    // TODO: naar QueryUtil - DONE
    public static void checkSortingDirection(String sortDir) throws ServiceException {
        try{
            if(!StringUtils.equalsAnyIgnoreCase(sortDir, "asc", "desc")){
                throw new IllegalArgumentException("Sort parameter can only be asc or desc");
            }
        }catch (Exception e){
            throw new ServiceException(GENERAL_EXCEPTION_MESSAGE + e.getMessage(), e);
        }
    }

    public static void checkZonedDateTime(final String date) throws ServiceException {
        try {
            if (!GenericValidator.isDate(date,"yyyy-MM-dd", true)){
                throw new IllegalArgumentException("Something is wrong with the date format");
            }
        }catch(Exception e){
            throw new ServiceException(GENERAL_EXCEPTION_MESSAGE + e.getMessage(), e);
        }
    }

    public static ZonedDateTime createZonedDateTime(final String date) throws ServiceException {
        checkZonedDateTime(date);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);

        return localDate.atStartOfDay(ZoneId.systemDefault());
    }
}
