package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link DeviationRenderer} class, focusing on its
 * constructor and equals method.
 */
public class DeviationRendererTest {

    /**
     * Tests that the constructor correctly initializes the renderer with its
     * default properties. The default alpha transparency should be 0.5f, and
     * drawing the series line as a path should be enabled.
     */
    @Test
    public void constructor_shouldInitializeWithDefaultValues() {
        // Arrange & Act: Create a new DeviationRenderer instance.
        // The constructor call is the action being tested.
        DeviationRenderer renderer = new DeviationRenderer(false, false);

        // Assert: Verify that the default properties are set as expected.
        assertTrue("The 'drawSeriesLineAsPath' property should be true by default.",
                renderer.getDrawSeriesLineAsPath());
        assertEquals("The default alpha value should be 0.5f.",
                0.5F, renderer.getAlpha(), 0.0f);
    }

    /**
     * Tests that the equals() method returns false when a DeviationRenderer
     * instance is compared with an object of a completely different type.
     */
    @Test
    public void equals_shouldReturnFalse_forInstanceOfDifferentClass() {
        // Arrange
        DeviationRenderer renderer = new DeviationRenderer(false, false);
        Object otherObject = new Object();

        // Act
        boolean isEqual = renderer.equals(otherObject);

        // Assert
        assertFalse("The equals() method should return false when comparing to an object of a different type.",
                isEqual);
    }
}