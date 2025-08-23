package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class BinaryCodecReadableTest {

  private static final double EPS = 1e-9;

  private final SpatialContext ctx = SpatialContext.GEO;
  private final BinaryCodec codec = new BinaryCodec(ctx, new SpatialContextFactory());

  // Helpers

  private byte[] writeShapeToBytes(Shape s) throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (DataOutputStream out = new DataOutputStream(baos)) {
      codec.writeShape(out, s);
      out.flush();
    }
    return baos.toByteArray();
  }

  private Shape readShapeFromBytes(byte[] bytes) throws Exception {
    try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes))) {
      return codec.readShape(in);
    }
  }

  private void assertPointEquals(Point expected, Shape actual) {
    assertTrue("Expected a Point but got " + actual.getClass().getSimpleName(), actual instanceof Point);
    Point p = (Point) actual;
    assertEquals(expected.getX(), p.getX(), EPS);
    assertEquals(expected.getY(), p.getY(), EPS);
  }

  private void assertRectEquals(Rectangle expected, Shape actual) {
    assertTrue("Expected a Rectangle but got " + actual.getClass().getSimpleName(), actual instanceof Rectangle);
    Rectangle r = (Rectangle) actual;
    assertEquals(expected.getMinX(), r.getMinX(), EPS);
    assertEquals(expected.getMaxX(), r.getMaxX(), EPS);
    assertEquals(expected.getMinY(), r.getMinY(), EPS);
    assertEquals(expected.getMaxY(), r.getMaxY(), EPS);
  }

  private void assertCircleEquals(Circle expected, Shape actual) {
    assertTrue("Expected a Circle but got " + actual.getClass().getSimpleName(), actual instanceof Circle);
    Circle c = (Circle) actual;
    assertEquals(expected.getCenter().getX(), c.getCenter().getX(), EPS);
    assertEquals(expected.getCenter().getY(), c.getCenter().getY(), EPS);
    assertEquals(expected.getRadius(), c.getRadius(), EPS);
  }

  // Tests

  @Test
  public void point_roundTrip_preservesCoordinates() throws Exception {
    Point original = ctx.getShapeFactory().pointXY(-73.9857, 40.7484); // NYC
    byte[] bytes = writeShapeToBytes(original);
    Shape restored = readShapeFromBytes(bytes);

    assertPointEquals(original, restored);
  }

  @Test
  public void rectangle_roundTrip_preservesBounds() throws Exception {
    Rectangle original = ctx.getShapeFactory().rectXY(-10.0, 12.5, -5.0, 7.25);
    byte[] bytes = writeShapeToBytes(original);
    Shape restored = readShapeFromBytes(bytes);

    assertRectEquals(original, restored);
  }

  @Test
  public void circle_roundTrip_preservesCenterAndRadius() throws Exception {
    Point center = ctx.getShapeFactory().pointXY(2.0, 48.0); // near Paris
    Circle original = ctx.getShapeFactory().circle(center, 5.5); // radius in degrees for GEO
    byte[] bytes = writeShapeToBytes(original);
    Shape restored = readShapeFromBytes(bytes);

    assertCircleEquals(original, restored);
  }

  @Test
  public void collection_roundTrip_preservesOrderAndContent() throws Exception {
    Point p = ctx.getShapeFactory().pointXY(1.0, 2.0);
    Rectangle r = ctx.getShapeFactory().rectXY(-3.0, 3.0, -4.0, 4.0);
    Circle c = ctx.getShapeFactory().circle(ctx.getShapeFactory().pointXY(0.5, -0.5), 1.25);

    List<Shape> shapes = Arrays.asList(p, r, c);
    ShapeCollection<Shape> original = new ShapeCollection<>(shapes, ctx);

    byte[] bytes = writeShapeToBytes(original);
    Shape restored = readShapeFromBytes(bytes);

    assertTrue("Expected a ShapeCollection but got " + restored.getClass().getSimpleName(),
        restored instanceof ShapeCollection);
    @SuppressWarnings("unchecked")
    ShapeCollection<Shape> restoredColl = (ShapeCollection<Shape>) restored;

    assertEquals(shapes.size(), restoredColl.size());
    assertPointEquals((Point) shapes.get(0), restoredColl.getShapes().get(0));
    assertRectEquals((Rectangle) shapes.get(1), restoredColl.getShapes().get(1));
    assertCircleEquals((Circle) shapes.get(2), restoredColl.getShapes().get(2));
  }

  @Test
  public void multipleShapes_backToBack_canBeStreamedSequentially() throws Exception {
    Point p = ctx.getShapeFactory().pointXY(-120.0, 35.0);
    Rectangle r = ctx.getShapeFactory().rectXY(10.0, 20.0, -10.0, 0.0);

    // Write shapes one after the other to the same stream
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (DataOutputStream out = new DataOutputStream(baos)) {
      codec.writeShape(out, p);
      codec.writeShape(out, r);
      out.flush();
    }

    // Read them back in the same order
    try (DataInputStream in = new DataInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
      Shape first = codec.readShape(in);
      Shape second = codec.readShape(in);

      assertPointEquals(p, first);
      assertRectEquals(r, second);
    }
  }

  @Test
  public void collection_withEmptyList_roundTrip_works() throws Exception {
    ShapeCollection<Shape> original = new ShapeCollection<>(Arrays.asList(), ctx);
    byte[] bytes = writeShapeToBytes(original);
    Shape restored = readShapeFromBytes(bytes);

    assertTrue(restored instanceof ShapeCollection);
    @SuppressWarnings("unchecked")
    ShapeCollection<Shape> restoredColl = (ShapeCollection<Shape>) restored;
    assertEquals(0, restoredColl.size());
  }

  @Test(expected = NullPointerException.class)
  public void writeShape_null_throwsNpe() throws Exception {
    codec.writeShape(new DataOutputStream(new ByteArrayOutputStream()), null);
  }
}