package org.locationtech.spatial4j.shape.impl;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.SpatialRelation;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * This test class contains tests for BufferedLineString.
 * The original test was auto-generated and has been rewritten for clarity.
 */
public class BufferedLineString_ESTestTest41 {

    /**
     * Tests that relating an empty BufferedLineString to itself correctly returns DISJOINT.
     * An empty shape should have no spatial relationship with any other shape.
     */
    @Test
    public void relate_onEmptyLineString_shouldReturnDisjoint() {
        // ARRANGE
        // An empty BufferedLineString is created when an empty list of points is provided.
        final SpatialContext geoContext = SpatialContext.GEO;
        final double bufferDistance = 10.0; // A simple, clear buffer value.
        
        BufferedLineString emptyLineString = new BufferedLineString(
                Collections.<Point>emptyList(), bufferDistance, geoContext);

        // ACT
        // The spatial relationship of an empty shape to itself should be DISJOINT.
        SpatialRelation relation = emptyLineString.relate(emptyLineString);

        // ASSERT
        assertEquals("An empty shape should be disjoint from itself.", SpatialRelation.DISJOINT, relation);

        // Also, verify the buffer value remains unchanged as a sanity check.
        assertEquals("The buffer value should not change after a relate operation.",
                bufferDistance, emptyLineString.getBuf(), 0.0);
    }
}