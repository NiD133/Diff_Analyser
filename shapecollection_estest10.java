package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.jts.JtsPoint;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for {@link ShapeCollection}.
 */
public class ShapeCollectionTest {

    /**
     * Tests that the ShapeCollection reflects modifications made to the
     * original list of shapes provided in its constructor.
     * This behavior is expected because the constructor is documented to
     * store the provided list by reference, not by making a defensive copy.
     */
    @Test
    public void getShapesShouldReflectChangesInOriginalList() {
        // Arrange
        // Create an empty list that will be passed to the ShapeCollection.
        List<JtsPoint> originalShapesList = new ArrayList<>();
        SpatialContext spatialContext = SpatialContext.GEO;

        // Create the ShapeCollection.
        ShapeCollection<JtsPoint> shapeCollection = new ShapeCollection<>(originalShapesList, spatialContext);

        // Act
        // Modify the original list *after* the ShapeCollection has been created.
        originalShapesList.add(null);

        // Retrieve the list of shapes from the collection.
        List<JtsPoint> shapesFromCollection = shapeCollection.getShapes();

        // Assert
        // The change to the original list should be visible through the ShapeCollection.
        assertFalse("Collection should not be empty after the original list was modified.", shapesFromCollection.isEmpty());
        assertEquals("Collection size should reflect the added element.", 1, shapesFromCollection.size());
    }
}