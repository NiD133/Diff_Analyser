package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertThrows;

/**
 * Unit tests for {@link BufferedLineString}.
 */
public class BufferedLineStringTest {

    /**
     * Verifies that the constructor throws a NullPointerException when the
     * provided SpatialContext is null. The SpatialContext is a required
     * dependency for creating shapes.
     */
    @Test
    public void constructorShouldThrowNullPointerExceptionForNullContext() {
        // Arrange: Define the arguments for the constructor, with a null context.
        List<Point> emptyPoints = Collections.emptyList();
        double bufferDistance = 10.0; // This value is arbitrary for this test.
        SpatialContext nullContext = null;

        // Act & Assert: Expect a NullPointerException when creating the BufferedLineString.
        assertThrows(NullPointerException.class, () -> {
            new BufferedLineString(emptyPoints, bufferDistance, false, nullContext);
        });
    }
}