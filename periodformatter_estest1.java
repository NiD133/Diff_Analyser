package org.joda.time.format;

import org.joda.time.Period;
import org.joda.time.ReadablePeriod;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link PeriodFormatter}.
 */
public class PeriodFormatterTest {

    /**
     * Tests that the printTo(Writer, ReadablePeriod) method correctly writes a
     * formatted period string to the provided writer.
     */
    @Test
    public void printTo_writesFormattedPeriodToWriter() throws IOException {
        // Arrange: Create a formatter for "X years and Y months" and a period to format.
        // Using PeriodFormatterBuilder makes the formatting logic explicit and easy to understand.
        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendYears()
                .appendSuffix(" year", " years")
                .appendSeparator(" and ")
                .appendMonths()
                .appendSuffix(" month", " months")
                .toFormatter()
                .withLocale(Locale.ENGLISH); // Set locale for consistent test results.

        // Use a simple, clear Period object for the test.
        ReadablePeriod period = new Period(2, 5, 0, 0, 0, 0, 0, 0); // 2 years and 5 months.
        StringWriter writer = new StringWriter();
        String expectedOutput = "2 years and 5 months";

        // Act: Print the period to the writer.
        formatter.printTo(writer, period);

        // Assert: The writer should contain the correctly formatted string.
        // This assertion directly verifies the behavior of the printTo method.
        assertEquals(expectedOutput, writer.toString());
    }
}