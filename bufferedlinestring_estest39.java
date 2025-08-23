package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

// The test class name and inheritance are preserved as they are part of the existing test suite structure.
public class BufferedLineString_ESTestTest39 extends BufferedLineString_ESTest_scaffolding {

    /**
     * Tests that getBuf() returns the buffer value provided in the constructor,
     * specifically for an empty BufferedLineString (created with no points).
     */
    @Test
    public void getBuf_shouldReturnConstructorValue_forEmptyLineString() {
        // Arrange
        // An empty list of points results in an empty shape.
        List<Point> emptyPoints = Collections.emptyList();
        SpatialContext spatialContext = SpatialContext.GEO;
        double expectedBuffer = 10.0;

        // The 'expandBufForLongitudeSkew' flag is irrelevant for an empty line, but we pass a value.
        BufferedLineString lineString = new BufferedLineString(emptyPoints, expectedBuffer, true, spatialContext);

        // Act
        double actualBuffer = lineString.getBuf();

        // Assert
        // The buffer value should be preserved and returned by the getter.
        // A delta of 0.0 is used because we expect the exact value to be returned.
        assertEquals(expectedBuffer, actualBuffer, 0.0);
    }
}