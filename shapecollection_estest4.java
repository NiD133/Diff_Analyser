package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Test suite for the {@link ShapeCollection} class.
 */
public class ShapeCollectionTest {

    /**
     * Verifies that getBuffered() throws a NullPointerException when passed a null context.
     * The context is a required parameter for buffering operations, as it defines the
     * rules for creating the new buffered shape.
     */
    @Test(expected = NullPointerException.class)
    public void getBufferedShouldThrowExceptionForNullContext() {
        // Arrange: Create a ShapeCollection. Its contents are irrelevant for this test.
        SpatialContext initialContext = SpatialContext.GEO;
        List<Shape> emptyShapeList = new ArrayList<>();
        ShapeCollection<Shape> shapeCollection = new ShapeCollection<>(emptyShapeList, initialContext);
        
        // The specific distance value is arbitrary for this test.
        double bufferDistance = 1983.31;

        // Act: Call the method under test with a null context.
        // The test will pass if a NullPointerException is thrown, as specified by the
        // @Test(expected=...) annotation.
        shapeCollection.getBuffered(bufferDistance, null);
    }
}