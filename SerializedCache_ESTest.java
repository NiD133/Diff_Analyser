package org.apache.ibatis.cache.decorators;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;

import static org.junit.Assert.*;

/**
 * Tests for the {@link SerializedCache} decorator.
 * This suite verifies that the cache correctly serializes and deserializes objects,
 * handles non-serializable objects gracefully, and delegates standard cache operations.
 */
public class SerializedCacheTest {

    private Cache delegate;
    private SerializedCache serializedCache;

    @Before
    public void setUp() {
        delegate = new PerpetualCache("test-cache");
        serializedCache = new SerializedCache(delegate);
    }

    // A simple serializable class for testing
    private static class Person implements Serializable {
        private static final long serialVersionUID = 1L;
        String name;

        Person(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            return name.equals(((Person) obj).name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    // A simple non-serializable class for testing
    private static class NonSerializableObject {
        String data = "some data";
    }

    @Test
    public void shouldStoreAndRetrieveSerializableObject() {
        // Arrange
        String key = "personKey";
        Person originalPerson = new Person("John Doe");

        // Act
        serializedCache.putObject(key, originalPerson);
        Object retrievedObject = serializedCache.getObject(key);

        // Assert
        assertNotNull("Retrieved object should not be null", retrievedObject);
        assertTrue("Retrieved object should be an instance of Person", retrievedObject instanceof Person);
        
        Person retrievedPerson = (Person) retrievedObject;
        assertEquals("Retrieved person should be equal to the original", originalPerson, retrievedPerson);
        assertNotSame("Retrieved person should be a different instance due to serialization", originalPerson, retrievedPerson);
    }

    @Test
    public void shouldReturnNullWhenGettingNonExistentObject() {
        // Arrange
        String key = "nonExistentKey";

        // Act
        Object retrievedObject = serializedCache.getObject(key);

        // Assert
        assertNull("Getting a non-existent key should return null", retrievedObject);
    }

    @Test
    public void shouldStoreAndRetrieveNullValue() {
        // Arrange
        String key = "nullValueKey";

        // Act
        serializedCache.putObject(key, null);
        Object retrievedObject = serializedCache.getObject(key);

        // Assert
        assertNull("A stored null value should be retrieved as null", retrievedObject);
    }

    @Test
    public void shouldRemoveObjectAndReturnIt() {
        // Arrange
        String key = "personKey";
        Person person = new Person("Jane Doe");
        serializedCache.putObject(key, person);
        assertEquals("Cache size should be 1 before removal", 1, serializedCache.getSize());

        // Act
        Object removedObject = serializedCache.removeObject(key);

        // Assert
        assertEquals("Removed object should be equal to the original", person, removedObject);
        assertNull("Object should be null after being removed", serializedCache.getObject(key));
        assertEquals("Cache size should be 0 after removal", 0, serializedCache.getSize());
    }

    @Test
    public void shouldReturnNullWhenRemovingNonExistentKey() {
        // Arrange
        String key = "nonExistentKey";

        // Act
        Object removedObject = serializedCache.removeObject(key);

        // Assert
        assertNull("Removing a non-existent key should return null", removedObject);
    }

    @Test(expected = CacheException.class)
    public void shouldThrowCacheExceptionWhenPuttingNonSerializableObject() {
        // Arrange
        String key = "nonSerializableKey";
        NonSerializableObject value = new NonSerializableObject();

        // Act
        serializedCache.putObject(key, value); // This should throw CacheException
    }

    @Test(expected = CacheException.class)
    public void getObject_whenDelegateContainsMalformedBytes_shouldThrowCacheException() {
        // Arrange
        // Simulate a corrupted cache entry with a byte array that is not a valid object stream.
        String key = "malformedBytesKey";
        byte[] malformedBytes = new byte[]{1, 2, 3, 4, 5};
        delegate.putObject(key, malformedBytes);

        // Act
        serializedCache.getObject(key); // Should throw CacheException during deserialization
    }

    @Test(expected = ClassCastException.class)
    public void getObject_whenDelegateContainsNonByteArray_shouldThrowClassCastException() {
        // Arrange
        // Simulate a corrupted cache state where a non-byte-array value was stored,
        // violating the contract of SerializedCache.
        String key = "corruptedKey";
        String notAByteArray = "I am a String, not a byte array";
        delegate.putObject(key, notAByteArray);

        // Act
        serializedCache.getObject(key); // Should throw ClassCastException when casting to byte[]
    }

    @Test
    public void shouldDelegateGetIdToWrappedCache() {
        // Arrange
        String expectedId = "test-cache";

        // Act
        String actualId = serializedCache.getId();

        // Assert
        assertEquals(expectedId, actualId);
    }

    @Test
    public void shouldDelegateGetSizeToWrappedCache() {
        // Arrange
        serializedCache.putObject("key1", "value1");
        serializedCache.putObject("key2", "value2");

        // Act & Assert
        assertEquals("Size should be delegated to the wrapped cache", 2, serializedCache.getSize());
        assertEquals("Delegate cache should also have size 2", 2, delegate.getSize());
    }

    @Test
    public void shouldDelegateClearToWrappedCache() {
        // Arrange
        serializedCache.putObject("key1", "value1");
        assertEquals(1, serializedCache.getSize());

        // Act
        serializedCache.clear();

        // Assert
        assertEquals("Cache size should be 0 after clear", 0, serializedCache.getSize());
        assertEquals("Delegate cache size should also be 0", 0, delegate.getSize());
    }

    @Test
    public void shouldDelegateEqualsAndHashCodeToWrappedCache() {
        // Arrange
        // SerializedCache's equals/hashCode delegates to the wrapped cache instance.
        Cache sameDelegate = delegate;
        Cache anotherDelegate = new PerpetualCache("another-cache");

        SerializedCache cacheWithSameDelegate = new SerializedCache(sameDelegate);
        SerializedCache cacheWithAnotherDelegate = new SerializedCache(anotherDelegate);

        // Assert
        assertTrue("SerializedCache should be equal to another wrapper with the same delegate",
            serializedCache.equals(cacheWithSameDelegate));
        assertFalse("SerializedCache should not be equal to a wrapper with a different delegate",
            serializedCache.equals(cacheWithAnotherDelegate));
        assertEquals("Hash code should be delegated to the wrapped cache",
            delegate.hashCode(), serializedCache.hashCode());
    }
}