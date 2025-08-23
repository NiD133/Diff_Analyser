package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotEquals;

/**
 * Test suite for the {@link ShapeCollection} class, focusing on understandability.
 */
public class ShapeCollectionTest {

    /**
     * Verifies that the equals() method returns false when a ShapeCollection
     * is compared to an object of an incompatible type.
     */
    @Test
    public void equals_shouldReturnFalse_whenComparedWithDifferentObjectType() {
        // Arrange
        // Use a standard spatial context, which is required by the ShapeCollection constructor.
        SpatialContext spatialContext = SpatialContext.GEO;

        // Create an empty ShapeCollection. The contents are not relevant for this specific test.
        List<Shape> emptyList = new ArrayList<>();
        ShapeCollection<Shape> shapeCollection = new ShapeCollection<>(emptyList, spatialContext);

        // Act & Assert
        // A ShapeCollection should not be equal to an object of a different class.
        // The original test compared it to the SpatialContext object, which serves as a good example
        // of an incompatible type.
        // Using assertNotEquals is more expressive than assertFalse(shapeCollection.equals(spatialContext)).
        assertNotEquals("A ShapeCollection should not be equal to an object of a different type.",
                shapeCollection, spatialContext);
    }
}