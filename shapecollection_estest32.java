package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.jts.JtsPoint;

import java.util.ArrayList;
import java.util.List;

public class ShapeCollection_ESTestTest32 extends ShapeCollection_ESTest_scaffolding {

    /**
     * Tests that the relate() method throws a NullPointerException if the underlying
     * list of shapes is modified to contain a null element after the collection's
     * construction.
     */
    @Test(expected = NullPointerException.class, timeout = 4000)
    public void relateShouldThrowNullPointerExceptionWhenShapeListContainsNull() {
        // Arrange
        SpatialContext ctx = SpatialContext.GEO;

        // The ShapeCollection constructor takes a list by reference. We create an empty
        // list first and then add a null to it after the collection is created.
        List<JtsPoint> shapes = new ArrayList<>();
        ShapeCollection<JtsPoint> shapeCollection = new ShapeCollection<>(shapes, ctx);
        shapes.add(null); // Mutate the list post-construction to introduce the null.

        // Create a simple shape to check the relation against.
        Rectangle otherShape = ctx.makeRectangle(0, 1, 0, 1);

        // Act & Assert
        // The relate() method iterates over the internal list. When it encounters the
        // null element, it will throw a NullPointerException.
        shapeCollection.relate(otherShape);
    }
}