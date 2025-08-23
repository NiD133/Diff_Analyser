package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

// The original test class name and scaffolding are kept for context.
// In a real-world scenario, the class would be renamed to something like "ShapeCollectionTest".
public class ShapeCollection_ESTestTest12 extends ShapeCollection_ESTest_scaffolding {

    /**
     * Tests that the center of an empty ShapeCollection in a geographic context
     * defaults to the origin point (0, 0).
     */
    @Test
    public void getCenter_ofEmptyCollection_shouldReturnGeoOrigin() {
        // Arrange: Create an empty ShapeCollection using the standard geographic context.
        List<Shape> emptyShapeList = Collections.emptyList();
        SpatialContext geoContext = SpatialContext.GEO;
        ShapeCollection<Shape> emptyShapeCollection = new ShapeCollection<>(emptyShapeList, geoContext);

        // Act: Get the center of the empty collection.
        Point centerPoint = emptyShapeCollection.getCenter();

        // Assert: The center of an empty collection's bounding box is expected to be the origin.
        assertNotNull("The center point should not be null.", centerPoint);
        assertEquals("Center X-coordinate should be 0.0", 0.0, centerPoint.getX(), 0.0);
        assertEquals("Center Y-coordinate should be 0.0", 0.0, centerPoint.getY(), 0.0);
    }
}