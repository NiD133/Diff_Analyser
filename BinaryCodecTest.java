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

public class BinaryCodecTest extends BaseRoundTripTest<SpatialContext> {

    @Override
    public SpatialContext initContext() {
        return SpatialContext.GEO;
    }

    // Test Point serialization/deserialization
    @Test
    public void testPointRoundTrip() throws Exception {
        assertRoundTrip(wkt("POINT(-10 30)"));
    }

    // Test Rectangle serialization/deserialization
    @Test
    public void testRectangleRoundTrip() throws Exception {
        assertRoundTrip(wkt("ENVELOPE(-10, 180, 42.3, 0)"));
    }

    // Test Circle serialization/deserialization
    @Test
    public void testCircleRoundTrip() throws Exception {
        assertRoundTrip(wkt("BUFFER(POINT(-10 30), 5.2)"));
    }

    // Test ShapeCollection serialization/deserialization with deterministic shapes
    @Test
    public void testShapeCollectionRoundTrip() throws Exception {
        Shape point = wkt("POINT(0 0)");
        Shape rectangle = wkt("ENVELOPE(-10, 10, 5, -5)");
        Shape circle = wkt("BUFFER(POINT(1 2), 3)");
        
        ShapeCollection<Shape> collection = ctx.makeCollection(
            Arrays.asList(point, rectangle, circle)
        );
        assertRoundTrip(collection);
    }

    @Override
    protected void assertRoundTrip(Shape shape, boolean andEquals) throws IOException {
        // Serialize shape to binary
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        binaryCodec.writeShape(new DataOutputStream(byteOut), shape);
        
        // Deserialize binary back to shape
        ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
        Shape deserializedShape = binaryCodec.readShape(new DataInputStream(byteIn));
        
        // Verify round-tripped shape matches original
        assertEquals(shape, deserializedShape);
    }
}