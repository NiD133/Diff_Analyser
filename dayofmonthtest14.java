package org.threeten.extra;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import org.junit.jupiter.api.Test;

/**
 * Tests the functionality of {@link DayOfMonth#from(TemporalAccessor)} when used
 * as a query for parsing with {@link DateTimeFormatter}.
 */
public class DayOfMonthTestTest14 {

    @Test
    void from_canBeUsedAsTemporalQueryToParseString() {
        // Arrange
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d");
        String textToParse = "3";
        DayOfMonth expectedDayOfMonth = DayOfMonth.of(3);

        // Act: The DayOfMonth::from method reference is used as a TemporalQuery to parse the string.
        DayOfMonth actualDayOfMonth = formatter.parse(textToParse, DayOfMonth::from);

        // Assert
        assertEquals(expectedDayOfMonth, actualDayOfMonth);
    }
}