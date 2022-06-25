package com.exercise.springbootsetup.query;

import com.exercise.springbootsetup.exception.ServiceException;

import java.time.ZonedDateTime;

public interface QueryService {
    String createSort(final String sortDir) throws ServiceException;

    ZonedDateTime createZonedDateTime(final String date) throws ServiceException;
}
