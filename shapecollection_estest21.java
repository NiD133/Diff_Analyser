package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Note: The original test class name "ShapeCollection_ESTestTest21" and its
 * base class "ShapeCollection_ESTest_scaffolding" are artifacts of a test
 * generation tool (EvoSuite). A more conventional name would be "ShapeCollectionTest".
 */
public class ShapeCollection_ESTestTest21 extends ShapeCollection_ESTest_scaffolding {

    /**
     * Verifies that calling the get() method with a negative index throws an
     * ArrayIndexOutOfBoundsException, as expected from a List-based collection.
     */
    @Test(timeout = 4000, expected = ArrayIndexOutOfBoundsException.class)
    public void getWithNegativeIndexShouldThrowException() {
        // Arrange
        // The constructor requires a list that implements RandomAccess, like ArrayList.
        List<Shape> emptyShapes = new ArrayList<>();
        SpatialContext context = SpatialContext.GEO;
        ShapeCollection<Shape> shapeCollection = new ShapeCollection<>(emptyShapes, context);

        // Act & Assert
        // The test expects an ArrayIndexOutOfBoundsException, which is declared in the @Test annotation.
        shapeCollection.get(-1);
    }
}