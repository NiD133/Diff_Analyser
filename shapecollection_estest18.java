package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.jts.JtsPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test suite for {@link ShapeCollection}.
 * This class contains the refactored test from the original auto-generated test file.
 */
public class ShapeCollectionTest {

    /**
     * Tests that get(index) correctly retrieves an element from the underlying list,
     * even when the element is null.
     */
    @Test
    public void get_withValidIndex_shouldReturnCorrectElement() {
        // Arrange
        // Create a list containing 11 null elements to mirror the original test's setup.
        final int numberOfShapes = 11;
        List<JtsPoint> shapes = new ArrayList<>(Collections.nCopies(numberOfShapes, null));
        
        SpatialContext context = SpatialContext.GEO;
        ShapeCollection<JtsPoint> shapeCollection = new ShapeCollection<>(shapes, context);

        // A precondition check to ensure the collection was set up correctly.
        assertEquals("Precondition failed: Collection size should match the source list.",
                numberOfShapes, shapeCollection.size());

        // Act
        // Retrieve the element at the last valid index.
        int lastIndex = numberOfShapes - 1;
        Shape retrievedShape = shapeCollection.get(lastIndex);

        // Assert
        // The retrieved element should be null, as that's what was placed in the list.
        assertNull("The element retrieved from the last index should be null.", retrievedShape);
    }
}