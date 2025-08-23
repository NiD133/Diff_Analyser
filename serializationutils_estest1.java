package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;

/**
 * Unit tests for {@link SerializationUtils}.
 */
public class SerializationUtilsTest {

    /**
     * Tests that serializing a null object to a stream completes without error
     * and writes data to the stream.
     */
    @Test
    public void serialize_withNullObject_writesToStream() {
        // Arrange: Create an in-memory output stream to capture the serialized data.
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Act: Call the method under test.
        // According to the Javadoc, this should not throw an exception.
        SerializationUtils.serialize(null, outputStream);

        // Assert: Verify that the serialization process wrote data to the stream.
        // A serialized null is not an empty stream; it contains header data and a null marker.
        assertTrue("The output stream should contain data after serializing a null object.",
                outputStream.size() > 0);
    }
}