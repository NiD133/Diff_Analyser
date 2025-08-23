package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Contains tests for the equals() method of the {@link XYLineAnnotation} class.
 */
public class XYLineAnnotationTest {

    /**
     * Tests that two XYLineAnnotation objects are not considered equal if their
     * starting x-coordinates (x1) are different.
     */
    @Test
    public void equals_returnsFalse_whenX1CoordinatesDiffer() {
        // Arrange: Create two annotations that are identical except for the x1 coordinate.
        // Using simple, whole numbers makes the difference obvious.
        XYLineAnnotation annotation1 = new XYLineAnnotation(1.0, 2.0, 3.0, 4.0);
        XYLineAnnotation annotation2 = new XYLineAnnotation(99.0, 2.0, 3.0, 4.0); // Different x1

        // Act & Assert: The equals method should return false.
        // We also check the hashCode contract and symmetry of the equals method.
        assertFalse("Annotations with different x1 coordinates should not be equal.", annotation1.equals(annotation2));
        assertNotEquals("Hash codes should differ for unequal objects.", annotation1.hashCode(), annotation2.hashCode());
    }
}