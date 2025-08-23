package org.joda.time.base;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeParser;
import org.joda.time.format.DateTimePrinter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link AbstractPartial} class.
 */
public class AbstractPartial_ESTestTest14 {

    /**
     * Verifies that calling toString() with a formatter that lacks a printer
     * throws an UnsupportedOperationException.
     */
    @Test
    public void toString_withPrinterlessFormatter_shouldThrowUnsupportedOperationException() {
        // Arrange: Create a formatter that does not support printing (it has no printer).
        DateTimeFormatter formatterWithoutPrinter = new DateTimeFormatter((DateTimePrinter) null, (DateTimeParser) null);
        LocalDate partialDate = new LocalDate(2023, 10, 27);

        // Act & Assert
        try {
            partialDate.toString(formatterWithoutPrinter);
            fail("Expected UnsupportedOperationException was not thrown.");
        } catch (UnsupportedOperationException e) {
            // Verify that the exception is thrown for the correct reason.
            assertEquals("Printing not supported", e.getMessage());
        }
    }
}