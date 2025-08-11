package org.locationtech.spatial4j.io;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.ShapeCollection;

/**
 * Tests for WKTWriter functionality, focusing on proper WKT (Well-Known Text) format output
 * for various geometric shapes, particularly edge cases like empty geometries.
 */
public class WKTWriterTest {

    private final SpatialContext spatialContext;

    /**
     * Constructor for testing with a custom spatial context.
     * @param spatialContext the spatial context to use for creating shapes and writers
     */
    protected WKTWriterTest(SpatialContext spatialContext) {
        this.spatialContext = spatialContext;
    }

    /**
     * Default constructor using the standard geographic spatial context.
     */
    public WKTWriterTest() {
        this(SpatialContext.GEO);
    }

    /**
     * Verifies that an empty point (created with NaN coordinates) is correctly
     * formatted as "POINT EMPTY" in WKT format.
     */
    @Test
    public void testEmptyPointFormattedAsWktEmpty() throws Exception {
        // Given: A WKT writer and an empty point with NaN coordinates
        ShapeWriter wktWriter = spatialContext.getFormats().getWktWriter();
        Point emptyPoint = spatialContext.makePoint(Double.NaN, Double.NaN);

        // When: Converting the empty point to WKT string format
        String wktOutput = wktWriter.toString(emptyPoint);

        // Then: The output should be the standard WKT representation for empty points
        assertEquals("POINT EMPTY", wktOutput);
    }

    /**
     * Verifies that an empty shape collection is correctly formatted as 
     * "GEOMETRYCOLLECTION EMPTY" in WKT format.
     */
    @Test
    public void testEmptyShapeCollectionFormattedAsWktEmpty() throws Exception {
        // Given: A WKT writer and an empty shape collection
        ShapeWriter wktWriter = spatialContext.getFormats().getWktWriter();
        ShapeCollection<Point> emptyShapeCollection = spatialContext.makeCollection(new ArrayList<>());

        // When: Converting the empty collection to WKT string format
        String wktOutput = wktWriter.toString(emptyShapeCollection);

        // Then: The output should be the standard WKT representation for empty geometry collections
        assertEquals("GEOMETRYCOLLECTION EMPTY", wktOutput);
    }
}