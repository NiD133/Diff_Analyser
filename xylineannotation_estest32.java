package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains unit tests for the {@link XYLineAnnotation} class.
 */
public class XYLineAnnotationTest {

    /**
     * A small tolerance for floating-point comparisons.
     */
    private static final double DELTA = 0.000001;

    /**
     * Verifies that the constructor correctly initializes the line coordinates
     * and that the corresponding getter methods return these values.
     */
    @Test
    public void constructorShouldSetCoordinatesCorrectly() {
        // Arrange: Define the expected coordinates for the line annotation.
        final double expectedX1 = 500.0;
        final double expectedY1 = 1069.473;
        final double expectedX2 = 0.25;
        final double expectedY2 = 0.25;

        // Act: Create a new XYLineAnnotation instance using the constructor.
        XYLineAnnotation annotation = new XYLineAnnotation(expectedX1, expectedY1, expectedX2, expectedY2);

        // Assert: Check that each getter returns the correct coordinate value.
        assertEquals("The x1 coordinate should match the value provided to the constructor.",
                expectedX1, annotation.getX1(), DELTA);
        assertEquals("The y1 coordinate should match the value provided to the constructor.",
                expectedY1, annotation.getY1(), DELTA);
        assertEquals("The x2 coordinate should match the value provided to the constructor.",
                expectedX2, annotation.getX2(), DELTA);
        assertEquals("The y2 coordinate should match the value provided to the constructor.",
                expectedY2, annotation.getY2(), DELTA);
    }
}