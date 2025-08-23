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

    /**
     * This test verifies that computing the bounding box of an empty collection of shapes
     * results in a Rectangle with NaN (Not-a-Number) coordinates. This is the expected
     * behavior for a bounding box with no points to define its boundaries.
     */
    @Test
    public void computeBoundingBox_forEmptyCollection_returnsNaNRectangle() {
        // ARRANGE
        // An empty list of shapes is the subject of our test.
        final List<Shape> emptyShapes = Collections.emptyList();
        final SpatialContext spatialContext = SpatialContext.GEO;

        // A ShapeCollection instance is needed to call its protected computeBoundingBox method.
        // The state of this particular instance is not relevant to the test itself.
        final ShapeCollection<Shape> shapeCollection = new ShapeCollection<>(emptyShapes, spatialContext);

        // ACT
        // Compute the bounding box for the empty collection.
        final Rectangle boundingBox = shapeCollection.computeBoundingBox(emptyShapes, spatialContext);

        // ASSERT
        // The bounding box of an empty collection should be a rectangle where all coordinates are NaN.
        final double delta = 0.0;
        assertEquals("MinX of an empty bounding box should be NaN", Double.NaN, boundingBox.getMinX(), delta);
        assertEquals("MinY of an empty bounding box should be NaN", Double.NaN, boundingBox.getMinY(), delta);
        assertEquals("MaxX of an empty bounding box should be NaN", Double.NaN, boundingBox.getMaxX(), delta);
        assertEquals("MaxY of an empty bounding box should be NaN", Double.NaN, boundingBox.getMaxY(), delta);
    }
}