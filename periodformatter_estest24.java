package org.joda.time.format;

import org.junit.Test;

/**
 * Unit tests for the PeriodFormatter class, focusing on parsing behavior.
 */
public class PeriodFormatterTest {

    /**
     * Verifies that calling parsePeriod() with a null input string
     * results in a NullPointerException.
     *
     * This is the expected behavior because the underlying parser, when invoked,
     * will fail to process a null reference.
     */
    @Test(expected = NullPointerException.class)
    public void parsePeriod_withNullInput_throwsNullPointerException() {
        // Arrange: Create a formatter with a valid (but simple) parser.
        // PeriodFormatterBuilder.Literal.EMPTY provides a non-null parser instance.
        PeriodFormatterBuilder.Literal emptyParser = PeriodFormatterBuilder.Literal.EMPTY;
        PeriodFormatter formatter = new PeriodFormatter(emptyParser, emptyParser);

        // Act: Attempt to parse a null string.
        // This action is expected to throw the exception.
        formatter.parsePeriod(null);

        // Assert: The test passes if a NullPointerException is thrown,
        // which is handled by the 'expected' parameter of the @Test annotation.
    }
}