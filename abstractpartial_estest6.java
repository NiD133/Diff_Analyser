package org.joda.time.base;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Instant;
import org.joda.time.YearMonth;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the AbstractPartial class, focusing on the toDateTime(ReadableInstant) method.
 */
public class AbstractPartialTest {

    /**
     * Tests that toDateTime() correctly combines the fields from a partial (YearMonth)
     * with the fields from a base instant to create a complete DateTime.
     * The fields from the partial should override the corresponding fields from the instant.
     */
    @Test
    public void toDateTime_shouldCombinePartialWithInstantFields() {
        // Arrange
        // A partial date representing October 2023.
        YearMonth yearMonthPartial = new YearMonth(2023, 10);

        // A base instant providing the remaining date-time fields (day, time, time zone).
        // This instant corresponds to 2005-06-09T10:20:30.456Z.
        Instant baseInstant = new DateTime(2005, 6, 9, 10, 20, 30, 456, DateTimeZone.UTC).toInstant();

        // Act
        // Combine the YearMonth with the base instant. The year and month from the partial
        // should override those from the base instant.
        DateTime result = yearMonthPartial.toDateTime(baseInstant);

        // Assert
        // The expected DateTime should have the year/month from the partial and all other
        // fields (day, time) from the base instant.
        DateTime expectedDateTime = new DateTime(2023, 10, 9, 10, 20, 30, 456, DateTimeZone.UTC);
        assertEquals(expectedDateTime, result);

        // We can also assert individual fields for maximum clarity.
        assertEquals(2023, result.getYear());
        assertEquals(10, result.getMonthOfYear());
        assertEquals(9, result.getDayOfMonth());
        assertEquals(10, result.getHourOfDay());
        assertEquals(DateTimeZone.UTC, result.getZone());
    }
}