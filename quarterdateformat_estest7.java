package org.jfree.chart.axis;

import org.junit.Test;
import java.util.TimeZone;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link QuarterDateFormat} class.
 */
public class QuarterDateFormatTest {

    /**
     * Verifies that the constructor throws an IllegalArgumentException
     * when the 'zone' argument is null, as this is not permitted.
     */
    @Test
    public void constructor_withNullTimeZone_shouldThrowIllegalArgumentException() {
        // Arrange: Define arguments for the constructor, with a null TimeZone.
        String[] quarterSymbols = QuarterDateFormat.REGULAR_QUARTERS;
        TimeZone nullZone = null;

        // Act & Assert: Attempt to create an instance and verify the exception.
        try {
            new QuarterDateFormat(nullZone, quarterSymbols);
            fail("Expected an IllegalArgumentException to be thrown for a null TimeZone.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct.
            assertEquals("Null 'zone' argument.", e.getMessage());
        }
    }
}