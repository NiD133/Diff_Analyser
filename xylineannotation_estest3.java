package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

public class XYLineAnnotation_ESTestTest3 extends XYLineAnnotation_ESTest_scaffolding {

    /**
     * Tests that the equals() method returns false when comparing two annotations
     * with different properties.
     */
    @Test
    public void equals_shouldReturnFalse_whenAnnotationsHaveDifferentProperties() {
        // Arrange: Create two distinct XYLineAnnotation objects.
        // They differ in their end coordinates (x2, y2), stroke, and paint.
        XYLineAnnotation annotation1 = new XYLineAnnotation(
                10.0, 20.0, 30.0, 40.0, new BasicStroke(1.0f), Color.BLUE);

        XYLineAnnotation annotation2 = new XYLineAnnotation(
                10.0, 20.0, 50.0, 60.0, new BasicStroke(2.0f), Color.RED);

        // Act: Compare the two annotations for equality.
        boolean areEqual = annotation1.equals(annotation2);

        // Assert: The annotations should not be considered equal.
        assertFalse("Annotations with different properties should not be equal", areEqual);
    }
}