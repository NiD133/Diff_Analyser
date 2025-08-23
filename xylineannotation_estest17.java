package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the constructor of the {@link XYLineAnnotation} class, focusing on argument validation.
 */
public class XYLineAnnotationTest {

    /**
     * Verifies that the constructor throws an IllegalArgumentException when the x1 coordinate
     * is not a finite number. The constructor must reject non-finite values like
     * Double.NEGATIVE_INFINITY to ensure the annotation's state is valid.
     */
    @Test
    public void constructorShouldThrowExceptionForNonFiniteX1() {
        // Arrange: Define coordinates with a non-finite value for x1.
        double nonFiniteX1 = Double.NEGATIVE_INFINITY;
        double y1 = 0.0;
        double x2 = 10.0;
        double y2 = 20.0;

        // Act & Assert
        try {
            new XYLineAnnotation(nonFiniteX1, y1, x2, y2);
            fail("Expected an IllegalArgumentException because the x1 coordinate is not finite.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is clear and correct.
            String expectedMessage = "Require 'x1' (-Infinity) to be finite.";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}