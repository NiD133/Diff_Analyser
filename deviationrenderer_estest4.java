package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the alpha (transparency) property of the {@link DeviationRenderer} class.
 */
public class DeviationRendererAlphaTest {

    private static final float DELTA = 0.001f;

    /**
     * Verifies that the default alpha value is correctly initialized and that the
     * setAlpha() and getAlpha() methods function as expected.
     */
    @Test
    public void testGetAndSetAlpha() {
        // Arrange: Create a new renderer instance.
        DeviationRenderer renderer = new DeviationRenderer();

        // Assert: Check that the default alpha value is 0.5f as per the constructor.
        assertEquals("The default alpha value should be 0.5f.", 0.5f, renderer.getAlpha(), DELTA);

        // Act: Set a new alpha value using the public setter method.
        float newAlpha = 0.75f;
        renderer.setAlpha(newAlpha);

        // Assert: Verify that getAlpha() returns the newly set value.
        float actualAlpha = renderer.getAlpha();
        assertEquals("getAlpha() should return the value set by setAlpha().", newAlpha, actualAlpha, DELTA);
    }
}