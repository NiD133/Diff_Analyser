package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * A collection of tests for the {@link XYLineAnnotation} class, focusing on
 * its constructor and property accessors.
 */
public class XYLineAnnotationTest {

    /** A small tolerance for floating-point comparisons. */
    private static final double DELTA = 1e-9;

    /**
     * Verifies that the constructor correctly initializes the line coordinates
     * and that the corresponding getter methods return these values.
     */
    @Test
    public void whenConstructed_thenGettersReturnCorrectCoordinates() {
        // Arrange: Define the coordinates for the start and end points of the line.
        final double startX = 0.0;
        final double startY = 2448.47017008;
        final double endX = 0.0;
        final double endY = 2448.47017008;

        // Act: Create the annotation with the specified coordinates.
        XYLineAnnotation annotation = new XYLineAnnotation(startX, startY, endX, endY);

        // Assert: Check that each coordinate getter returns the expected value.
        assertEquals("The x1 coordinate should match the constructor argument.",
                     startX, annotation.getX1(), DELTA);
        assertEquals("The y1 coordinate should match the constructor argument.",
                     startY, annotation.getY1(), DELTA);
        assertEquals("The x2 coordinate should match the constructor argument.",
                     endX, annotation.getX2(), DELTA);
        assertEquals("The y2 coordinate should match the constructor argument.",
                     endY, annotation.getY2(), DELTA);
    }
}