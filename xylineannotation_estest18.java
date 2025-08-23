package org.jfree.chart.annotations;

import org.jfree.chart.plot.CategoryPlot;
import org.junit.Test;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link XYLineAnnotation} class.
 */
public class XYLineAnnotationTest {

    /**
     * Verifies that the equals() method returns false for two annotations that
     * have the same coordinates but different strokes.
     */
    @Test
    public void equals_shouldReturnFalse_whenStrokesAreDifferent() {
        // Arrange
        // Create an annotation with the default stroke (solid, 1.0f width).
        XYLineAnnotation annotationWithDefaultStroke = new XYLineAnnotation(10.0, 20.0, 30.0, 40.0);

        // Create a second annotation with the same coordinates but a different stroke.
        // CategoryPlot.DEFAULT_GRIDLINE_STROKE is a dashed stroke, which differs
        // from the solid stroke used by the default constructor.
        Stroke customStroke = CategoryPlot.DEFAULT_GRIDLINE_STROKE;
        
        // The paint is explicitly set to black, which matches the default paint,
        // ensuring the inequality is due to the stroke alone.
        Paint paint = Color.BLACK;
        
        XYLineAnnotation annotationWithCustomStroke = new XYLineAnnotation(10.0, 20.0, 30.0, 40.0, customStroke, paint);

        // Act
        boolean areEqual = annotationWithDefaultStroke.equals(annotationWithCustomStroke);

        // Assert
        assertFalse("Annotations with different strokes should not be considered equal.", areEqual);
    }
}