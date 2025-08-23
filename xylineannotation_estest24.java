package org.jfree.chart.annotations;

import org.junit.Test;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the equals() method of the {@link XYLineAnnotation} class.
 */
public class XYLineAnnotationEqualityTest {

    /**
     * Verifies that the equals() method returns false when comparing
     * an XYLineAnnotation with an object of a completely different type.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentObjectType() {
        // Arrange
        final Stroke stroke = new BasicStroke(1.0f);
        final Paint paint = Color.BLACK;
        XYLineAnnotation lineAnnotation = new XYLineAnnotation(1.0, 2.0, 3.0, 4.0, stroke, paint);

        // An object of a different, unrelated type for comparison
        Object otherObject = new Object();

        // Act
        boolean isEqual = lineAnnotation.equals(otherObject);

        // Assert
        assertFalse("An XYLineAnnotation should not be equal to an object of a different type.", isEqual);
    }
}