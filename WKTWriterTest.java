package org.locationtech.spatial4j.io;

import org.junit.Before;
import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.ShapeCollection;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class WKTWriterTest {

    private SpatialContext spatialContext;
    private ShapeWriter wktWriter;

    @Before
    public void setUp() {
        spatialContext = SpatialContext.GEO;
        wktWriter = spatialContext.getFormats().getWktWriter();
    }

    @Test
    public void writePointWithNaNCoordinates_shouldReturnWktEmptyPoint() {
        Point emptyPoint = spatialContext.makePoint(Double.NaN, Double.NaN);
        
        String wkt = wktWriter.toString(emptyPoint);
        
        assertEquals("POINT EMPTY", wkt);
    }

    @Test
    public void writeEmptyShapeCollection_shouldReturnWktEmptyGeometryCollection() {
        ShapeCollection<Point> emptyCollection = spatialContext.makeCollection(Collections.emptyList());
        
        String wkt = wktWriter.toString(emptyCollection);
        
        assertEquals("GEOMETRYCOLLECTION EMPTY", wkt);
    }
}