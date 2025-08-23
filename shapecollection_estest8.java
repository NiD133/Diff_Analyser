package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ShapeCollection} class.
 */
public class ShapeCollectionTest {

    @Test
    public void size_shouldReturnZero_whenCollectionIsEmpty() {
        // Arrange: Set up the test objects and preconditions.
        // Use a standard, readily available spatial context.
        SpatialContext spatialContext = SpatialContext.GEO;
        
        // Create an empty list of shapes for the collection.
        List<Shape> emptyShapes = Collections.emptyList();
        ShapeCollection<Shape> shapeCollection = new ShapeCollection<>(emptyShapes, spatialContext);

        // Act: Execute the method under test.
        int actualSize = shapeCollection.size();

        // Assert: Verify the outcome.
        int expectedSize = 0;
        assertEquals("The size of an empty ShapeCollection should be 0", expectedSize, actualSize);
    }
}