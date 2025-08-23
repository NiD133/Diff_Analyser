package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the ShapeCollection class.
 * This class focuses on improving the understandability of a specific test case.
 */
public class ShapeCollectionTest {

    /**
     * Tests that the toString() method correctly formats a collection with numerous elements.
     * The expected format is "ShapeCollection(elem1, elem2, ..., ...<size>)".
     */
    @Test
    public void toStringShouldListAllElementsAndAppendSizeForLargeCollection() {
        // ARRANGE
        // Define the number of elements to test with, making the intent clear.
        final int numberOfElements = 23;

        // Create a list containing the specified number of null shapes in a single, readable line.
        List<Shape> shapes = new ArrayList<>(Collections.nCopies(numberOfElements, null));
        
        SpatialContext spatialContext = SpatialContext.GEO;
        ShapeCollection<Shape> shapeCollection = new ShapeCollection<>(shapes, spatialContext);

        // ACT
        String actualToString = shapeCollection.toString();

        // ASSERT
        // Programmatically build the expected string to avoid a long, hardcoded literal.
        // This makes the assertion self-documenting and easier to maintain.
        String expectedElements = String.join(", ", Collections.nCopies(numberOfElements, "null"));
        String expectedToString = "ShapeCollection(" + expectedElements + " ..." + numberOfElements + ")";

        assertEquals(expectedToString, actualToString);
    }
}