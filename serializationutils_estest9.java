package org.apache.commons.lang3;

import org.junit.Test;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.HashMap;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link org.apache.commons.lang3.SerializationUtils}.
 */
public class SerializationUtilsTest {

    /**
     * A simple helper class that does not implement Serializable,
     * used to test serialization failure.
     */
    private static class NonSerializableObject {
        // This class is intentionally not serializable.
    }

    /**
     * Tests that serialize() throws a SerializationException when trying to serialize
     * an object graph that contains a non-serializable object.
     */
    @Test
    public void testSerializeThrowsExceptionForNonSerializableContent() {
        // Arrange: Create a serializable HashMap that contains a non-serializable object.
        final HashMap<String, Object> map = new HashMap<>();
        map.put("key", new NonSerializableObject());

        // Act & Assert
        try {
            SerializationUtils.serialize(map);
            fail("Expected a SerializationException to be thrown due to non-serializable content.");
        } catch (final SerializationException e) {
            // Verify that the underlying cause is the expected NotSerializableException.
            final Throwable cause = e.getCause();
            assertNotNull("The exception should have a cause.", cause);
            assertTrue("The cause should be a NotSerializableException.", cause instanceof NotSerializableException);
        }
    }
}