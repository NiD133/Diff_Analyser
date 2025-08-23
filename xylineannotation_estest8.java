package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

/**
 * Unit tests for the XYLineAnnotation class.
 */
public class XYLineAnnotationTest {

    /**
     * Verifies that the constructor correctly initializes all properties of the
     * annotation (coordinates, stroke, and paint) and that the corresponding
     * getter methods return these values.
     */
    @Test
    public void constructorShouldSetAllPropertiesCorrectly() {
        // Arrange: Define the properties for the line annotation.
        // Using descriptive variables makes the test's intent clear.
        double expectedX1 = 2545.5;
        double expectedY1 = -909.0;
        double expectedX2 = -1423.676;
        double expectedY2 = 0.0;
        Stroke expectedStroke = new BasicStroke(2.0f);
        Paint expectedPaint = Color.RED;

        // Act: Create an instance of XYLineAnnotation using the full constructor.
        XYLineAnnotation annotation = new XYLineAnnotation(
                expectedX1, expectedY1, expectedX2, expectedY2,
                expectedStroke, expectedPaint
        );

        // Assert: Verify that each getter returns the value set in the constructor.
        // Assertions are grouped logically and include messages for clarity.
        assertEquals("The x1 coordinate should match the constructor argument.",
                expectedX1, annotation.getX1(), 0.01);
        assertEquals("The y1 coordinate should match the constructor argument.",
                expectedY1, annotation.getY1(), 0.01);
        assertEquals("The x2 coordinate should match the constructor argument.",
                expectedX2, annotation.getX2(), 0.01);
        assertEquals("The y2 coordinate should match the constructor argument.",
                expectedY2, annotation.getY2(), 0.01);

        // The original test missed verifying these important style properties.
        assertSame("The stroke object should be the same instance provided to the constructor.",
                expectedStroke, annotation.getStroke());
        assertSame("The paint object should be the same instance provided to the constructor.",
                expectedPaint, annotation.getPaint());
    }
}