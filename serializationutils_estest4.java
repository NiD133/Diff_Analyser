package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link org.apache.commons.lang3.SerializationUtils}.
 */
public class SerializationUtilsTest {

    /**
     * Tests that serializing and then deserializing a null object results in a null object.
     * This is a common round-trip scenario.
     */
    @Test
    public void testSerializeAndDeserializeNull() {
        // Arrange: The object to be serialized is null.
        // No explicit object creation is needed.

        // Act: Serialize the null object and then deserialize the resulting byte array.
        final byte[] serializedData = SerializationUtils.serialize(null);
        final Object result = SerializationUtils.deserialize(serializedData);

        // Assert: The deserialized object should also be null.
        assertNull("Deserializing a serialized null object should result in null", result);
    }
}