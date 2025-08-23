package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.ShapeCollection;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link WKTWriter}.
 */
public class WKTWriterTest {

    @Test
    public void toString_withEmptyShapeCollection_returnsGeometryCollectionEmpty() {
        // Arrange
        WKTWriter wktWriter = new WKTWriter();
        
        // Create an empty ShapeCollection, which is the subject of our test
        ShapeCollection<Shape> emptyShapeCollection = new ShapeCollection<>(
                Collections.emptyList(),
                SpatialContext.GEO
        );

        String expectedWkt = "GEOMETRYCOLLECTION EMPTY";

        // Act
        String actualWkt = wktWriter.toString(emptyShapeCollection);

        // Assert
        assertEquals(expectedWkt, actualWkt);
    }
}