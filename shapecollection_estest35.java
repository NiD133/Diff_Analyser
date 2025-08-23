package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.impl.PointImpl;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the ShapeCollection class.
 */
public class ShapeCollectionTest {

    /**
     * This test verifies that an empty ShapeCollection is disjoint from any other shape.
     * The 'relate' method should correctly identify that there is no spatial relationship
     * (i.e., no intersection, containment, etc.) when the collection contains no shapes.
     */
    @Test
    public void relate_shouldReturnDisjoint_whenCollectionIsEmpty() {
        // Arrange: Set up the test objects and state.
        // Use the standard geodetic context for simplicity and clarity.
        final SpatialContext spatialContext = SpatialContext.GEO;

        // Create an empty ShapeCollection.
        ShapeCollection<Shape> emptyShapeCollection = new ShapeCollection<>(Collections.emptyList(), spatialContext);

        // Create an arbitrary point to test against. Its specific location is irrelevant.
        Point anyPoint = new PointImpl(0, 0, spatialContext);

        // Act: Call the method under test.
        SpatialRelation relation = emptyShapeCollection.relate(anyPoint);

        // Assert: Verify the outcome.
        // An empty collection must be disjoint from any other shape.
        assertEquals("An empty collection should be disjoint from any shape", SpatialRelation.DISJOINT, relation);
    }
}