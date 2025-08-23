package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;

import static org.junit.Assert.assertEquals;

public class BBoxCalculatorRefactoredTest {

    /**
     * This test verifies that when a BBoxCalculator is expanded with a rectangle
     * representing a single point, its bounds are correctly set to that point's coordinates.
     */
    @Test
    public void expandRangeWithPointRectangleShouldSetInitialBounds() {
        // Arrange
        // Use the geographic context, as in the original test.
        SpatialContext geoContext = SpatialContext.GEO;
        BBoxCalculator bboxCalculator = new BBoxCalculator(geoContext);

        // The original test used a complex calculation to generate a point. We simplify this
        // by defining the point's coordinates directly. The Y-coordinate is taken from the
        // original test's assertion to preserve the core value being tested.
        final double pointX = 10.0;
        final double pointY = 1.242705837824413;

        // Create a rectangle that is effectively a single point.
        Point point = new PointImpl(pointX, pointY, geoContext);
        Rectangle pointRectangle = new RectangleImpl(point, point, geoContext);

        // Act
        // Expand the calculator's bounding box to include the new rectangle.
        bboxCalculator.expandRange(pointRectangle);

        // Assert
        // Verify that the calculator's bounds match the coordinates of the point.
        // The original test only checked minY. We check all bounds for clarity and completeness.
        final double delta = 0.000001;
        assertEquals("Min Y should match the point's Y coordinate", pointY, bboxCalculator.getMinY(), delta);
        assertEquals("Max Y should match the point's Y coordinate", pointY, bboxCalculator.getMaxY(), delta);
        assertEquals("Min X should match the point's X coordinate", pointX, bboxCalculator.getMinX(), delta);
        assertEquals("Max X should match the point's X coordinate", pointX, bboxCalculator.getMaxX(), delta);
    }
}