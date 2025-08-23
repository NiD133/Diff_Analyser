package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A more focused and readable test for the ShapeCollection class.
 */
public class ShapeCollectionTest {

    /**
     * Verifies that computeBoundingBox throws a NullPointerException if the provided
     * collection of shapes contains a null element.
     */
    @Test(expected = NullPointerException.class)
    public void computeBoundingBox_withNullShapeInList_throwsNullPointerException() {
        // Arrange
        SpatialContext ctx = SpatialContext.GEO;
        List<Shape> shapesWithNull = Collections.singletonList(null);

        // The method under test, computeBoundingBox, is protected. We need an instance
        // of ShapeCollection to call it. The constructor requires a list that implements
        // RandomAccess, so we provide an empty ArrayList.
        ShapeCollection<Shape> shapeCollection = new ShapeCollection<>(new ArrayList<>(), ctx);

        // Act & Assert
        // This call is expected to throw a NullPointerException, which is handled by
        // the 'expected' parameter in the @Test annotation.
        shapeCollection.computeBoundingBox(shapesWithNull, ctx);
    }
}