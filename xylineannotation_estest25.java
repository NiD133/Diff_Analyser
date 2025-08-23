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
// Note: The original test class name "XYLineAnnotation_ESTestTest25" has been
// renamed to "XYLineAnnotationTest" to follow standard naming conventions.
public class XYLineAnnotationTest {

    /**
     * Verifies that the equals() method returns false when the annotation is
     * compared with an object of a different, unrelated type.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentTypeObject() {
        // Arrange: Create an instance of the annotation and an object of a different type.
        Stroke stroke = new BasicStroke(1.0f);
        Paint paint = Color.BLACK;
        XYLineAnnotation annotation = new XYLineAnnotation(10.0, 20.0, 30.0, 40.0, stroke, paint);

        Object nonAnnotationObject = new Object();

        // Act: Compare the annotation with the other object.
        boolean isEqual = annotation.equals(nonAnnotationObject);

        // Assert: The result should be false.
        assertFalse("An XYLineAnnotation should not be equal to an object of a different type.", isEqual);
    }
}