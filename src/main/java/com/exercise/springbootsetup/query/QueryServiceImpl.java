package com.exercise.springbootsetup.query;

import com.exercise.springbootsetup.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service("queryService")
public class QueryServiceImpl implements QueryService{
    final static String GENERAL_EXCEPTION_MESSAGE = "Exception while getting books: ";

    @Override
    public String createSort(String sortDir) throws ServiceException {
        try{
            if(StringUtils.isNotBlank(sortDir) && !StringUtils.equalsAnyIgnoreCase(sortDir, "asc", "desc")){
                throw new IllegalArgumentException("Sort parameter can only be asc or desc");
            }

            String direction = null;
            if (StringUtils.isNotBlank(sortDir)){
                direction = StringUtils.upperCase(sortDir);
            }
            return direction;
        }catch (Exception e){
            throw new ServiceException(GENERAL_EXCEPTION_MESSAGE + e.getMessage(), e);
        }
    }

    @Override
    public ZonedDateTime createZonedDateTime(final String date) throws ServiceException {
        try {
            if (StringUtils.isNotBlank(date) && !GenericValidator.isDate(date,"yyyy-MM-dd", true)){
                throw new IllegalArgumentException("Something is wrong with the date format");
            }
            ZonedDateTime zonedDate = null;
            if (StringUtils.isNotBlank(date)){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(date, formatter);

                zonedDate = localDate.atStartOfDay(ZoneId.systemDefault());
            }
            return zonedDate;
        }catch(Exception e){
            throw new ServiceException(GENERAL_EXCEPTION_MESSAGE + e.getMessage(), e);
        }
    }
}
