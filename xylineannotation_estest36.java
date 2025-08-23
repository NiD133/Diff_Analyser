package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

/**
 * Contains tests for the {@link XYLineAnnotation} class.
 */
public class XYLineAnnotationTest {

    private static final double DELTA = 0.001;

    /**
     * Verifies that the constructor taking only coordinates correctly initializes
     * the line's position and sets the stroke and paint to their default values.
     */
    @Test
    public void constructorWithCoordinatesShouldSetDefaultStrokeAndPaint() {
        // Arrange: Define the coordinates and expected default properties.
        double x1 = 500.0;
        double y1 = 1069.473;
        double x2 = 0.25;
        double y2 = 0.25;

        Stroke expectedDefaultStroke = new BasicStroke(1.0f);
        Paint expectedDefaultPaint = Color.BLACK;

        // Act: Create the annotation using the constructor under test.
        XYLineAnnotation annotation = new XYLineAnnotation(x1, y1, x2, y2);

        // Assert: Verify that all properties are set as expected.
        // Check coordinates
        assertEquals("The x1 coordinate should be correctly set.", x1, annotation.getX1(), DELTA);
        assertEquals("The y1 coordinate should be correctly set.", y1, annotation.getY1(), DELTA);
        assertEquals("The x2 coordinate should be correctly set.", x2, annotation.getX2(), DELTA);
        assertEquals("The y2 coordinate should be correctly set.", y2, annotation.getY2(), DELTA);

        // Check for default stroke and paint, which was missing in the original test
        assertEquals("The stroke should default to a 1.0f width BasicStroke.", expectedDefaultStroke, annotation.getStroke());
        assertEquals("The paint should default to black.", expectedDefaultPaint, annotation.getPaint());
    }
}