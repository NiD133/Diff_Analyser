package org.threeten.extra;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.time.DateTimeException;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAccessor;

/**
 * Test class for {@link DayOfMonth#from(TemporalAccessor)}.
 */
public class DayOfMonthFromTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void from_givenTemporalAccessorWithoutDayOfMonthField_throwsDateTimeException() {
        // Arrange: A TemporalAccessor that does not contain a day-of-month field.
        // ZoneOffset is used here as an example of such a type.
        TemporalAccessor temporalWithoutDayOfMonth = ZoneOffset.MAX;
        String expectedMessage = "Unable to obtain DayOfMonth from TemporalAccessor: " +
                                 temporalWithoutDayOfMonth + " of type " +
                                 temporalWithoutDayOfMonth.getClass().getName();

        // Assert: Configure the expected exception type and message.
        thrown.expect(DateTimeException.class);
        thrown.expectMessage(expectedMessage);

        // Act: Attempt to create a DayOfMonth from the unsupported temporal.
        DayOfMonth.from(temporalWithoutDayOfMonth);
    }
}