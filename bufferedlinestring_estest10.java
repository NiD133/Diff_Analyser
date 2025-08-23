package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link BufferedLineString}.
 */
public class BufferedLineStringTest {

    @Test
    public void getBufShouldReturnBufferSizeProvidedInConstructor() {
        // Arrange
        // Use the standard GEO context, a common practice in Spatial4j.
        final SpatialContext spatialContext = SpatialContext.GEO;
        final double expectedBuffer = 0.0;
        final List<Point> emptyPoints = Collections.emptyList();

        // The constructor should correctly store the buffer size, even with no points.
        final BufferedLineString lineString = new BufferedLineString(emptyPoints, expectedBuffer, spatialContext);

        // Act
        final double actualBuffer = lineString.getBuf();

        // Assert
        assertEquals("The buffer size should match the value provided in the constructor.",
                expectedBuffer, actualBuffer, 0.0);
    }
}