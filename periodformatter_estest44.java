package org.joda.time.format;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for {@link PeriodFormatter}.
 */
// The original test class name `PeriodFormatter_ESTestTest44` and its scaffolding
// parent suggest it was auto-generated. This improved version is part of the same class.
public class PeriodFormatter_ESTestTest44 extends PeriodFormatter_ESTest_scaffolding {

    /**
     * Tests that isPrinter() returns false when the PeriodFormatter is constructed
     * with a null PeriodPrinter.
     */
    @Test
    public void isPrinter_shouldReturnFalse_whenFormatterHasNoPrinter() {
        // Arrange: Create a PeriodFormatter with a null printer.
        // The parser component is irrelevant for this test, so it can also be null.
        PeriodFormatter formatter = new PeriodFormatter(null, null);

        // Act: Check if the formatter is capable of printing.
        boolean result = formatter.isPrinter();

        // Assert: The formatter should not be identified as a printer.
        assertFalse("A formatter created with a null printer should not be a printer.", result);
    }
}