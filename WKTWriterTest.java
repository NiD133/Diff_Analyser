package org.locationtech.spatial4j.io;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.ShapeCollection;

/**
 * Tests for {@link WKTWriter} to ensure it correctly formats shapes into
 * Well-Known Text, particularly for empty geometries.
 */
public class WKTWriterTest {

  private WKTWriter writer;
  private SpatialContext ctx;

  @Before
  public void setUp() {
    ctx = SpatialContext.GEO;
    writer = (WKTWriter) ctx.getFormats().getWktWriter();
  }

  @Test
  public void toString_givenEmptyPoint_returnsPointEmptyWkt() {
    // Arrange
    Point emptyPoint = ctx.makePoint(Double.NaN, Double.NaN);
    String expectedWkt = "POINT EMPTY";

    // Act
    String actualWkt = writer.toString(emptyPoint);

    // Assert
    assertEquals(expectedWkt, actualWkt);
  }

  @Test
  public void toString_givenEmptyShapeCollection_returnsGeometryCollectionEmptyWkt() {
    // Arrange
    ShapeCollection<?> emptyCollection = ctx.makeCollection(Collections.emptyList());
    String expectedWkt = "GEOMETRYCOLLECTION EMPTY";

    // Act
    String actualWkt = writer.toString(emptyCollection);

    // Assert
    assertEquals(expectedWkt, actualWkt);
  }
}