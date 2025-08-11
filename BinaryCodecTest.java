/*******************************************************************************
 * Copyright (c) 2015 MITRE
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0 which
 * accompanies this distribution and is available at
 *    http://www.apache.org/licenses/LICENSE-2.0.txt
 ******************************************************************************/

package org.locationtech.spatial4j.io;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.ShapeCollection;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the BinaryCodec class, ensuring that shapes can be serialized and deserialized
 * correctly without loss of information.
 */
public class BinaryCodecTest extends BaseRoundTripTest<SpatialContext> {

  /**
   * Initializes the spatial context to use geographic coordinates.
   * 
   * @return The spatial context for geographic coordinates.
   */
  @Override
  public SpatialContext initContext() {
    return SpatialContext.GEO;
  }

  /**
   * Tests the serialization and deserialization of a rectangular shape.
   * 
   * @throws Exception If an error occurs during the test.
   */
  @Test
  public void testRectangleSerialization() throws Exception {
    Shape rectangle = wkt("ENVELOPE(-10, 180, 42.3, 0)");
    assertRoundTrip(rectangle);
  }

  /**
   * Tests the serialization and deserialization of a circular shape.
   * 
   * @throws Exception If an error occurs during the test.
   */
  @Test
  public void testCircleSerialization() throws Exception {
    Shape circle = wkt("BUFFER(POINT(-10 30), 5.2)");
    assertRoundTrip(circle);
  }

  /**
   * Tests the serialization and deserialization of a collection of shapes.
   * 
   * @throws Exception If an error occurs during the test.
   */
  @Test
  public void testShapeCollectionSerialization() throws Exception {
    ShapeCollection<Shape> shapeCollection = ctx.makeCollection(
        Arrays.asList(
            randomShape(),
            randomShape(),
            randomShape()
        )
    );
    assertRoundTrip(shapeCollection);
  }

  /**
   * Asserts that a shape can be serialized and then deserialized back to an equivalent shape.
   * 
   * @param shape The shape to test.
   * @param andEquals Unused parameter, kept for compatibility with the base class.
   * @throws IOException If an I/O error occurs during the test.
   */
  @Override
  protected void assertRoundTrip(Shape shape, boolean andEquals) throws IOException {
    // Serialize the shape to a byte array
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
    binaryCodec.writeShape(dataOutputStream, shape);

    // Deserialize the shape from the byte array
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
    Shape deserializedShape = binaryCodec.readShape(dataInputStream);

    // Verify that the original shape and the deserialized shape are equal
    assertEquals("The deserialized shape should be equal to the original shape.", shape, deserializedShape);
  }
}