package org.locationtech.spatial4j.io;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Rectangle;
import org.locationtech.spatial4j.shape.Shape;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests the round-trip serialization and deserialization of shapes using {@link BinaryCodec}.
 * This test ensures that a shape can be written to a binary format and read back
 * into an identical object.
 */
public class BinaryCodecRoundTripTest extends BaseRoundTripTest<SpatialContext> {

    @Override
    public SpatialContext initContext() {
        return SpatialContext.GEO;
    }

    /**
     * Performs a round-trip test for a given shape:
     * 1. Serializes the shape to a byte array using {@link BinaryCodec}.
     * 2. Deserializes the shape from the byte array.
     * 3. Asserts that the deserialized shape is equal to the original.
     *
     * <p>Note: The {@code andEquals} parameter from the overridden method is unused here,
     * as equality is always asserted.
     */
    @Override
    protected void assertRoundTrip(Shape originalShape, @SuppressWarnings("unused") boolean andEquals) throws IOException {
        // Serialize the shape to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        binaryCodec.writeShape(new DataOutputStream(outputStream), originalShape);
        byte[] shapeBytes = outputStream.toByteArray();

        // Deserialize the shape from the byte array
        ByteArrayInputStream inputStream = new ByteArrayInputStream(shapeBytes);
        Shape deserializedShape = binaryCodec.readShape(new DataInputStream(inputStream));

        // Assert that the deserialized shape is equal to the original
        assertEquals(originalShape, deserializedShape);
    }

    @Test
    public void testRectangleRoundTrip() throws IOException {
        // ARRANGE: Create a test rectangle. Using the context's factory method makes the
        // dimensions (minX, maxX, minY, maxY) explicit and readable.
        // The original test used the WKT string: "ENVELOPE(-10, 180, 42.3, 0)"
        Rectangle originalRectangle = ctx.makeRectangle(-10, 180, 0, 42.3);

        // ACT & ASSERT: Perform the round-trip check.
        // This call now correctly invokes the overridden assertRoundTrip method in this class.
        assertRoundTrip(originalRectangle, true);
    }
}