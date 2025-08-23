package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

// The original class name is kept for context, though in a real scenario,
// it would be renamed to something like ShapeCollectionTest.
public class ShapeCollection_ESTestTest36 extends ShapeCollection_ESTest_scaffolding {

    /**
     * Verifies that buffering an empty ShapeCollection results in another empty ShapeCollection.
     * This ensures the buffering operation correctly handles the edge case of an empty input.
     */
    @Test
    public void getBuffered_withEmptyCollection_returnsEmptyCollection() {
        // Arrange: Create an empty ShapeCollection using a standard geographic context.
        SpatialContext spatialContext = SpatialContext.GEO;
        List<Shape> emptyList = Collections.emptyList();
        ShapeCollection<Shape> emptyCollection = new ShapeCollection<>(emptyList, spatialContext);

        // Act: Apply a buffer of 0.0 to the empty collection. The distance value
        // should not affect the outcome for an empty collection.
        Shape bufferedResult = emptyCollection.getBuffered(0.0, spatialContext);

        // Assert: The result should be a non-null, empty ShapeCollection.
        assertNotNull("The buffered result should not be null.", bufferedResult);
        assertTrue("The result should be an instance of ShapeCollection.",
                bufferedResult instanceof ShapeCollection);

        ShapeCollection<?> bufferedCollection = (ShapeCollection<?>) bufferedResult;
        assertEquals("Buffering an empty collection should produce an empty collection.",
                0, bufferedCollection.size());
    }
}