package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

/**
 * Unit tests for the {@link XYLineAnnotation} class.
 */
public class XYLineAnnotationTest {

    /**
     * Verifies that the constructor correctly initializes all properties
     * and that the corresponding getter methods return the correct values.
     */
    @Test
    public void constructorShouldSetAllPropertiesCorrectly() {
        // Arrange: Define the properties for the line annotation.
        double expectedX1 = 2545.5;
        double expectedY1 = -909.0;
        double expectedX2 = -1423.676;
        double expectedY2 = 0.0;
        Stroke expectedStroke = new BasicStroke(1.0f);
        Paint expectedPaint = Color.BLACK;

        // Act: Create a new XYLineAnnotation instance.
        XYLineAnnotation annotation = new XYLineAnnotation(
                expectedX1, expectedY1, expectedX2, expectedY2, expectedStroke, expectedPaint
        );

        // Assert: Verify that the getters return the values provided to the constructor.
        assertEquals("X1 coordinate should match the constructor argument.",
                expectedX1, annotation.getX1(), 0.01);
        assertEquals("Y1 coordinate should match the constructor argument.",
                expectedY1, annotation.getY1(), 0.01);
        assertEquals("X2 coordinate should match the constructor argument.",
                expectedX2, annotation.getX2(), 0.01);
        assertEquals("Y2 coordinate should match the constructor argument.",
                expectedY2, annotation.getY2(), 0.01);
        assertSame("Stroke object should be the same as the constructor argument.",
                expectedStroke, annotation.getStroke());
        assertSame("Paint object should be the same as the constructor argument.",
                expectedPaint, annotation.getPaint());
    }
}