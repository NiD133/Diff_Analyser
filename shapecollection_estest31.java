package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link ShapeCollection} class.
 * This class contains the refactored version of an auto-generated test.
 */
public class ShapeCollection_ESTestTest31 {

    /**
     * Tests that {@link ShapeCollection#computeMutualDisjoint(List)} returns true
     * for an empty collection of shapes. An empty set of shapes is trivially disjoint.
     */
    @Test
    public void computeMutualDisjoint_withEmptyCollection_shouldReturnTrue() {
        // Arrange: Create an empty collection of shapes.
        // Note: The ShapeCollection constructor requires a list that implements RandomAccess.
        List<Shape> emptyShapes = new ArrayList<>();
        ShapeCollection<Shape> emptyCollection = new ShapeCollection<>(emptyShapes, SpatialContext.GEO);

        // Act: Call the method under test.
        boolean isDisjoint = ShapeCollection.computeMutualDisjoint(emptyCollection);

        // Assert: Verify that the result is true.
        assertTrue("An empty collection of shapes should be considered mutually disjoint.", isDisjoint);
    }
}