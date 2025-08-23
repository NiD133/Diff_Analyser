package org.joda.time.format;

import org.joda.time.ReadablePeriod;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link PeriodFormatter}.
 */
public class PeriodFormatterTest {

    /**
     * Tests that calling print() with a null period throws an IllegalArgumentException.
     */
    @Test
    public void testPrint_withNullPeriod_throwsIllegalArgumentException() {
        // Arrange: Create a basic formatter. The internal printer/parser implementation
        // is not relevant for this test, as the null check happens before they are used.
        PeriodFormatter formatter = new PeriodFormatter(
                PeriodFormatterBuilder.Literal.EMPTY,
                PeriodFormatterBuilder.Literal.EMPTY);

        // Act & Assert: Attempt to print a null period and verify the resulting exception.
        try {
            formatter.print((ReadablePeriod) null);
            fail("Expected an IllegalArgumentException to be thrown for a null period.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception has the expected message.
            assertEquals("Period must not be null", e.getMessage());
        }
    }
}