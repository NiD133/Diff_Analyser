package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

/**
 * Contains tests for the getter methods of the {@link XYLineAnnotation} class.
 * This class name and inheritance are preserved from the original auto-generated test.
 */
public class XYLineAnnotation_ESTestTest14 extends XYLineAnnotation_ESTest_scaffolding {

    /**
     * Verifies that the constructor correctly initializes the line coordinates
     * and that the corresponding getter methods return these values.
     */
    @Test
    public void testGettersReturnCorrectCoordinates() {
        // Arrange: Define the coordinates and visual properties for the annotation.
        double expectedX1 = 0.0;
        double expectedY1 = 0.0;
        double expectedX2 = -1423.746;
        double expectedY2 = 0.0;
        Stroke expectedStroke = new BasicStroke(1.0f);
        Paint expectedPaint = Color.BLACK;

        // Act: Create the XYLineAnnotation instance with the defined properties.
        XYLineAnnotation annotation = new XYLineAnnotation(
                expectedX1, expectedY1, expectedX2, expectedY2,
                expectedStroke, expectedPaint);

        // Assert: Confirm that the getter methods return the values set in the constructor.
        assertEquals("The x1 coordinate should match the constructor argument.",
                expectedX1, annotation.getX1(), 0.0);
        assertEquals("The y1 coordinate should match the constructor argument.",
                expectedY1, annotation.getY1(), 0.0);
        assertEquals("The x2 coordinate should match the constructor argument.",
                expectedX2, annotation.getX2(), 0.0);
        assertEquals("The y2 coordinate should match the constructor argument.",
                expectedY2, annotation.getY2(), 0.0);
    }
}