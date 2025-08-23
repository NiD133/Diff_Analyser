package org.locationtech.spatial4j.io;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.ShapeCollection;

/**
 * Verifies how WKTWriter serializes empty shapes to WKT.
 * - A point with NaN coordinates is considered empty and should serialize as "POINT EMPTY".
 * - An empty shape collection should serialize as "GEOMETRYCOLLECTION EMPTY".
 */
public class WKTWriterTest {

  private SpatialContext ctx;
  private ShapeWriter wktWriter;

  @Before
  public void setUp() {
    // Use the default GEO context for consistent WKT formatting across tests
    ctx = SpatialContext.GEO;
    wktWriter = ctx.getFormats().getWktWriter();
  }

  private Point newEmptyPoint() {
    // In Spatial4j, a Point with NaN coordinates is considered "empty"
    return ctx.makePoint(Double.NaN, Double.NaN);
  }

  private ShapeCollection<Point> newEmptyGeometryCollection() {
    // An empty collection represents GEOMETRYCOLLECTION EMPTY in WKT
    return ctx.makeCollection(new ArrayList<Point>());
  }

  @Test
  public void emptyPoint_isSerializedAs_POINT_EMPTY() {
    // given
    Point emptyPoint = newEmptyPoint();

    // when
    String wkt = wktWriter.toString(emptyPoint);

    // then
    assertEquals("POINT EMPTY", wkt);
  }

  @Test
  public void emptyGeometryCollection_isSerializedAs_GEOMETRYCOLLECTION_EMPTY() {
    // given
    ShapeCollection<Point> emptyCollection = newEmptyGeometryCollection();

    // when
    String wkt = wktWriter.toString(emptyCollection);

    // then
    assertEquals("GEOMETRYCOLLECTION EMPTY", wkt);
  }
}