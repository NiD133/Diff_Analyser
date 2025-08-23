package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link XYLineAnnotation} class.
 */
public class XYLineAnnotationTest {

    /**
     * Tests the reflexivity of the equals() method.
     * An object must always be equal to itself.
     */
    @Test
    public void equals_onSameInstance_shouldReturnTrue() {
        // Arrange: Create an annotation with simple, clear coordinates.
        XYLineAnnotation annotation = new XYLineAnnotation(10.0, 20.0, 30.0, 40.0);

        // Act: Compare the instance with itself.
        boolean isEqual = annotation.equals(annotation);

        // Assert: The result should be true.
        assertTrue("An instance of XYLineAnnotation should be equal to itself.", isEqual);
    }
}