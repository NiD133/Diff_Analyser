package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the BBoxCalculator class.
 */
// Renamed from BBoxCalculator_ESTestTest7 to follow standard conventions.
public class BBoxCalculatorTest {

    /**
     * Tests that calling expandRange on a new calculator correctly sets the
     * initial minimum and maximum Y boundaries.
     */
    // Test method name is now descriptive, explaining what is being tested.
    // The original name "test06" was uninformative.
    @Test
    public void expandRangeShouldCorrectlySetInitialYBounds() {
        // ARRANGE: Set up the test objects and initial state.
        // A default SpatialContext is sufficient for this test's purpose.
        SpatialContext ctx = SpatialContext.GEO;
        BBoxCalculator bboxCalculator = new BBoxCalculator(ctx);

        // ACT: Perform the action under test.
        // Expand the bounding box with a new range. The calculator starts with
        // minY=POSITIVE_INFINITY and maxY=NEGATIVE_INFINITY.
        bboxCalculator.expandRange(1.0, 1.0, -388.0, 0.0);

        // ASSERT: Verify the outcome.
        // Check that the Y boundaries have been updated to the values from the expanded range.
        assertEquals("The minimum Y should be updated to the new min Y.",
                -388.0, bboxCalculator.getMinY(), 0.01);
        assertEquals("The maximum Y should be updated to the new max Y.",
                0.0, bboxCalculator.getMaxY(), 0.01);
    }
}