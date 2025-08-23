package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.EOFException;
import org.junit.Test;

/**
 * Test suite for {@link SerializationUtils}.
 */
public class SerializationUtilsTest {

    /**
     * Tests that attempting to deserialize a byte array that is too short to be a
     * valid serialized stream throws a SerializationException. A valid Java
     * serialization stream starts with a 4-byte header, so a 1-byte array is
     * guaranteed to be invalid and cause a premature end-of-file.
     */
    @Test
    public void deserialize_givenTruncatedByteArray_throwsSerializationException() {
        // Arrange: Create a byte array that is too short to be a valid serialized object.
        final byte[] truncatedData = new byte[1];

        // Act & Assert
        try {
            SerializationUtils.deserialize(truncatedData);
            fail("Expected a SerializationException to be thrown for truncated data.");
        } catch (final SerializationException e) {
            // Verify that the exception has the expected underlying cause.
            // The ObjectInputStream should throw an EOFException when the stream ends
            // before the object is fully read.
            assertNotNull("The exception should have a cause.", e.getCause());
            assertEquals("The cause should be an EOFException.", EOFException.class, e.getCause().getClass());
        }
    }
}