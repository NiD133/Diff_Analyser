package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Rectangle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link BBoxCalculator} focusing on geodetic contexts.
 */
public class BBoxCalculatorTest {

    @Test
    public void yRangeShouldBePreservedWhenXRangeWrapsTheGlobe() {
        // ARRANGE
        // Use a geodetic context, where longitude wraps around the globe.
        SpatialContext geoContext = SpatialContext.GEO;
        BBoxCalculator calculator = new BBoxCalculator(geoContext);

        // Define a bounding box with a Y-range from -90 to -1.
        // The initial X-range (1.0 to 488.83) is wider than 360 degrees, which will
        // cause the bounding box to wrap the entire world horizontally.
        double expectedMinY = -90.0;
        double expectedMaxY = -1.0;

        // ACT
        // 1. Expand with a range that wraps the world on the X-axis.
        calculator.expandRange(1.0, 488.83, expectedMinY, expectedMaxY);

        // 2. Expand with another X-range that crosses the dateline.
        // This second expansion should be fully contained within the first world-wrapping range.
        calculator.expandXRange(1.0, -90.0);

        // 3. The getBoundary() method finalizes the min/max X values after all expansions.
        // This is a necessary step to trigger the internal calculation.
        Rectangle resultBoundary = calculator.getBoundary();

        // ASSERT
        // Verify that the Y-range was correctly established and maintained.
        assertEquals("minY should be the minimum of all expanded ranges.", expectedMinY, calculator.getMinY(), 0.0);
        assertEquals("maxY should be the maximum of all expanded ranges.", expectedMaxY, calculator.getMaxY(), 0.0);

        // Verify that the X-range has wrapped the globe, as expected.
        assertEquals("minX should be -180 due to world wrap.", -180.0, calculator.getMinX(), 0.0);
        assertEquals("maxX should be 180 due to world wrap.", 180.0, calculator.getMaxX(), 0.0);
        assertTrue("The resulting rectangle should span the full world width.", resultBoundary.getCrossesDateLine());

        // Also assert the properties of the returned Rectangle object for completeness.
        assertEquals(-180.0, resultBoundary.getMinX(), 0.0);
        assertEquals(180.0, resultBoundary.getMaxX(), 0.0);
        assertEquals(expectedMinY, resultBoundary.getMinY(), 0.0);
        assertEquals(expectedMaxY, resultBoundary.getMaxY(), 0.0);
    }
}