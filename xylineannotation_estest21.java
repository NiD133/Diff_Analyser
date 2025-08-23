package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

/**
 * Tests for the equals() method in the {@link XYLineAnnotation} class.
 */
public class XYLineAnnotation_ESTestTest21 extends XYLineAnnotation_ESTest_scaffolding {

    /**
     * Tests that two XYLineAnnotation objects are not considered equal if their
     * coordinates or styling (stroke and paint) are different.
     */
    @Test
    public void equals_shouldReturnFalse_whenAnnotationsHaveDifferentAttributes() {
        // Arrange
        // Create a standard annotation with default styling.
        XYLineAnnotation annotation1 = new XYLineAnnotation(10.0, 20.0, 30.0, 40.0);

        // Create a second annotation with different end coordinates (x2, y2)
        // and custom styling.
        Stroke customStroke = new BasicStroke(2.0f);
        Paint customPaint = Color.RED;
        XYLineAnnotation annotation2 = new XYLineAnnotation(
                10.0, 20.0, 50.0, 60.0, customStroke, customPaint);

        // Act
        boolean areEqual = annotation1.equals(annotation2);

        // Assert
        assertFalse("Annotations with different coordinates and styles should not be equal", areEqual);
    }
}