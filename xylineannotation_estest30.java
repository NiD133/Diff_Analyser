package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link XYLineAnnotation} class, focusing on its constructor
 * and coordinate getter methods.
 */
public class XYLineAnnotationTest {

    /**
     * A small tolerance for floating-point comparisons.
     */
    private static final double DELTA = 1e-9;

    /**
     * Verifies that the constructor correctly initializes the line coordinates
     * and that the corresponding getter methods return these values.
     */
    @Test
    public void constructorShouldSetCoordinatesAndGettersShouldReturnThem() {
        // Arrange: Define the coordinates for the line annotation.
        double startX = 500.0;
        double startY = 1069.473;
        double endX = 0.25;
        double endY = 0.25;

        // Act: Create a new XYLineAnnotation with the specified coordinates.
        XYLineAnnotation annotation = new XYLineAnnotation(startX, startY, endX, endY);

        // Assert: Verify that the getters return the values set in the constructor.
        assertEquals("The x1 coordinate should be correctly initialized.", startX, annotation.getX1(), DELTA);
        assertEquals("The y1 coordinate should be correctly initialized.", startY, annotation.getY1(), DELTA);
        assertEquals("The x2 coordinate should be correctly initialized.", endX, annotation.getX2(), DELTA);
        assertEquals("The y2 coordinate should be correctly initialized.", endY, annotation.getY2(), DELTA);
    }
}