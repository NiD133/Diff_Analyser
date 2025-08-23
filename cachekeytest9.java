package org.apache.ibatis.cache;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for CacheKey serialization behavior.
 */
@DisplayName("CacheKey Serialization")
class CacheKeyTest {

    /**
     * A CacheKey is Serializable, but its ability to be serialized depends on the
     * objects it contains. This test verifies that attempting to serialize a CacheKey
     * containing a non-serializable object correctly throws a NotSerializableException.
     */
    @Test
    @DisplayName("should throw NotSerializableException when an object in the key is not serializable")
    void shouldThrowExceptionWhenSerializingKeyWithNonSerializableObject() {
        // Arrange: Create a CacheKey and add a non-serializable object to it.
        // java.lang.Object itself does not implement the Serializable interface.
        CacheKey cacheKey = new CacheKey();
        cacheKey.update(new Object());

        // Act & Assert: Verify that attempting to serialize the cacheKey throws the expected exception.
        assertThrows(NotSerializableException.class,
            () -> serialize(cacheKey),
            "Serialization should fail if the CacheKey contains a non-serializable object.");
    }

    /**
     * Helper method to serialize and then deserialize an object.
     * This is a common pattern to verify an object's full serializability.
     *
     * @param <T>    the type of the object
     * @param object the object to serialize and deserialize
     * @return the deserialized object instance
     * @throws Exception if any serialization or deserialization error occurs
     */
    private static <T> T serialize(T object) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new ObjectOutputStream(baos).writeObject(object);

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        return (T) new ObjectInputStream(bais).readObject();
    }
}