package org.locationtech.spatial4j.shape.impl;

import org.junit.Before;
import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.SpatialRelation;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link BufferedLineString}.
 */
public class BufferedLineStringTest {

    private SpatialContext geoContext;

    @Before
    public void setUp() {
        // The GEO context is a common choice for spatial operations.
        geoContext = SpatialContext.GEO;
    }

    /**
     * An empty BufferedLineString should be considered disjoint from any other shape,
     * as it represents nothing and occupies no space.
     */
    @Test
    public void relate_onEmptyLineString_shouldReturnDisjoint() {
        // Arrange: Create an empty BufferedLineString.
        // According to the source, a line string with no points is considered empty.
        List<Point> emptyPoints = Collections.emptyList();
        double buffer = 10.0; // The buffer value is arbitrary for this test.
        BufferedLineString emptyLineString = new BufferedLineString(emptyPoints, buffer, false, geoContext);

        // An arbitrary shape to test the relationship against.
        Rectangle otherShape = geoContext.makeRectangle(0, 10, 0, 10);

        // Pre-condition check: Ensure the line string is indeed empty.
        assertTrue("The BufferedLineString should be empty", emptyLineString.isEmpty());

        // Act: Determine the spatial relationship.
        SpatialRelation relation = emptyLineString.relate(otherShape);

        // Assert: The relationship should be DISJOINT.
        assertEquals("An empty shape should be disjoint from any other shape", SpatialRelation.DISJOINT, relation);
    }

    /**
     * The getBuf() method should return the buffer value provided during construction.
     */
    @Test
    public void getBuf_shouldReturnConstructorValue() {
        // Arrange
        double expectedBuffer = 25.5;
        // The points list cannot be empty for this test, as it might trigger different logic.
        // We use a single point, which the constructor handles as a valid case.
        List<Point> points = Collections.singletonList(geoContext.makePoint(0, 0));
        BufferedLineString lineString = new BufferedLineString(points, expectedBuffer, false, geoContext);

        // Act
        double actualBuffer = lineString.getBuf();

        // Assert
        assertEquals("getBuf() should return the value set in the constructor", expectedBuffer, actualBuffer, 0.0);
    }
}