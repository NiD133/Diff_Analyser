package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link XYLineAnnotation} class, focusing on its constructor and getters.
 */
public class XYLineAnnotationTest {

    private static final double TOLERANCE = 0.0000001;

    /**
     * Verifies that the constructor correctly initializes the line coordinates
     * and that the corresponding getter methods return the expected values.
     */
    @Test
    public void constructorShouldSetAndGettersShouldReturnCorrectCoordinates() {
        // Arrange: Define the coordinates for the line annotation.
        final double expectedX1 = 0.0;
        final double expectedY1 = -7304.152;
        final double expectedX2 = 1447.795;
        final double expectedY2 = 1447.795;

        // Act: Create a new XYLineAnnotation instance using the constructor.
        XYLineAnnotation annotation = new XYLineAnnotation(
                expectedX1, expectedY1, expectedX2, expectedY2);

        // Assert: Confirm that each getter returns the value provided to the constructor.
        assertEquals("The x1 coordinate should match the constructor argument.",
                expectedX1, annotation.getX1(), TOLERANCE);
        assertEquals("The y1 coordinate should match the constructor argument.",
                expectedY1, annotation.getY1(), TOLERANCE);
        assertEquals("The x2 coordinate should match the constructor argument.",
                expectedX2, annotation.getX2(), TOLERANCE);
        assertEquals("The y2 coordinate should match the constructor argument.",
                expectedY2, annotation.getY2(), TOLERANCE);
    }
}