package org.joda.time.format;

import org.joda.time.ReadablePeriod;
import org.joda.time.Seconds;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for {@link PeriodFormatter}.
 */
public class PeriodFormatterTest {

    /**
     * Verifies that calling print() on a formatter created without a printer
     * correctly throws an UnsupportedOperationException.
     */
    @Test
    public void print_shouldThrowUnsupportedOperationException_whenPrinterIsNotSet() {
        // Arrange: Create a formatter that is configured to only parse, not print.
        // The PeriodPrinter is set to null, disabling printing functionality.
        PeriodFormatter formatter = new PeriodFormatter(null, null);
        ReadablePeriod period = Seconds.ZERO;

        // Act & Assert: Attempting to print should throw an exception.
        // We use assertThrows to verify the exception type and capture the instance.
        UnsupportedOperationException thrown = assertThrows(
            UnsupportedOperationException.class,
            () -> formatter.print(period)
        );

        // Assert: Verify the exception message is as expected.
        assertEquals("Printing not supported", thrown.getMessage());
    }
}