package org.jfree.chart.axis;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.text.FieldPosition;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * A test suite for the QuarterDateFormat class.
 * This specific test focuses on the format() method.
 */
public class QuarterDateFormat_ESTestTest13 extends QuarterDateFormat_ESTest_scaffolding {

    /**
     * Verifies that the format() method correctly formats a date in the fourth
     * quarter into the "YYYY Q" string format.
     */
    @Test
    public void format_shouldReturnYearAndQuarter_forDateInFourthQuarter() {
        // Arrange
        // Use a fixed timezone like "GMT" to ensure the test is deterministic
        // and not affected by the system's default timezone.
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        QuarterDateFormat formatter = new QuarterDateFormat(timeZone);

        // Create a specific date in the fourth quarter (October) of the year 451.
        // This replaces the confusing `new MockDate(-2355, 0, 2123, ...)` from
        // the original test, which resolved to the same time period.
        Calendar calendar = new GregorianCalendar(timeZone);
        calendar.set(451, Calendar.OCTOBER, 23);
        Date dateInQ4 = calendar.getTime();

        StringBuffer resultBuffer = new StringBuffer();
        // The FieldPosition parameter is not used by this formatter, but we
        // provide a non-null value for completeness.
        FieldPosition fieldPosition = new FieldPosition(0);

        // Act
        formatter.format(dateInQ4, resultBuffer, fieldPosition);

        // Assert
        // The default format is "YYYY Q". For a date in October 451, the
        // expected output is "451 4".
        assertEquals("451 4", resultBuffer.toString());
    }
}