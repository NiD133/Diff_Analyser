package org.apache.ibatis.cache;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.ibatis.cache.decorators.SerializedCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SerializedCache} to ensure it correctly handles object serialization.
 */
class SerializedCacheTest {

    private Cache serializedCache;

    @BeforeEach
    void setUp() {
        PerpetualCache delegate = new PerpetualCache("test-delegate");
        serializedCache = new SerializedCache(delegate);
    }

    /**
     * A simple test-only object that does not implement the Serializable interface.
     */
    private static class NonSerializableObject {
        private final int value;

        NonSerializableObject(int value) {
            this.value = value;
        }

        // equals() and hashCode() are not needed for this specific test
        // but are good practice for value objects.
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NonSerializableObject that = (NonSerializableObject) o;
            return value == that.value;
        }

        @Override
        public int hashCode() {
            return value;
        }
    }

    @Test
    void shouldThrowCacheExceptionWhenPuttingNonSerializableObject() {
        // Arrange
        Object key = 1;
        Object nonSerializableValue = new NonSerializableObject(123);

        // Act & Assert
        assertThrows(CacheException.class,
            () -> serializedCache.putObject(key, nonSerializableValue),
            "Should throw CacheException when the value is not serializable.");
    }
}