package org.threeten.extra;

import org.junit.Test;
import java.time.DateTimeException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link DayOfYear}.
 */
public class DayOfYearTest {

    /**
     * Tests that creating a DayOfYear with a value of 0 is not allowed.
     * The valid range for a day of the year is 1 to 366.
     */
    @Test
    public void of_whenDayIsZero_throwsDateTimeException() {
        // Act & Assert
        DateTimeException exception = assertThrows(
            "DayOfYear.of(0) should throw an exception.",
            DateTimeException.class,
            () -> DayOfYear.of(0)
        );

        // Assert
        assertEquals(
            "The exception message should indicate the invalid value.",
            "Invalid value for DayOfYear: 0",
            exception.getMessage()
        );
    }
}