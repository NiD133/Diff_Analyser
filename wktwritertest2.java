package org.locationtech.spatial4j.io;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.ShapeCollection;

/**
 * Tests for the {@link WKTWriter} class.
 */
public class WKTWriterTest {

    // Initialize the context and writer as final fields.
    // SpatialContext.GEO is the standard singleton for geographic operations.
    // This fixes the NullPointerException from the original code.
    private final SpatialContext ctx = SpatialContext.GEO;
    private final ShapeWriter writer = ctx.getFormats().getWktWriter();

    @Test
    public void toString_shouldReturnGeometryCollectionEmpty_forEmptyShapeCollection() {
        // Arrange: Create an empty ShapeCollection.
        // Using Collections.emptyList() is a clear and standard way to represent an empty collection.
        ShapeCollection<?> emptyCollection = ctx.makeCollection(Collections.emptyList());
        String expectedWkt = "GEOMETRYCOLLECTION EMPTY";

        // Act: Convert the empty collection to its WKT string representation.
        String actualWkt = writer.toString(emptyCollection);

        // Assert: Verify the output matches the expected WKT format for an empty collection.
        assertEquals(expectedWkt, actualWkt);
    }
}