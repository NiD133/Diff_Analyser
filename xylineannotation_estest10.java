package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

/**
 * Provides tests for the {@link XYLineAnnotation} class.
 */
public class XYLineAnnotationTest {

    private static final double DELTA = 1e-9;

    /**
     * Verifies that the constructor correctly initializes the annotation's
     * properties and that the corresponding getter methods return the
     * expected values.
     */
    @Test
    public void constructorShouldSetAllPropertiesCorrectly() {
        // Arrange: Define the coordinates and style properties for the line.
        // Using distinct, simple values makes the test easier to read and debug.
        double x1 = 10.0;
        double y1 = 20.0;
        double x2 = -30.0;
        double y2 = -40.0;
        Stroke expectedStroke = new BasicStroke(2.0f);
        Paint expectedPaint = Color.RED;

        // Act: Create a new XYLineAnnotation instance using the full constructor.
        XYLineAnnotation annotation = new XYLineAnnotation(x1, y1, x2, y2, expectedStroke, expectedPaint);

        // Assert: Check that all properties were set correctly.
        assertEquals("The x1 coordinate should match the constructor argument.",
                x1, annotation.getX1(), DELTA);
        assertEquals("The y1 coordinate should match the constructor argument.",
                y1, annotation.getY1(), DELTA);
        assertEquals("The x2 coordinate should match the constructor argument.",
                x2, annotation.getX2(), DELTA);
        assertEquals("The y2 coordinate should match the constructor argument.",
                y2, annotation.getY2(), DELTA);
        assertEquals("The stroke should match the constructor argument.",
                expectedStroke, annotation.getStroke());
        assertEquals("The paint should match the constructor argument.",
                expectedPaint, annotation.getPaint());
    }
}