package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

/**
 * Contains tests for the {@link SerializationUtils} class.
 * This version has been refactored for improved understandability.
 */
public class SerializationUtilsTest {

    /**
     * Tests that deserializing a stream, which was created by serializing a null object,
     * correctly returns null. This verifies the round-trip serialization of null values.
     */
    @Test
    public void deserialize_givenStreamOfSerializedNull_returnsNull() {
        // Arrange: Serialize a null object to create the input for deserialization.
        // The cast to Serializable is technically not needed for a null value,
        // but it clarifies that we are intentionally passing a serializable null.
        final byte[] serializedNull = SerializationUtils.serialize((Serializable) null);
        final InputStream inputStream = new ByteArrayInputStream(serializedNull);

        // Act: Deserialize the byte stream back into an object.
        final Object deserializedObject = SerializationUtils.deserialize(inputStream);

        // Assert: The resulting object should be null, completing the round-trip.
        assertNull("Deserializing a serialized null should result in null.", deserializedObject);
    }
}