package org.joda.time.format;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * This test class contains tests for the {@link PeriodFormatter}.
 * This specific test focuses on the behavior of the getPrinter() method.
 */
public class PeriodFormatter_ESTestTest8 extends PeriodFormatter_ESTest_scaffolding {

    /**
     * Tests that getPrinter() returns null if the PeriodFormatter was constructed
     * with a null printer component.
     */
    @Test
    public void getPrinter_shouldReturnNull_whenFormatterIsCreatedWithNullPrinter() {
        // Arrange: Create a PeriodFormatter with no printer and no parser.
        // The constructor allows null for either or both components.
        PeriodFormatter formatter = new PeriodFormatter(null, null);

        // Act: Retrieve the printer from the formatter.
        PeriodPrinter retrievedPrinter = formatter.getPrinter();

        // Assert: The retrieved printer should be null, matching how it was constructed.
        assertNull("The printer should be null as it was initialized.", retrievedPrinter);
    }
}