package org.locationtech.spatial4j.io;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.ShapeCollection;

/**
 * Test suite for the WKTWriter class.
 */
public class WKTWriterTest {

  // Spatial context used for creating shapes and obtaining the WKT writer.
  private SpatialContext spatialContext;

  /**
   * Constructor that allows specifying a custom SpatialContext.
   * 
   * @param spatialContext the spatial context to use
   */
  protected WKTWriterTest(SpatialContext spatialContext) {
    this.spatialContext = spatialContext;
  }

  /**
   * Default constructor using the GEO spatial context.
   */
  public WKTWriterTest() {
    this(SpatialContext.GEO);
  }

  /**
   * Test the WKTWriter's ability to correctly handle an empty Point.
   */
  @Test
  public void testToStringOnEmptyPoint() throws Exception {
    // Obtain the WKT writer from the spatial context
    ShapeWriter wktWriter = spatialContext.getFormats().getWktWriter();
    
    // Create an empty Point (NaN coordinates)
    Point emptyPoint = spatialContext.makePoint(Double.NaN, Double.NaN);

    // Verify that the WKT representation of an empty Point is "POINT EMPTY"
    assertEquals("POINT EMPTY", wktWriter.toString(emptyPoint));
  }

  /**
   * Test the WKTWriter's ability to correctly handle an empty ShapeCollection.
   */
  @Test
  public void testToStringOnEmptyShapeCollection() throws Exception {
    // Obtain the WKT writer from the spatial context
    ShapeWriter wktWriter = spatialContext.getFormats().getWktWriter();
    
    // Create an empty ShapeCollection
    ShapeCollection<Point> emptyCollection = spatialContext.makeCollection(new ArrayList<>());

    // Verify that the WKT representation of an empty ShapeCollection is "GEOMETRYCOLLECTION EMPTY"
    assertEquals("GEOMETRYCOLLECTION EMPTY", wktWriter.toString(emptyCollection));
  }
}