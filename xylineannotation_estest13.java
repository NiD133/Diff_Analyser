package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

/**
 * Tests for the {@link XYLineAnnotation} class, focusing on constructor and getters.
 */
public class XYLineAnnotationTest {

    private static final double DELTA = 1e-9;

    /**
     * Verifies that the constructor correctly initializes the line coordinates
     * and that the getter methods return these values.
     */
    @Test
    public void gettersShouldReturnCoordinatesSetInConstructor() {
        // Arrange: Define distinct coordinates and style properties for the annotation.
        final double x1 = 10.0;
        final double y1 = 20.0;
        final double x2 = 30.0;
        final double y2 = 40.0;
        final Stroke stroke = new BasicStroke(2.0f);
        final Paint paint = Color.RED;

        // Act: Create an instance of the annotation.
        XYLineAnnotation annotation = new XYLineAnnotation(x1, y1, x2, y2, stroke, paint);

        // Assert: Confirm that each getter returns the corresponding value passed to the constructor.
        assertEquals("The x1 coordinate should match the constructor argument.", x1, annotation.getX1(), DELTA);
        assertEquals("The y1 coordinate should match the constructor argument.", y1, annotation.getY1(), DELTA);
        assertEquals("The x2 coordinate should match the constructor argument.", x2, annotation.getX2(), DELTA);
        assertEquals("The y2 coordinate should match the constructor argument.", y2, annotation.getY2(), DELTA);
    }
}