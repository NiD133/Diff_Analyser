package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains tests for the ShapeCollection class, focusing on method contracts and
 * exception handling.
 */
public class ShapeCollectionTest {

    /**
     * Verifies that calling get() on an empty ShapeCollection with any index
     * throws an IndexOutOfBoundsException.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void getShouldThrowIndexOutOfBoundsExceptionForInvalidIndex() {
        // Arrange: Create an empty ShapeCollection.
        // The collection is backed by an empty list, so any index will be out of bounds.
        List<Shape> emptyShapeList = new ArrayList<>();
        SpatialContext spatialContext = SpatialContext.GEO;
        ShapeCollection<Shape> emptyCollection = new ShapeCollection<>(emptyShapeList, spatialContext);

        // Act: Attempt to access an element at an index that does not exist.
        // The original test used a large arbitrary index (1341). Using 0 is
        // sufficient and clearer for demonstrating the error on an empty collection.
        emptyCollection.get(0);

        // Assert: The test expects an IndexOutOfBoundsException, which is declared
        // in the @Test annotation. The test will pass if this exception is thrown
        // and fail otherwise.
    }
}