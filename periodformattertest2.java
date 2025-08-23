package org.joda.time.format;

import static org.junit.Assert.assertEquals;

import org.joda.time.Period;
import org.junit.Test;

/**
 * Tests for the printing methods of {@link PeriodFormatter}.
 * This focuses on the printTo(StringBuffer, ReadablePeriod) method.
 */
public class PeriodFormatterTest {

    /**
     * The standard ISO period formatter is used for these tests.
     * It is thread-safe and immutable.
     */
    private final PeriodFormatter formatter = ISOPeriodFormat.standard();

    @Test
    public void printTo_withValidPeriod_appendsFormattedStringToBuffer() {
        // Arrange
        Period period = new Period(1, 2, 3, 4, 5, 6, 7, 8); // 1Y 2M 3W 4D 5h 6m 7s 8ms
        StringBuffer buffer = new StringBuffer();
        String expectedOutput = "P1Y2M3W4DT5H6M7.008S";

        // Act
        formatter.printTo(buffer, period);

        // Assert
        assertEquals(expectedOutput, buffer.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void printTo_withNullPeriod_throwsIllegalArgumentException() {
        // Arrange
        StringBuffer buffer = new StringBuffer();

        // Act: This call is expected to throw an exception.
        formatter.printTo(buffer, null);
    }
}