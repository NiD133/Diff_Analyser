package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.jts.JtsPoint;

import java.util.ArrayList;
import java.util.List;

public class ShapeCollection_ESTestTest5 extends ShapeCollection_ESTest_scaffolding {

    /**
     * Tests that getBuffered() throws a NullPointerException if the underlying
     * list of shapes contains a null element. This can happen if the list is
     * modified after the ShapeCollection is created, as the collection holds a
     * reference to the original list.
     */
    @Test(expected = NullPointerException.class)
    public void getBufferedShouldThrowNPEWhenCollectionContainsNullShape() {
        // Arrange: Create a ShapeCollection with an empty list of shapes.
        // The collection maintains a reference to this list, not a defensive copy.
        List<JtsPoint> shapes = new ArrayList<>();
        SpatialContext spatialContext = SpatialContext.GEO;
        ShapeCollection<JtsPoint> shapeCollection = new ShapeCollection<>(shapes, spatialContext);

        // Mutate the list to an invalid state by adding a null element after construction.
        shapes.add(null);

        // Act & Assert: Calling getBuffered() should fail with an NPE because it will
        // attempt to process the null element in the list.
        shapeCollection.getBuffered(0.0, spatialContext);
    }
}