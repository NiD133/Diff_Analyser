package org.joda.time.format;

import static org.junit.Assert.assertEquals;

import org.joda.time.Period;
import org.junit.Test;

/**
 * Unit tests for {@link PeriodFormatter}.
 * This test focuses on the print functionality with the standard ISO-8601 format.
 */
public class PeriodFormatterTest {

    /**
     * Tests that the standard ISO period formatter correctly prints a period
     * containing all possible field types (years, months, weeks, days, hours,
     * minutes, seconds, and milliseconds).
     */
    @Test
    public void print_withAllFields_shouldRenderStandardISO8601Format() {
        // Arrange: Create a period with a value for each field.
        Period period = new Period(1, 2, 3, 4, 5, 6, 7, 8); // 1Y 2M 3W 4D 5H 6M 7S 8ms
        PeriodFormatter formatter = ISOPeriodFormat.standard();
        String expectedOutput = "P1Y2M3W4DT5H6M7.008S";

        // Act: Format the period into a string.
        String actualOutput = formatter.print(period);

        // Assert: Verify the formatted string matches the expected ISO-8601 standard format.
        assertEquals(expectedOutput, actualOutput);
    }
}