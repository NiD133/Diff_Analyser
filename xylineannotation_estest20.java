package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.assertNotEquals;

/**
 * This test class focuses on the equals() method of the XYLineAnnotation class.
 */
public class XYLineAnnotation_ESTestTest20 extends XYLineAnnotation_ESTest_scaffolding {

    /**
     * Verifies that the equals() method returns false for two annotations
     * that differ only in their y2 coordinate. This also implicitly tests
     * the symmetry of the equals contract.
     */
    @Test
    public void equals_shouldReturnFalse_whenY2CoordinatesDiffer() {
        // Arrange: Create two line annotations where all properties are identical
        // except for the y2 coordinate.
        XYLineAnnotation annotation1 = new XYLineAnnotation(10.0, 20.0, 30.0, 40.0);
        XYLineAnnotation annotation2 = new XYLineAnnotation(10.0, 20.0, 30.0, 50.0); // y2 is different

        // Act & Assert: The two annotations should not be equal.
        assertNotEquals(annotation1, annotation2);
        
        // For completeness, also assert the symmetric property of the equals contract.
        assertNotEquals(annotation2, annotation1);
    }
}