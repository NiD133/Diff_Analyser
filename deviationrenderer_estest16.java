package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link DeviationRenderer} class, focusing on input validation.
 */
public class DeviationRendererTest {

    /**
     * Verifies that the setAlpha() method throws an IllegalArgumentException when provided
     * with an alpha value greater than 1.0. The valid range for alpha is [0.0, 1.0].
     */
    @Test
    public void setAlpha_shouldThrowIllegalArgumentException_whenAlphaIsGreaterThanOne() {
        // Arrange
        // DeviationStepRenderer is a concrete subclass of DeviationRenderer, suitable for testing.
        DeviationRenderer renderer = new DeviationStepRenderer(false, true);
        float invalidAlpha = 1.1f; // A value clearly outside the valid range.
        String expectedErrorMessage = "Requires 'alpha' in the range 0.0 to 1.0.";

        // Act & Assert
        try {
            renderer.setAlpha(invalidAlpha);
            fail("Expected an IllegalArgumentException because the alpha value is out of range.");
        } catch (IllegalArgumentException e) {
            // Verify that the correct exception with the expected message was thrown.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}