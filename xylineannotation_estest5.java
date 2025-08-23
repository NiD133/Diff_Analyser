package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

/**
 * Unit tests for the XYLineAnnotation class, focusing on the equals() method.
 */
// The original test class name and inheritance are preserved to maintain compatibility
// with the existing test suite structure.
public class XYLineAnnotation_ESTestTest5 extends XYLineAnnotation_ESTest_scaffolding {

    /**
     * Verifies that the equals() method returns false when comparing two
     * XYLineAnnotation objects that have different coordinates, strokes, and paints.
     */
    @Test
    public void equals_shouldReturnFalse_forAnnotationsWithDifferentAttributes() {
        // Arrange: Create two distinct XYLineAnnotation objects.

        // The first annotation uses the simple constructor, which applies a default
        // stroke (1.0f) and paint (Color.BLACK).
        XYLineAnnotation annotation1 = new XYLineAnnotation(1.0, -222.6, 0.05, 500.0);

        // The second annotation is created with completely different coordinates
        // and a custom stroke and paint.
        Stroke customStroke = new BasicStroke(2.0f);
        Paint customPaint = Color.RED;
        XYLineAnnotation annotation2 = new XYLineAnnotation(-1160.6, 1402.9, 1.0, 1.0, customStroke, customPaint);

        // Act: Compare the two different annotations for equality.
        boolean areEqual = annotation1.equals(annotation2);

        // Assert: The annotations should be considered not equal.
        assertFalse("Annotations with different attributes should not be equal", areEqual);
    }
}