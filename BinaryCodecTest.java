package org.locationtech.spatial4j.io;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.ShapeCollection;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BinaryCodecTest extends BaseRoundTripTest<SpatialContext> {

  private static final SpatialContext SPATIAL_CONTEXT = SpatialContext.GEO;

  @Override
  public SpatialContext initContext() {
    return SPATIAL_CONTEXT;
  }

  @Test
  public void testRectangleRoundTrip() throws IOException {
    // Given
    String wkt = "ENVELOPE(-10, 180, 42.3, 0)";
    Shape rectangle = wkt(wkt);

    // When
    assertRoundTrip(rectangle);

    // Then (assertRoundTrip handles the assertion)
  }

  @Test
  public void testCircleRoundTrip() throws IOException {
    // Given
    String wkt = "BUFFER(POINT(-10 30), 5.2)";
    Shape circle = wkt(wkt);

    // When
    assertRoundTrip(circle);

    // Then (assertRoundTrip handles the assertion)
  }

  @Test
  public void testShapeCollectionRoundTrip() throws IOException {
    // Given
    List<Shape> shapes = Arrays.asList(
        randomShape(),
        randomShape(),
        randomShape()
    );
    ShapeCollection<Shape> shapeCollection = ctx.makeCollection(shapes);

    // When
    assertRoundTrip(shapeCollection);

    // Then (assertRoundTrip handles the assertion)
  }

  @Override
  protected void assertRoundTrip(Shape originalShape, boolean andEquals) throws IOException {
    // Given
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

    // When
    binaryCodec.writeShape(dataOutputStream, originalShape);

    // Prepare for reading the shape back
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

    // Read the shape back
    Shape readShape = binaryCodec.readShape(dataInputStream);

    // Then
    assertEquals("The shape read back should be equal to the original shape", originalShape, readShape);
  }

}