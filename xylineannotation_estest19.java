package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

/**
 * Contains tests for the equals() method of the {@link XYLineAnnotation} class.
 */
public class XYLineAnnotation_ESTestTest19 {

    /**
     * Tests that two XYLineAnnotation objects with identical coordinates and strokes
     * are not considered equal if their paint attribute differs.
     *
     * This scenario was the focus of the original test, which compared an annotation
     * with a default paint (Color.BLACK) to one with a custom paint.
     */
    @Test
    public void equals_shouldReturnFalse_whenAnnotationsHaveDifferentPaints() {
        // Arrange
        final double x1 = 10.0, y1 = 20.0, x2 = 30.0, y2 = 40.0;
        final Stroke commonStroke = new BasicStroke(1.0f);

        // Create an annotation with a specific paint.
        XYLineAnnotation annotationWithBlackPaint = new XYLineAnnotation(
                x1, y1, x2, y2, commonStroke, Color.BLACK);

        // Create another annotation with the same coordinates and stroke, but a different paint.
        XYLineAnnotation annotationWithRedPaint = new XYLineAnnotation(
                x1, y1, x2, y2, commonStroke, Color.RED);

        // Act & Assert
        // The equals() method should return false because the paints are different.
        // Using assertNotEquals is more expressive for this comparison.
        assertNotEquals("Annotations with different paints should not be considered equal",
                annotationWithBlackPaint, annotationWithRedPaint);
    }
}