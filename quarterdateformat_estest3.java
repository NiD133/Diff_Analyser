package org.jfree.chart.axis;

import org.junit.Test;

import java.text.FieldPosition;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link QuarterDateFormat} class.
 */
public class QuarterDateFormatTest {

    /**
     * Verifies that the formatter correctly formats a date using Roman numerals
     * with the quarter displayed before the year.
     */
    @Test
    public void format_withRomanNumeralsAndQuarterFirst_returnsCorrectString() {
        // Arrange
        // The test requires a date that falls in the fourth quarter of the year 451.
        // We use a Calendar instance for a clear and reliable way to create this date.
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        calendar.set(Calendar.YEAR, 451);
        calendar.set(Calendar.MONTH, Calendar.OCTOBER); // October is in the 4th quarter.
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date dateInFourthQuarter = calendar.getTime();

        boolean quarterFirst = true;
        QuarterDateFormat formatter = new QuarterDateFormat(
                TimeZone.getDefault(),
                QuarterDateFormat.ROMAN_QUARTERS,
                quarterFirst
        );

        String expectedFormattedDate = "IV 451";
        StringBuffer resultBuffer = new StringBuffer();

        // Act
        formatter.format(dateInFourthQuarter, resultBuffer, new FieldPosition(0));

        // Assert
        assertEquals(expectedFormattedDate, resultBuffer.toString());
    }
}