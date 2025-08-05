/*******************************************************************************
 * Copyright (c) 2015 MITRE
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0 which
 * accompanies this distribution and is available at
 *    http://www.apache.org/licenses/LICENSE-2.0.txt
 ******************************************************************************/

package org.locationtech.spatial4j.io;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Circle;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.ShapeCollection;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Tests the round-trip serialization and deserialization of shapes
 * using the {@link BinaryCodec}.
 */
public class BinaryCodecTest extends BaseRoundTripTest<SpatialContext> {

  @Override
  public SpatialContext initContext() {
    return SpatialContext.GEO;
  }

  @Test
  public void testRectangleRoundTrip() throws IOException {
    // Given a Rectangle shape
    Rectangle originalRectangle = ctx.makeRectangle(-10, 180, 0, 42.3);

    // When it is serialized and deserialized
    // Then the resulting shape should be equal to the original
    assertRoundTrip(originalRectangle);
  }

  @Test
  public void testCircleRoundTrip() throws IOException {
    // Given a Circle shape
    Circle originalCircle = ctx.makeCircle(-10, 30, 5.2);

    // When it is serialized and deserialized
    // Then the resulting shape should be equal to the original
    assertRoundTrip(originalCircle);
  }

  @Test
  public void testShapeCollectionRoundTrip() throws IOException {
    // Given a collection of various shapes
    List<Shape> shapes = Arrays.asList(
        ctx.makePoint(0, 0),
        ctx.makeRectangle(10, 20, 30, 40),
        ctx.makeCircle(-1, -2, 3)
    );
    ShapeCollection<Shape> originalCollection = ctx.makeCollection(shapes);

    // When it is serialized and deserialized
    // Then the resulting shape should be equal to the original
    assertRoundTrip(originalCollection);
  }

  /**
   * Asserts that a given shape can be written to a binary format and then
   * read back to produce an equivalent shape. This is the "round trip".
   *
   * @param shape The shape to serialize and deserialize.
   * @param andEquals This parameter from the superclass is ignored, as we always assert for equality.
   * @throws IOException If an I/O error occurs during serialization or deserialization.
   */
  @Override
  protected void assertRoundTrip(Shape shape, boolean andEquals) throws IOException {
    // 1. Serialize the original shape to a byte array.
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    DataOutputStream dataOutput = new DataOutputStream(outputStream);
    binaryCodec.writeShape(dataOutput, shape);
    byte[] shapeBytes = outputStream.toByteArray();

    // 2. Deserialize the byte array back into a new shape object.
    ByteArrayInputStream inputStream = new ByteArrayInputStream(shapeBytes);
    DataInputStream dataInput = new DataInputStream(inputStream);
    Shape deserializedShape = binaryCodec.readShape(dataInput);

    // 3. Assert that the deserialized shape is equal to the original.
    assertEquals(shape, deserializedShape);
  }
}