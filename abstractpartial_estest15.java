package org.joda.time.base;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.DateTimePrinter;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This test class focuses on the behavior of the AbstractPartial class.
 */
public class AbstractPartialTest {

    /**
     * Tests that calling toString() with a formatter whose printer estimates
     * a negative printed length throws a NegativeArraySizeException.
     *
     * This scenario can occur with a faulty DateTimePrinter implementation. The
     * formatter uses this estimated length to pre-allocate a buffer, and a
     * negative value is invalid for array allocation.
     */
    @Test(expected = NegativeArraySizeException.class)
    public void toStringWithFormatterEstimatingNegativeLengthShouldThrowException() {
        // Arrange
        // 1. Create a mock DateTimePrinter that simulates a faulty implementation
        //    by returning a negative estimated length.
        DateTimePrinter mockPrinter = mock(DateTimePrinter.class);
        when(mockPrinter.estimatePrintedLength()).thenReturn(-1);

        // 2. A DateTimeParser is required to construct the formatter, but its
        //    behavior is not relevant for this printing test.
        DateTimeParser mockParser = mock(DateTimeParser.class);
        DateTimeFormatter formatter = new DateTimeFormatter(mockPrinter, mockParser);

        // 3. Create an instance of a class that extends AbstractPartial, like LocalDate.
        //    The specific date value does not affect this test's outcome.
        LocalDate partialDate = new LocalDate();

        // Act & Assert
        // The toString method will use the formatter, which attempts to create a
        // buffer with the negative estimated length, triggering the expected exception.
        partialDate.toString(formatter);
    }
}