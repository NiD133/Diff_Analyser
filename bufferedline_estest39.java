package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import static org.junit.Assert.assertEquals;

/**
 * Tests the equality contract (equals and hashCode) of the {@link BufferedLine} class.
 */
public class BufferedLineEqualsTest {

    // A simple Cartesian spatial context is used because BufferedLine operates in Euclidean space.
    private final SpatialContext spatialContext = new SpatialContext(false);

    @Test
    public void equals_shouldReturnTrue_forTwoIdenticalBufferedLines() {
        // Arrange
        // According to the source code, two BufferedLine objects are equal if they have the
        // same start point, end point, and buffer distance.
        // We will create two identical lines to verify this behavior.
        Point startAndEndPoint = spatialContext.makePoint(0.0, 0.0);
        double buffer = 0.0;

        BufferedLine lineA = new BufferedLine(startAndEndPoint, startAndEndPoint, buffer, spatialContext);
        BufferedLine lineB = new BufferedLine(startAndEndPoint, startAndEndPoint, buffer, spatialContext);

        // Act & Assert
        // 1. Verify that the two identical line objects are considered equal.
        assertEquals(lineA, lineB);

        // 2. As per the Java contract, if two objects are equal, their hash codes must also be equal.
        assertEquals("Equal objects must have equal hash codes.", lineA.hashCode(), lineB.hashCode());
    }
}