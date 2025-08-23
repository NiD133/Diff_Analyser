package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link ShapeCollection} class.
 */
public class ShapeCollectionTest {

    private final SpatialContext spatialContext = SpatialContext.GEO;

    /**
     * Tests that buffering a ShapeCollection which contains a single, empty
     * ShapeCollection results in a new collection with exactly one element.
     * This element represents the buffered (but still empty) geometry of the inner collection.
     */
    @Test
    public void getBufferedOnCollectionContainingOneEmptyCollectionReturnsCollectionWithOneElement() {
        // Arrange
        // 1. Create an empty ShapeCollection.
        ShapeCollection<Shape> emptyCollection = new ShapeCollection<>(Collections.emptyList(), spatialContext);

        // 2. Create a parent collection that contains only the empty collection.
        List<Shape> shapes = Collections.singletonList(emptyCollection);
        ShapeCollection<Shape> collectionContainingEmpty = new ShapeCollection<>(shapes, spatialContext);

        double bufferDistance = 10.0; // An arbitrary distance for the buffer operation.

        // Act
        // Buffer the parent collection. The implementation should iterate through its
        // contents, buffer each one, and create a new collection of the results.
        ShapeCollection bufferedResult = collectionContainingEmpty.getBuffered(bufferDistance, spatialContext);

        // Assert
        // The resulting collection should contain a single shape, which is the result
        // of buffering the inner empty collection.
        assertEquals("The buffered collection should contain one element", 1, bufferedResult.size());
    }
}