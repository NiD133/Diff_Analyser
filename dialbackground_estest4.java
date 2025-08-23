package org.jfree.chart.plot.dial;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.Paint;
import org.junit.Test;

/**
 * Unit tests for the {@link DialBackground} class.
 */
public class DialBackgroundTest {

    /**
     * Verifies that the constructor throws an IllegalArgumentException when a null
     * 'paint' argument is provided, as this is not a permitted value.
     */
    @Test
    public void constructor_shouldThrowExceptionForNullPaint() {
        try {
            // Attempt to create a DialBackground with a null Paint object.
            new DialBackground((Paint) null);
            fail("Expected an IllegalArgumentException to be thrown, but it was not.");
        } catch (IllegalArgumentException e) {
            // This is the expected outcome.
            // Verify that the exception message is correct.
            assertEquals("Null 'paint' argument.", e.getMessage());
        }
    }
}