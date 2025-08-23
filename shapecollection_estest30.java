package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.jts.JtsPoint;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link ShapeCollection} class.
 */
public class ShapeCollectionTest {

    /**
     * Tests that the area of a ShapeCollection is zero if it contains another
     * ShapeCollection that is itself empty. The total area of a collection
     * should be the sum of the areas of its member shapes.
     */
    @Test
    public void getArea_shouldReturnZero_whenCollectionContainsAnEmptyCollection() {
        // Arrange
        SpatialContext geoContext = SpatialContext.GEO;

        // Create an empty inner collection of shapes.
        ShapeCollection<JtsPoint> emptyInnerCollection = new ShapeCollection<>(new ArrayList<>(), geoContext);

        // Create a list containing only the empty inner collection.
        // Note: ShapeCollection requires a list that implements RandomAccess, like ArrayList.
        List<Shape> shapes = new ArrayList<>();
        shapes.add(emptyInnerCollection);

        // Create the outer collection to be tested.
        ShapeCollection<Shape> collectionOfCollections = new ShapeCollection<>(shapes, geoContext);

        // Act
        double actualArea = collectionOfCollections.getArea(geoContext);

        // Assert
        double expectedArea = 0.0;
        assertEquals(expectedArea, actualArea, 0.01);
    }
}