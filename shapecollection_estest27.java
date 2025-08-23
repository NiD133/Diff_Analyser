package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link ShapeCollection} class.
 */
public class ShapeCollectionTest {

    /**
     * Tests that the equals() method is reflexive.
     * According to the contract of Object.equals(), an object must always be equal to itself.
     */
    @Test
    public void testEquals_isReflexive() {
        // Arrange: Create an empty ShapeCollection. The contents are not important
        // for a reflexivity check.
        List<Shape> shapes = new ArrayList<>();
        SpatialContext spatialContext = SpatialContext.GEO;
        ShapeCollection<Shape> shapeCollection = new ShapeCollection<>(shapes, spatialContext);

        // Act & Assert: An object must be equal to itself.
        assertTrue(
            "A ShapeCollection instance should be equal to itself.",
            shapeCollection.equals(shapeCollection)
        );
    }
}