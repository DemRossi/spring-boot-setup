package com.exercise.springbootsetup.query;

import com.exercise.springbootsetup.exception.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class QueryServiceImplTest {
    final String DATE = "2011-04-01";
    final String WRONG_FORMAT_DATE = "2011-30-01";
    final String NOT_DATE = "qwerty";
    final String ZONED_DATE = "2011-04-01T00:00+02:00[Europe/Paris]";

    @InjectMocks
    private QueryServiceImpl queryService;

    @Test
    void createSort_asc() throws ServiceException {
        assertThat(queryService.createSort("asc")).isEqualTo("ASC");
    }

    @Test
    void createSort_desc() throws ServiceException {
        assertThat(queryService.createSort("desc")).isEqualTo("DESC");
    }

    @Test
    void createSort_wrong_sort() {
        Exception exception = assertThrows(ServiceException.class, () -> {
            String sort = queryService.createSort("qwerty");
        });

        String expectedMessage = "Sort parameter can only be asc or desc";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createZonedDateTime() throws ServiceException {
        assertThat(queryService.createZonedDateTime(DATE)).isEqualTo(ZonedDateTime.parse(ZONED_DATE));
    }

    @Test
    void createZonedDateTime_wrong_format() {
        Exception exception = assertThrows(ServiceException.class, () -> {
            ZonedDateTime date = queryService.createZonedDateTime(WRONG_FORMAT_DATE);
        });

        String expectedMessage = "Something is wrong with the date format";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createZonedDateTime_not_a_date() {
        Exception exception = assertThrows(ServiceException.class, () -> {
            ZonedDateTime date = queryService.createZonedDateTime(NOT_DATE);
        });

        String expectedMessage = "Something is wrong with the date format";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}