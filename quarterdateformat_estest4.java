package org.jfree.chart.axis;

import org.junit.Test;
import java.text.FieldPosition;
import java.util.Date;
import java.util.TimeZone;

/**
 * Tests for the {@link QuarterDateFormat} class, focusing on exception handling.
 */
public class QuarterDateFormatTest {

    /**
     * Verifies that the format() method throws a NullPointerException
     * when the 'toAppendTo' StringBuffer is null.
     */
    @Test(expected = NullPointerException.class)
    public void format_withNullStringBuffer_shouldThrowNullPointerException() {
        // Arrange
        // Use a standard timezone and default quarter symbols for simplicity.
        QuarterDateFormat formatter = new QuarterDateFormat(TimeZone.getDefault(), QuarterDateFormat.REGULAR_QUARTERS);
        Date aDate = new Date();
        FieldPosition fieldPosition = new FieldPosition(0);

        // Act & Assert
        // This call is expected to throw a NullPointerException.
        // The @Test(expected=...) annotation handles the assertion.
        formatter.format(aDate, null, fieldPosition);
    }
}