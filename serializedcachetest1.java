package org.apache.ibatis.cache;

import org.apache.ibatis.cache.decorators.SerializedCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the SerializedCache decorator.
 * This suite verifies that the cache correctly serializes and deserializes objects,
 * handles non-serializable objects, and performs standard cache operations.
 */
class SerializedCacheTest {

    private Cache cache;

    @BeforeEach
    void setUp() {
        // Use a fresh cache for each test to ensure test isolation.
        PerpetualCache perpetualCache = new PerpetualCache("default-test-cache");
        cache = new SerializedCache(perpetualCache);
    }

    @Test
    void shouldStoreAndRetrieveSerializableObject() {
        // Arrange
        final int key = 1;
        final SerializableObject value = new SerializableObject(100);

        // Act
        cache.putObject(key, value);
        final SerializableObject retrievedValue = (SerializableObject) cache.getObject(key);

        // Assert
        assertEquals(value, retrievedValue, "Retrieved object should be equal to the original object.");
        assertNotSame(value, retrievedValue, "Retrieved object should be a new instance due to deserialization.");
    }

    @Test
    void shouldThrowExceptionWhenPuttingNonSerializableObject() {
        // Arrange
        final int key = 1;
        final NonSerializableObject value = new NonSerializableObject(200);

        // Act & Assert
        CacheException exception = assertThrows(CacheException.class,
            () -> cache.putObject(key, value),
            "Putting a non-serializable object should throw a CacheException.");

        assertTrue(exception.getMessage().contains("Error serializing object"),
            "Exception message should indicate a serialization error.");
    }

    @Test
    void shouldRemoveObjectFromCache() {
        // Arrange
        final int key = 1;
        final SerializableObject value = new SerializableObject(100);
        cache.putObject(key, value);
        assertNotNull(cache.getObject(key), "Object should be in the cache before removal.");

        // Act
        cache.removeObject(key);

        // Assert
        assertNull(cache.getObject(key), "Object should be null after being removed.");
    }

    @Test
    void shouldClearAllObjectsFromCache() {
        // Arrange
        cache.putObject(1, new SerializableObject(100));
        cache.putObject(2, new SerializableObject(200));
        assertEquals(2, cache.getSize(), "Cache size should be 2 before clearing.");

        // Act
        cache.clear();

        // Assert
        assertEquals(0, cache.getSize(), "Cache size should be 0 after clearing.");
        assertNull(cache.getObject(1), "Objects should be evicted after clearing.");
    }

    @Test
    void shouldHandleNullValues() {
        // Arrange
        final int key = 1;

        // Act
        cache.putObject(key, null);
        final Object retrievedValue = cache.getObject(key);

        // Assert
        assertNull(retrievedValue, "Cache should correctly store and retrieve null values.");
    }

    // --- Helper Classes ---

    /** A simple object that can be serialized, for testing purposes. */
    private static class SerializableObject implements Serializable {
        private static final long serialVersionUID = 1L;
        private final int value;

        public SerializableObject(int value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SerializableObject that = (SerializableObject) o;
            return value == that.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }

    /** A simple object that CANNOT be serialized, for testing error conditions. */
    private static class NonSerializableObject {
        private final int value;

        public NonSerializableObject(int value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NonSerializableObject that = (NonSerializableObject) o;
            return value == that.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value);
        }
    }
}