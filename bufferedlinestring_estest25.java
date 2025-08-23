package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import java.util.Collections;
import java.util.List;

/**
 * Unit tests for {@link BufferedLineString}.
 */
public class BufferedLineStringTest {

    /**
     * Tests that the BufferedLineString constructor throws a NullPointerException
     * if the provided SpatialContext is null, specifically when the list of points is empty.
     * This scenario triggers a call to `ctx.makeCollection()`, which fails on a null context.
     */
    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNPEForNullContextWhenPointListIsEmpty() {
        // Arrange
        List<Point> emptyPoints = Collections.emptyList();
        double buffer = 10.0; // The buffer value is arbitrary for this test.
        SpatialContext nullContext = null;

        // Act & Assert
        // This should throw a NullPointerException because the context is used internally.
        new BufferedLineString(emptyPoints, buffer, nullContext);
    }
}