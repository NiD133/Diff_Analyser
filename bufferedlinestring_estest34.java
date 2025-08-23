package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link BufferedLineString} class.
 */
public class BufferedLineStringTest {

    private final SpatialContext spatialContext = SpatialContext.GEO;

    /**
     * Tests the reflexive property of the equals() method.
     * An object must be equal to itself.
     */
    @Test
    public void equals_givenSameObjectInstance_shouldReturnTrue() {
        // Arrange
        final double bufferDistance = 10.0;
        final List<Point> emptyPoints = Collections.emptyList();
        final BufferedLineString lineString = new BufferedLineString(emptyPoints, bufferDistance, spatialContext);

        // Act & Assert
        // According to the Java contract for Object.equals(), an object must equal itself.
        // assertEquals(a, a) is an idiomatic way to test this property.
        assertEquals(lineString, lineString);

        // We can also add a sanity check to ensure the object was constructed as expected.
        assertEquals("Buffer distance should be correctly set", bufferDistance, lineString.getBuf(), 0.0);
    }
}