package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link BBoxCalculator}.
 */
public class BBoxCalculatorTest {

    /**
     * Verifies that a newly created BBoxCalculator is in a default, "empty" state.
     * In this state, the Y-axis boundaries should be initialized to an inverted and
     * infinite range (minY = +Infinity, maxY = -Infinity), signifying that no
     * points or shapes have been added yet.
     */
    @Test
    public void newCalculatorShouldHaveUninitializedYBounds() {
        // Arrange: Create a new BBoxCalculator with a geodetic context.
        SpatialContext geoContext = SpatialContext.GEO;
        BBoxCalculator bboxCalculator = new BBoxCalculator(geoContext);

        // Act: Retrieve the initial min and max Y values.
        // No actions are performed on the calculator, so we are testing its initial state.
        double initialMinY = bboxCalculator.getMinY();
        double initialMaxY = bboxCalculator.getMaxY();

        // Assert: The initial Y bounds should be inverted infinities.
        // The minY value starts at the largest possible value and shrinks as points are added.
        assertEquals("Initial min Y should be positive infinity",
                Double.POSITIVE_INFINITY, initialMinY, 0.0);

        // The maxY value starts at the smallest possible value and grows as points are added.
        assertEquals("Initial max Y should be negative infinity",
                Double.NEGATIVE_INFINITY, initialMaxY, 0.0);
    }
}