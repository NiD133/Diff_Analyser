package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link DeviationRenderer#setAlpha(float)} method, focusing on invalid arguments.
 */
public class DeviationRendererTest {

    /**
     * Verifies that setAlpha() throws an IllegalArgumentException when provided with a negative value.
     * The alpha value must be within the range [0.0, 1.0].
     */
    @Test
    public void setAlpha_shouldThrowIllegalArgumentException_whenAlphaIsNegative() {
        // Arrange
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        float invalidNegativeAlpha = -0.5f;
        String expectedMessage = "Requires 'alpha' in the range 0.0 to 1.0.";

        // Act & Assert
        try {
            renderer.setAlpha(invalidNegativeAlpha);
            fail("Expected an IllegalArgumentException to be thrown for a negative alpha value.");
        } catch (IllegalArgumentException e) {
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}