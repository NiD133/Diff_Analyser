package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

/**
 * Unit tests for the {@link XYLineAnnotation} class, focusing on its data accessors.
 */
public class XYLineAnnotationTest {

    private static final double DELTA = 0.01;

    /**
     * Verifies that the getX1() method correctly returns the x-coordinate of the
     * line's starting point, as provided in the constructor.
     */
    @Test
    public void getX1_shouldReturnCorrectStartingXCoordinate() {
        // Arrange: Define the coordinates and style for the annotation.
        final double expectedX1 = 2545.5;
        final double y1 = -909.0;
        final double x2 = -1423.676;
        final double y2 = 0.0;
        final Stroke stroke = new BasicStroke(1.0f);
        final Paint paint = Color.BLACK;

        XYLineAnnotation annotation = new XYLineAnnotation(expectedX1, y1, x2, y2, stroke, paint);

        // Act: Retrieve the value using the getter method under test.
        double actualX1 = annotation.getX1();

        // Assert: Check if the retrieved value matches the expected value.
        assertEquals("The x-coordinate of the starting point should match the constructor argument.",
                expectedX1, actualX1, DELTA);
        
        // For completeness, we also verify that other properties were set correctly.
        assertEquals("Y1 coordinate mismatch", y1, annotation.getY1(), DELTA);
        assertEquals("X2 coordinate mismatch", x2, annotation.getX2(), DELTA);
        assertEquals("Y2 coordinate mismatch", y2, annotation.getY2(), DELTA);
    }
}