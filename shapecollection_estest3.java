package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link ShapeCollection} class.
 */
public class ShapeCollectionTest {

    /**
     * Tests that {@link ShapeCollection#computeMutualDisjoint(List)} correctly identifies
     * a list containing only empty ShapeCollections as being mutually disjoint.
     * By definition, empty shapes cannot intersect with anything.
     */
    @Test
    public void computeMutualDisjoint_withListOfEmptyCollections_returnsTrue() {
        // Arrange: Set up the test data.
        SpatialContext spatialContext = SpatialContext.GEO;

        // Create two distinct but empty shape collections.
        ShapeCollection<Shape> emptyCollection1 = new ShapeCollection<>(new ArrayList<>(), spatialContext);
        ShapeCollection<Shape> emptyCollection2 = new ShapeCollection<>(new ArrayList<>(), spatialContext);

        // The method under test takes a list of shapes. A ShapeCollection is a Shape.
        List<Shape> listOfEmptyCollections = List.of(emptyCollection1, emptyCollection2);

        // Act: Call the static method being tested.
        boolean areDisjoint = ShapeCollection.computeMutualDisjoint(listOfEmptyCollections);

        // Assert: Verify the result.
        assertTrue("A list containing only empty collections should be considered mutually disjoint.", areDisjoint);
    }
}