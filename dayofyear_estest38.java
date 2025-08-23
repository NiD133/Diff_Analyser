package org.threeten.extra;

import org.junit.Test;
import java.time.DateTimeException;
import java.time.Month;
import java.time.temporal.TemporalAccessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link DayOfYear#from(TemporalAccessor)}.
 */
public class DayOfYearFromTest {

    /**
     * Tests that DayOfYear.from() throws a DateTimeException when the provided
     * temporal object does not contain the required DAY_OF_YEAR field.
     */
    @Test
    public void from_temporalWithoutDayOfYearField_throwsException() {
        // Arrange: A TemporalAccessor (Month) that cannot be resolved to a day-of-year.
        TemporalAccessor temporalWithoutDayOfYear = Month.NOVEMBER;
        String expectedMessage = "Unable to obtain DayOfYear from TemporalAccessor: NOVEMBER of type java.time.Month";

        // Act & Assert
        try {
            DayOfYear.from(temporalWithoutDayOfYear);
            fail("Expected DateTimeException was not thrown for a temporal lacking DAY_OF_YEAR.");
        } catch (DateTimeException e) {
            // Verify that the exception has the expected informative message.
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}