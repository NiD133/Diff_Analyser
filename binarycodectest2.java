package org.locationtech.spatial4j.io;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Shape;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests the round-trip serialization and deserialization of Shapes using {@link BinaryCodec}.
 * It ensures that a shape, once written to a binary format and read back, is
 * identical to the original.
 */
public class BinaryCodecRoundTripTest extends BaseRoundTripTest<SpatialContext> {

    @Override
    public SpatialContext initContext() {
        return SpatialContext.GEO;
    }

    /**
     * Asserts that a given shape can be serialized and then deserialized back into an
     * equal object.
     * <p>
     * This method overrides a base class method. The {@code andEquals} parameter is part of the
     * base method's signature but is not used here, as this test's purpose is always to
     * check for equality.
     *
     * @param originalShape the shape to serialize and deserialize.
     * @param andEquals     (from base class, unused) flag to determine if equality should be checked.
     * @throws IOException if an I/O error occurs during serialization or deserialization.
     */
    @Override
    protected void assertRoundTrip(Shape originalShape, boolean andEquals) throws IOException {
        // 1. Serialize (write) the shape to a byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        binaryCodec.writeShape(new DataOutputStream(outputStream), originalShape);
        byte[] shapeBytes = outputStream.toByteArray();

        // 2. Deserialize (read) the shape from the byte array
        ByteArrayInputStream inputStream = new ByteArrayInputStream(shapeBytes);
        Shape deserializedShape = binaryCodec.readShape(new DataInputStream(inputStream));

        // 3. Assert that the deserialized shape is equal to the original
        assertEquals(originalShape, deserializedShape);
    }

    @Test
    public void whenCircleIsSerialized_thenItCanBeDeserializedBackToEqualObject() throws IOException {
        // Arrange: Create a Circle shape. A circle is defined by a center point (lon, lat)
        // and a radius in degrees.
        double lon = -10;
        double lat = 30;
        double radius = 5.2;
        Shape originalCircle = ctx.makeCircle(lon, lat, radius);

        // Act & Assert: Perform the round-trip serialization/deserialization and check for equality.
        assertRoundTrip(originalCircle);
    }
}