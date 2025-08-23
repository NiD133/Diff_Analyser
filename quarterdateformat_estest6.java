package org.jfree.chart.axis;

import org.junit.Test;
import java.util.TimeZone;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link QuarterDateFormat} class, focusing on constructor argument validation.
 */
public class QuarterDateFormatTest {

    /**
     * Verifies that the constructor throws an {@link IllegalArgumentException}
     * when the provided TimeZone is null. The constructor should not accept
     * a null 'zone' argument.
     */
    @Test
    public void constructorShouldThrowIllegalArgumentExceptionForNullTimeZone() {
        // Arrange: Define arguments for the constructor, with a null TimeZone
        // to trigger the expected exception.
        TimeZone nullZone = null;
        String[] quarterSymbols = QuarterDateFormat.REGULAR_QUARTERS;
        boolean quarterFirst = true;

        // Act & Assert: Attempt to create an instance and verify the specific exception.
        try {
            new QuarterDateFormat(nullZone, quarterSymbols, quarterFirst);
            fail("Expected an IllegalArgumentException to be thrown, but no exception occurred.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct, ensuring the right validation failed.
            assertEquals("Null 'zone' argument.", e.getMessage());
        }
    }
}