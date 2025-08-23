package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Rectangle;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for {@link BBoxCalculator}.
 */
public class BBoxCalculatorTest {

    /**
     * Tests that calling {@link BBoxCalculator#expandRange(double, double, double, double)}
     * with a {@link Double#NaN} for the minimum X value is handled gracefully.
     * The expected behavior is that the invalid part of the range (minX) is ignored,
     * and the bounding box is calculated using the valid coordinates. In this case,
     * the range becomes a point at (0,0).
     */
    @Test
    public void expandRangeWithNaNMinXShouldBeHandledGracefully() {
        // Arrange
        // Use a geographic context, as BBoxCalculator has special logic for it.
        SpatialContext geoContext = SpatialContext.GEO;
        BBoxCalculator bboxCalculator = new BBoxCalculator(geoContext);

        // Act
        // Expand the range with a NaN minX. The implementation should handle this without errors.
        bboxCalculator.expandRange(Double.NaN, 0.0, 0.0, 0.0);
        Rectangle resultBoundary = bboxCalculator.getBoundary();

        // Assert
        // The resulting boundary should be a point at (0,0), as the NaN value for minX
        // should be ignored, and the valid parts of the range (maxX=0, minY=0, maxY=0) are used.
        assertNotNull("Resultant boundary should not be null", resultBoundary);
        assertEquals("minX should be 0.0", 0.0, resultBoundary.getMinX(), 0.01);
        assertEquals("maxX should be 0.0", 0.0, resultBoundary.getMaxX(), 0.01);
        assertEquals("minY should be 0.0", 0.0, resultBoundary.getMinY(), 0.01);
        assertEquals("maxY should be 0.0", 0.0, resultBoundary.getMaxY(), 0.01);
    }
}