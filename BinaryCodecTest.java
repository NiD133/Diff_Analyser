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
 * Tests the BinaryCodec class to ensure shapes can be correctly serialized to binary format
 * and deserialized back to their original form.
 */
public class BinaryCodecTest extends BaseRoundTripTest<SpatialContext> {

  @Override
  public SpatialContext initContext() {
    return SpatialContext.GEO;
  }

  @Test
  public void testRectangleSerializationAndDeserialization() throws Exception {
    // Given: A rectangle with bounds from longitude -10 to 180 and latitude 0 to 42.3
    Shape rectangle = wkt("ENVELOPE(-10, 180, 42.3, 0)");
    
    // When/Then: The rectangle should be correctly serialized and deserialized
    assertRoundTrip(rectangle);
  }

  @Test
  public void testCircleSerializationAndDeserialization() throws Exception {
    // Given: A circle centered at (-10, 30) with radius 5.2
    Shape circle = wkt("BUFFER(POINT(-10 30), 5.2)");
    
    // When/Then: The circle should be correctly serialized and deserialized
    assertRoundTrip(circle);
  }

  @Test
  public void testShapeCollectionSerializationAndDeserialization() throws Exception {
    // Given: A collection containing three random shapes
    ShapeCollection<Shape> shapeCollection = ctx.makeCollection(
        Arrays.asList(
            randomShape(),
            randomShape(),
            randomShape()
        )
    );
    
    // When/Then: The collection should be correctly serialized and deserialized
    assertRoundTrip(shapeCollection);
  }

  /**
   * Verifies that a shape can be serialized to binary format and then deserialized
   * back to an equivalent shape.
   * 
   * @param shape The shape to test
   * @param andEquals Not used in this implementation (inherited parameter)
   * @throws IOException If an I/O error occurs during serialization/deserialization
   */
  @Override
  protected void assertRoundTrip(Shape shape, boolean andEquals) throws IOException {
    // Serialize the shape to binary format
    byte[] serializedShape = serializeShape(shape);
    
    // Deserialize the binary data back to a shape
    Shape deserializedShape = deserializeShape(serializedShape);
    
    // Verify the deserialized shape equals the original
    assertEquals("The deserialized shape should equal the original shape", 
                 shape, deserializedShape);
  }
  
  private byte[] serializeShape(Shape shape) throws IOException {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
    
    binaryCodec.writeShape(dataOutputStream, shape);
    
    return outputStream.toByteArray();
  }
  
  private Shape deserializeShape(byte[] serializedData) throws IOException {
    ByteArrayInputStream inputStream = new ByteArrayInputStream(serializedData);
    DataInputStream dataInputStream = new DataInputStream(inputStream);
    
    return binaryCodec.readShape(dataInputStream);
  }
}