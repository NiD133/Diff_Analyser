package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class ShapeCollection_ESTestTest15_Refactored extends ShapeCollection_ESTest_scaffolding {

    /**
     * This test verifies that getBoundingBox() returns a mutable internal Rectangle instance.
     * Modifying this returned instance affects the internal state of the ShapeCollection,
     * which is reflected in subsequent calls to getBoundingBox().
     *
     * This behavior can be unexpected and suggests that the class exposes its internal
     * representation. A more robust implementation might return a defensive copy.
     */
    @Test
    public void getBoundingBox_shouldReturnMutableRectangle_whoseChangesAreVisibleOnSubsequentCalls() {
        // Arrange: Create an empty ShapeCollection.
        List<Shape> emptyShapeList = new ArrayList<>();
        SpatialContext geoContext = SpatialContext.GEO;
        ShapeCollection<Shape> shapeCollection = new ShapeCollection<>(emptyShapeList, geoContext);

        // Act:
        // 1. Retrieve the bounding box for the first time.
        Rectangle firstBbox = shapeCollection.getBoundingBox();

        // 2. Directly modify the state of the retrieved bounding box object.
        //    This replaces the original test's obscure use of GeodesicSphereDistCalc
        //    to make the intent of the modification perfectly clear.
        double newMaxX = 10.0;
        double newMaxY = 20.0;
        firstBbox.reset(0, newMaxX, 0, newMaxY);

        // 3. Retrieve the bounding box again.
        Rectangle secondBbox = shapeCollection.getBoundingBox();

        // Assert:
        // Verify that the second call returns the *exact same* instance as the first.
        assertSame("Subsequent calls to getBoundingBox should return the same instance",
                firstBbox, secondBbox);

        // Verify that the changes made to the first retrieved instance are reflected.
        assertEquals("The modification to the bounding box's maxX was not reflected",
                newMaxX, secondBbox.getMaxX(), 0.0);
        assertEquals("The modification to the bounding box's maxY was not reflected",
                newMaxY, secondBbox.getMaxY(), 0.0);
    }
}