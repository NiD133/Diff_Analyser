package org.jfree.chart.plot.dial;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link DialBackground} class.
 */
public class DialBackgroundTest {

    /**
     * Verifies that setGradientPaintTransformer() throws an IllegalArgumentException
     * when a null argument is provided. This is the expected behavior to prevent
     * null pointer exceptions later.
     */
    @Test
    public void setGradientPaintTransformer_whenGivenNull_shouldThrowIllegalArgumentException() {
        // Arrange: Create an instance of the class under test.
        DialBackground dialBackground = new DialBackground();

        // Act & Assert: Call the method with a null argument and verify the resulting exception.
        try {
            dialBackground.setGradientPaintTransformer(null);
            fail("Expected an IllegalArgumentException to be thrown, but it was not.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct, confirming the right check failed.
            assertEquals("Null 't' argument.", e.getMessage());
        }
    }
}