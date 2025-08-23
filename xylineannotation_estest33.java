package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

/**
 * A test suite for the {@link XYLineAnnotation} class, focusing on cloning and equality.
 */
public class XYLineAnnotationTest {

    /**
     * Verifies that the clone() method creates a new instance that is
     * logically equal to the original object.
     */
    @Test
    public void clone_shouldCreateAnEqualButNotSameInstance() throws CloneNotSupportedException {
        // Arrange: Create an original annotation with specific properties.
        Stroke originalStroke = new BasicStroke(1.5f);
        Paint originalPaint = Color.RED;
        XYLineAnnotation originalAnnotation = new XYLineAnnotation(
                10.0, 20.0, 30.0, 40.0, originalStroke, originalPaint);

        // Act: Clone the original annotation.
        XYLineAnnotation clonedAnnotation = (XYLineAnnotation) originalAnnotation.clone();

        // Assert: The clone should be a different object instance...
        assertNotSame("The cloned object should be a new instance.",
                originalAnnotation, clonedAnnotation);
        
        // ...but it should be logically equal to the original.
        assertEquals("The cloned object should be equal to the original.",
                originalAnnotation, clonedAnnotation);

        // Explicitly verify that all properties of the clone match the original
        // to ensure the clone was created correctly.
        assertEquals(10.0, clonedAnnotation.getX1(), 0.0);
        assertEquals(20.0, clonedAnnotation.getY1(), 0.0);
        assertEquals(30.0, clonedAnnotation.getX2(), 0.0);
        assertEquals(40.0, clonedAnnotation.getY2(), 0.0);
        assertEquals(originalStroke, clonedAnnotation.getStroke());
        assertEquals(originalPaint, clonedAnnotation.getPaint());
    }
}