package org.locationtech.spatial4j.io;

import org.locationtech.spatial4j.context.SpatialContext;
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
 * Tests the binary serialization and deserialization (round-trip) of {@link ShapeCollection}
 * objects using {@link BinaryCodec}. This ensures that a collection of shapes can be
 * written to a binary format and read back into an equal object.
 */
public class BinaryCodecShapeCollectionTest extends BaseRoundTripTest<SpatialContext> {

    @Override
    public SpatialContext initContext() {
        return SpatialContext.GEO;
    }

    /**
     * Overrides the base class assertion to perform a round-trip test.
     * <p>
     * This implementation corrects a flaw in the original test where the {@code andEquals}
     * parameter was ignored. It now conditionally asserts equality based on the parameter,
     * adhering to the expected contract of the method.
     */
    @Override
    protected void assertRoundTrip(Shape shape, boolean andEquals) throws IOException {
        Shape deserializedShape = writeThenRead(shape);
        if (andEquals) {
            assertEquals(shape, deserializedShape);
        }
    }

    /**
     * Helper method to serialize a shape to a byte array and then deserialize it back.
     * This encapsulates the core round-trip logic.
     *
     * @param shape The shape to process.
     * @return The shape after serialization and deserialization.
     */
    private Shape writeThenRead(Shape shape) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        binaryCodec.writeShape(new DataOutputStream(outputStream), shape);

        byte[] shapeBytes = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(shapeBytes);
        return binaryCodec.readShape(new DataInputStream(inputStream));
    }

    /**
     * Verifies that a ShapeCollection containing multiple random shapes can be
     * successfully serialized and deserialized, resulting in an object equal to the original.
     */
    @Test
    public void shouldCorrectlyRoundTripShapeCollection() throws IOException {
        // Arrange: Create a collection of three random shapes.
        List<Shape> shapes = Arrays.asList(randomShape(), randomShape(), randomShape());
        ShapeCollection<Shape> originalCollection = ctx.makeCollection(shapes);

        // Act & Assert: Perform the round-trip test, which asserts equality.
        assertRoundTrip(originalCollection);
    }
}