package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for {@link BufferedLineString}.
 * This specific test focuses on behavior when the line string is defined by a single point.
 */
// Note: The original class name 'BufferedLineString_ESTestTest12' and scaffolding are artifacts
// from a test generation tool (EvoSuite). In a real-world scenario, this would be part of a
// single, clean 'BufferedLineStringTest' class.
public class BufferedLineString_ESTestTest12 extends BufferedLineString_ESTest_scaffolding {

    /**
     * Tests that creating a BufferedLineString with a single point results
     * in a shape whose bounding box is not empty. Internally, a single-point
     * line string is treated as a buffered point (a circle), which has a valid area.
     */
    @Test
    public void getBoundingBox_withSinglePoint_returnsNonEmptyBox() {
        // Arrange
        final SpatialContext geoContext = SpatialContext.GEO;
        final double bufferDistance = 5.0; // 5 degrees

        // A BufferedLineString with only one point is a special case, treated as a buffered point.
        Point singlePoint = geoContext.makePoint(10, 20);
        List<Point> points = Collections.singletonList(singlePoint);

        // The 'expandBufForLongitudeSkew' parameter is not relevant for a single point,
        // but we set it to true to match the original test's scenario.
        BufferedLineString lineString = new BufferedLineString(points, bufferDistance, true, geoContext);

        // Act
        Rectangle boundingBox = lineString.getBoundingBox();

        // Assert
        assertNotNull("The bounding box should not be null.", boundingBox);
        assertFalse("The bounding box of a buffered single-point line string should not be empty.",
                boundingBox.isEmpty());
    }
}