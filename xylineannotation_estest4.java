package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the {@link XYLineAnnotation} class, focusing on its equality logic.
 */
public class XYLineAnnotationTest {

    /**
     * Verifies that the equals() method returns false when comparing two
     * XYLineAnnotation objects with different coordinates.
     */
    @Test
    public void equals_shouldReturnFalse_whenCoordinatesDiffer() {
        // Arrange: Create two line annotations with different start and end points.
        // The default stroke and paint will be the same for both.
        XYLineAnnotation annotation1 = new XYLineAnnotation(0.0, -7304.15, 1447.80, 1447.80);
        XYLineAnnotation annotation2 = new XYLineAnnotation(0.0, 2448.47, 0.0, 2448.47);

        // Act: Compare the two different annotations for equality.
        boolean areEqual = annotation1.equals(annotation2);

        // Assert: The result should be false because the objects are not equivalent.
        assertFalse("Annotations with different coordinates should not be considered equal.", areEqual);
    }
}