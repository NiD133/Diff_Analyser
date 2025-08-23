package org.apache.commons.lang3;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Contains tests for the {@link SerializationUtils} class.
 */
public class SerializationUtilsTest {

    /**
     * Tests that {@code SerializationUtils.deserialize(InputStream)} propagates an
     * {@link ArrayIndexOutOfBoundsException} when provided with a malformed InputStream.
     * <p>
     * This test verifies the behavior when the underlying stream is fundamentally broken.
     * The malformation is achieved by constructing a {@link ByteArrayInputStream} with
     * an invalid negative offset, which is designed to throw an exception upon access.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void deserializeFromStreamWithInvalidOffsetShouldThrowException() {
        // Arrange: Create a malformed input stream.

        // 1. Serialize a sample object to get a valid byte array.
        final Serializable objectToSerialize = "some test data";
        final byte[] serializedData = SerializationUtils.serialize(objectToSerialize);

        // 2. Intentionally create a ByteArrayInputStream with invalid parameters (a negative offset).
        // This setup ensures that any attempt to read from the stream will fail with an
        // ArrayIndexOutOfBoundsException from within the ByteArrayInputStream implementation itself.
        final int invalidOffset = -1;
        final int validLength = serializedData.length;
        final InputStream malformedInputStream = new ByteArrayInputStream(serializedData, invalidOffset, validLength);

        // Act: Attempt to deserialize from the malformed stream.
        // This call is expected to trigger the exception from the underlying stream.
        SerializationUtils.deserialize(malformedInputStream);

        // Assert: The test succeeds if an ArrayIndexOutOfBoundsException is thrown,
        // which is handled by the @Test(expected=...) annotation. An explicit fail() is not needed.
    }
}