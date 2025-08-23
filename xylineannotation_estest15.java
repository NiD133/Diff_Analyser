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
     * Verifies that the constructor correctly initializes the annotation's properties
     * and that the corresponding getter methods return these values.
     */
    @Test
    public void testConstructorAndGetters() {
        // Arrange: Define the properties for the line annotation.
        final double expectedX1 = -1992.0;
        final double expectedY1 = 1.0;
        final double expectedX2 = 0.04;
        final double expectedY2 = 1.0;
        final Stroke expectedStroke = new BasicStroke(2.0f);
        final Paint expectedPaint = Color.RED;

        // Act: Create a new XYLineAnnotation instance.
        XYLineAnnotation annotation = new XYLineAnnotation(
            expectedX1, expectedY1, expectedX2, expectedY2,
            expectedStroke, expectedPaint
        );

        // Assert: Confirm that each getter returns the value provided to the constructor.
        assertEquals("The x1 coordinate should match the constructor argument.",
                     expectedX1, annotation.getX1(), 0.01);
        assertEquals("The y1 coordinate should match the constructor argument.",
                     expectedY1, annotation.getY1(), 0.01);
        assertEquals("The x2 coordinate should match the constructor argument.",
                     expectedX2, annotation.getX2(), 0.01);
        assertEquals("The y2 coordinate should match the constructor argument.",
                     expectedY2, annotation.getY2(), 0.01);
        assertSame("The stroke object should be the same instance passed to the constructor.",
                   expectedStroke, annotation.getStroke());
        assertSame("The paint object should be the same instance passed to the constructor.",
                   expectedPaint, annotation.getPaint());
    }
}