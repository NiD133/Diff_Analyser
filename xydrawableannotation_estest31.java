package org.jfree.chart.annotations;

import org.jfree.chart.Drawable;
import org.jfree.chart.title.TextTitle;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

/**
 * Unit tests for the {@link XYDrawableAnnotation} class, focusing on cloning and equality.
 */
public class XYDrawableAnnotationTest {

    /**
     * This test verifies that the clone() method creates a new instance
     * that is a deep copy and is equal to the original object.
     */
    @Test
    public void clone_shouldCreateEqualButSeparateInstance() throws CloneNotSupportedException {
        // Arrange: Create an original annotation with clear, simple values.
        Drawable drawable = new TextTitle("Test Annotation");
        double x = 10.0;
        double y = 20.0;
        double width = 100.0;
        double height = 50.0;
        XYDrawableAnnotation originalAnnotation = new XYDrawableAnnotation(x, y, width, height, drawable);

        // Act: Create a clone of the original annotation.
        XYDrawableAnnotation clonedAnnotation = (XYDrawableAnnotation) originalAnnotation.clone();

        // Assert: The clone should be a different object instance but equal in value to the original.
        assertNotSame("A clone must be a different object in memory.", originalAnnotation, clonedAnnotation);
        assertEquals("A clone should be equal to the original object.", originalAnnotation, clonedAnnotation);
        
        // As per the equals/hashCode contract, equal objects must have the same hash code.
        assertEquals("Equal objects must have the same hash code.", 
                     originalAnnotation.hashCode(), clonedAnnotation.hashCode());
    }
}