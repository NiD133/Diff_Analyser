package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Rectangle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for BBoxCalculator.
 */
public class BBoxCalculatorTest {

    /**
     * Tests that expanding a BBoxCalculator with the world bounds in a geographic context
     * correctly results in a bounding box that wraps the world and has the correct
     * min/max latitude and longitude.
     */
    @Test(timeout = 4000)
    public void expandWithWorldBoundsShouldResultInWorldWrap() {
        // Arrange: Set up a geographic context, a BBoxCalculator, and the world bounds rectangle.
        final SpatialContext geoContext = SpatialContext.GEO;
        final BBoxCalculator bboxCalculator = new BBoxCalculator(geoContext);
        final Rectangle worldBounds = geoContext.getWorldBounds();

        // Act: Expand the calculator's range using the world bounds.
        bboxCalculator.expandRange(worldBounds);

        // Assert: Verify the calculator's state reflects the full world bounds.
        assertTrue("Calculator should report that it wraps the world on the X-axis.",
                bboxCalculator.doesXWorldWrap());

        // Verify all boundary coordinates are correct.
        assertEquals("Min Y should be the south pole.", -90.0, bboxCalculator.getMinY(), 0.0);
        assertEquals("Max Y should be the north pole.", 90.0, bboxCalculator.getMaxY(), 0.0);
        assertEquals("Min X should be the prime meridian.", -180.0, bboxCalculator.getMinX(), 0.0);
        assertEquals("Max X should be the prime meridian.", 180.0, bboxCalculator.getMaxX(), 0.0);
    }
}