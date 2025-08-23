package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link ShapeCollection} class.
 * This is a cleaned-up version of an auto-generated test.
 */
public class ShapeCollectionTest {

    @Test
    public void twoEmptyShapeCollectionsShouldBeEqual() {
        // Arrange
        // Use a standard spatial context for simplicity.
        SpatialContext spatialContext = SpatialContext.GEO;

        // Create two separate but identical empty collections.
        // The ShapeCollection constructor requires a list that implements RandomAccess, like ArrayList.
        ShapeCollection<Shape> collection1 = new ShapeCollection<>(new ArrayList<>(), spatialContext);
        ShapeCollection<Shape> collection2 = new ShapeCollection<>(new ArrayList<>(), spatialContext);

        // Assert
        // 1. Test for equality. assertEquals provides a more informative failure message than assertTrue.
        assertEquals(collection1, collection2);

        // 2. Per the equals/hashCode contract, equal objects must have equal hash codes.
        assertEquals(collection1.hashCode(), collection2.hashCode());
    }
}