package com.exercise.springbootsetup.query;

import com.exercise.springbootsetup.exception.ServiceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class QueryUtilTest {
    final String DATE = "2011-04-01";
    final String WRONG_FORMAT_DATE = "2011-30-01";
    final String NOT_DATE = "qwerty";
    final String ZONED_DATE = "2011-04-01T00:00+02:00[Europe/Paris]";
    final String JSON_PATH = "src/test/resources/file/test_json.json";
    final String HTML_PATH = "src/test/resources/file/test_json.html";
    final String NO_SEPARATOR_PATH = "srctestresourcesfiletest_json.html";

    @Test
    void checkSortingDirection_asc() {
        assertDoesNotThrow(() -> QueryUtil.checkSortingDirection("asc"));
    }

    @Test
    void checkSortingDirection_desc() {
        assertDoesNotThrow(() -> QueryUtil.checkSortingDirection("desc"));
    }

    @Test
    void checkSortingDirection_wrong_input() {
        Exception exception = assertThrows(ServiceException.class, () ->
            QueryUtil.checkSortingDirection("jef")
        );

        String expectedMessage = "Sort parameter can only be asc or desc";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createZonedDateTime() throws ServiceException {
        assertThat(QueryUtil.createZonedDateTime(DATE)).isEqualTo(ZonedDateTime.parse(ZONED_DATE));
    }

    @Test
    void createZonedDateTime_wrong_format() {
        Exception exception = assertThrows(ServiceException.class, () -> {
            ZonedDateTime date = QueryUtil.createZonedDateTime(WRONG_FORMAT_DATE);
        });

        String expectedMessage = "Something is wrong with the date format";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void createZonedDateTime_not_a_date() {
        Exception exception = assertThrows(ServiceException.class, () -> {
            ZonedDateTime date = QueryUtil.createZonedDateTime(NOT_DATE);
        });

        String expectedMessage = "Something is wrong with the date format";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void checkFilePath_expect_correct(){
        assertDoesNotThrow(() -> QueryUtil.checkFilePath(JSON_PATH));
    }

    @Test
    void checkFilePath_not_a_json_expect_error() {
        Exception exception = assertThrows(ServiceException.class, () ->
                QueryUtil.checkFilePath(HTML_PATH)
        );

        String expectedMessage = "Given file isn't a json";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void checkFilePath_no_separators_expect_error() {
        Exception exception = assertThrows(ServiceException.class, () ->
                QueryUtil.checkFilePath(NO_SEPARATOR_PATH)
        );

        String expectedMessage = "Invalid path";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }
}