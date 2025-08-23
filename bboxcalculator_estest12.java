package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

/**
 * Unit tests for {@link BBoxCalculator}.
 */
public class BBoxCalculatorTest {

    /**
     * Tests that calling getBoundary() on a BBoxCalculator initialized with a null
     * SpatialContext throws a NullPointerException. The context is required internally
     * to construct the final Rectangle object.
     */
    @Test(expected = NullPointerException.class)
    public void getBoundary_whenContextIsNull_throwsNullPointerException() {
        // Arrange: Create a BBoxCalculator with a null SpatialContext.
        BBoxCalculator calculator = new BBoxCalculator((SpatialContext) null);

        // Act: Attempt to get the boundary. This is expected to fail.
        calculator.getBoundary();

        // Assert: The @Test(expected) annotation verifies that a NullPointerException was thrown.
    }
}