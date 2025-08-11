/*******************************************************************************
 * Copyright (c) 2015 MITRE
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0 which
 * accompanies this distribution and is available at
 *    http://www.apache.org/licenses/LICENSE-2.0.txt
 ******************************************************************************/

package org.locationtech.spatial4j.io;

import org.junit.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.ShapeCollection;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * Verifies that BinaryCodec performs a lossless write/read round-trip for supported shapes.
 * Uses SpatialContext.GEO and asserts full structural equality after round-tripping.
 */
public class BinaryCodecTest extends BaseRoundTripTest<SpatialContext> {

  private static final String RECT_WKT = "ENVELOPE(-10, 180, 42.3, 0)";
  private static final String CIRCLE_WKT = "BUFFER(POINT(-10 30), 5.2)";

  @Override
  public SpatialContext initContext() {
    return SpatialContext.GEO;
  }

  @Test
  public void roundTrip_rectangle_isLossless() throws Exception {
    // Arrange
    Shape rectangle = wkt(RECT_WKT);

    // Act + Assert
    assertRoundTrip(rectangle);
  }

  @Test
  public void roundTrip_circle_isLossless() throws Exception {
    // Arrange
    Shape circle = wkt(CIRCLE_WKT);

    // Act + Assert
    assertRoundTrip(circle);
  }

  @Test
  public void roundTrip_shapeCollection_isLossless() throws Exception {
    // Arrange: build a small, diverse collection of random shapes
    ShapeCollection<Shape> collection = ctx.makeCollection(
        Arrays.asList(
            randomShape(),
            randomShape(),
            randomShape()
        )
    );

    // Act + Assert
    assertRoundTrip(collection);
  }

  /**
   * Writes the given shape via BinaryCodec and reads it back, asserting equality.
   * Note: We always assert full equality because the binary codec is expected to be lossless.
   */
  @Override
  protected void assertRoundTrip(Shape original, boolean andEquals) throws IOException {
    Shape roundTripped = roundTripBinary(original);
    assertEquals("Binary round-trip should preserve the shape", original, roundTripped);
  }

  private void assertRoundTrip(Shape original) throws IOException {
    assertRoundTrip(original, true);
  }

  /**
   * Serializes the provided shape with BinaryCodec and deserializes it back.
   */
  private Shape roundTripBinary(Shape original) throws IOException {
    // Serialize
    byte[] bytes;
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
         DataOutputStream out = new DataOutputStream(baos)) {
      binaryCodec.writeShape(out, original);
      out.flush();
      bytes = baos.toByteArray();
    }

    // Deserialize
    try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
         DataInputStream in = new DataInputStream(bais)) {
      return binaryCodec.readShape(in);
    }
  }
}