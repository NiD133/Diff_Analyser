package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link XYLineAnnotation} class.
 */
public class XYLineAnnotationTest {

    /**
     * Verifies that two XYLineAnnotation instances with different coordinates are not
     * considered equal by the equals() method.
     */
    @Test
    public void equals_shouldReturnFalse_whenCoordinatesDiffer() {
        // Arrange: Create two annotations with different coordinates.
        // The default stroke and paint will be the same for both.
        XYLineAnnotation annotation1 = new XYLineAnnotation(10.0, 20.0, 30.0, 40.0);
        XYLineAnnotation annotation2 = new XYLineAnnotation(10.0, 99.0, 30.0, 99.0); // y1 and y2 are different

        // Act & Assert: The two objects should not be equal.
        // Using assertNotEquals is more expressive than assertFalse(annotation1.equals(annotation2)).
        assertNotEquals(annotation1, annotation2);
    }
}