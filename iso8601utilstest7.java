package com.google.gson.internal.bind.util;

import static com.google.common.truth.Truth.assertThat;

import java.text.ParseException;
import java.text.ParsePosition;
import java.time.Instant;
import java.util.Date;
import org.junit.Test;

/**
 * Tests for {@link ISO8601Utils#parse(String, ParsePosition)}.
 */
public class ISO8601UtilsParseTest {

    @Test
    public void parse_stringWithNegativeOffset_returnsCorrectUtcDate() throws ParseException {
        // Arrange
        String dateStringWithOffset = "2018-06-25T00:00:00-03:00";
        // The input string is equivalent to 3:00 AM in the UTC time zone.
        Instant expectedInstant = Instant.parse("2018-06-25T03:00:00Z");
        Date expectedDate = Date.from(expectedInstant);

        // Act
        Date actualDate = ISO8601Utils.parse(dateStringWithOffset, new ParsePosition(0));

        // Assert
        assertThat(actualDate).isEqualTo(expectedDate);
    }
}