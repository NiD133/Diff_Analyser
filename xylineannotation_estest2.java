package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

/**
 * Unit tests for the equals() method in the {@link XYLineAnnotation} class.
 */
public class XYLineAnnotation_ESTestTest2 extends XYLineAnnotation_ESTest_scaffolding {

    /**
     * Tests that two XYLineAnnotation objects are not considered equal if their
     * y2 coordinates differ, while all other properties are the same.
     */
    @Test
    public void equals_shouldReturnFalse_whenY2CoordinatesDiffer() {
        // Arrange
        // Define common properties for two annotations to ensure we only test one difference.
        double x1 = 10.0;
        double y1 = 1.0;
        double x2 = 1.0;
        Stroke commonStroke = new BasicStroke(1.0f);
        Paint commonPaint = Color.RED;

        // Create two annotations that differ only in their y2 coordinate.
        XYLineAnnotation annotation1 = new XYLineAnnotation(x1, y1, x2, 100.0, commonStroke, commonPaint);
        XYLineAnnotation annotation2 = new XYLineAnnotation(x1, y1, x2, 200.0, commonStroke, commonPaint);

        // Act
        boolean areEqual = annotation1.equals(annotation2);

        // Assert
        assertFalse("Annotations with different y2 coordinates should not be equal.", areEqual);
    }
}