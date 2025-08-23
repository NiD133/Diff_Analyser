package org.locationtech.spatial4j.shape;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * This test suite contains tests for the {@link ShapeCollection} class.
 * The original test was auto-generated and tested an implementation side-effect.
 * This version has been refactored for clarity and to test the class's public contract.
 */
public class ShapeCollection_ESTestTest16 {

    /**
     * Verifies that the bounding box of an empty ShapeCollection is an "empty" rectangle.
     * An empty rectangle is defined as one with no area, represented by NaN coordinates.
     */
    @Test
    public void getBoundingBoxForEmptyCollectionShouldReturnEmptyRectangle() {
        // Arrange
        SpatialContext geoContext = SpatialContext.GEO;
        List<Shape> emptyShapeList = Collections.emptyList();
        ShapeCollection<Shape> emptyCollection = new ShapeCollection<>(emptyShapeList, geoContext);

        // Act
        Rectangle boundingBox = emptyCollection.getBoundingBox();

        // Assert
        // The bounding box of an empty collection should have no area and its coordinates should be NaN.
        assertFalse("Bounding box of an empty collection should not have area", boundingBox.hasArea());
        assertTrue("MinX should be NaN for an empty bounding box", Double.isNaN(boundingBox.getMinX()));
        assertTrue("MaxX should be NaN for an empty bounding box", Double.isNaN(boundingBox.getMaxX()));
        assertTrue("MinY should be NaN for an empty bounding box", Double.isNaN(boundingBox.getMinY()));
        assertTrue("MaxY should be NaN for an empty bounding box", Double.isNaN(boundingBox.getMaxY()));
    }
}