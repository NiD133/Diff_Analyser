package org.apache.ibatis.cache.decorators;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;

import java.io.EOFException;
import java.io.SequenceInputStream;
import java.util.Enumeration;
import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.decorators.*;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.evosuite.runtime.mock.java.io.MockFileInputStream;

/**
 * Test suite for SerializedCache functionality.
 * SerializedCache is a cache decorator that serializes/deserializes objects
 * to ensure thread safety and object isolation.
 */
public class SerializedCacheTest {

    private Cache baseCache;
    private SerializedCache serializedCache;
    
    @Before
    public void setUp() {
        baseCache = new PerpetualCache("testCache");
        serializedCache = new SerializedCache(baseCache);
    }

    // ========== Basic Cache Operations ==========
    
    @Test
    public void shouldReturnCacheId() {
        // Given: A serialized cache with a known ID
        String expectedId = "testCache";
        
        // When: Getting the cache ID
        String actualId = serializedCache.getId();
        
        // Then: Should return the delegate cache's ID
        assertEquals(expectedId, actualId);
    }
    
    @Test
    public void shouldReturnZeroSizeForEmptyCache() {
        // When: Getting size of empty cache
        int size = serializedCache.getSize();
        
        // Then: Should return 0
        assertEquals(0, size);
    }
    
    @Test
    public void shouldReturnCorrectSizeAfterAddingItems() {
        // Given: A serializable object
        String key = "testKey";
        String value = "testValue";
        
        // When: Adding an item to cache
        serializedCache.putObject(key, value);
        
        // Then: Size should be 1
        assertEquals(1, serializedCache.getSize());
    }
    
    @Test
    public void shouldReturnNullForNonExistentKey() {
        // When: Getting object with non-existent key
        Object result = serializedCache.getObject("nonExistentKey");
        
        // Then: Should return null
        assertNull(result);
    }
    
    @Test
    public void shouldClearAllCacheEntries() {
        // Given: Cache with an item
        serializedCache.putObject("key", "value");
        
        // When: Clearing the cache
        serializedCache.clear();
        
        // Then: Cache should be empty and ID should remain
        assertEquals(0, serializedCache.getSize());
        assertEquals("testCache", serializedCache.getId());
    }

    // ========== Serialization Behavior ==========
    
    @Test
    public void shouldStoreAndRetrieveSerializableObject() {
        // Given: A serializable object
        String key = "stringKey";
        String originalValue = "serializable string";
        
        // When: Storing and retrieving the object
        serializedCache.putObject(key, originalValue);
        Object retrievedValue = serializedCache.getObject(key);
        
        // Then: Should get back an equivalent object (but different instance due to serialization)
        assertEquals(originalValue, retrievedValue);
        assertNotSame(originalValue, retrievedValue); // Different instances due to serialization
    }
    
    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenStoringNonSerializableObject() {
        // Given: A non-serializable object (PerpetualCache doesn't implement Serializable)
        PerpetualCache nonSerializableObject = new PerpetualCache("test");
        
        // When: Attempting to store non-serializable object
        // Then: Should throw RuntimeException
        serializedCache.putObject("key", nonSerializableObject);
    }
    
    @Test
    public void shouldHandleNullValues() {
        // Given: A null value
        String key = "nullKey";
        
        // When: Storing and retrieving null
        serializedCache.putObject(key, null);
        Object result = serializedCache.getObject(key);
        
        // Then: Should handle null correctly
        assertNull(result);
    }
    
    @Test
    public void shouldRemoveObjectAndReturnIt() {
        // Given: Cache with stored object
        String key = "removeKey";
        String value = "removeValue";
        baseCache.putObject(key, value); // Store directly in base cache
        
        // When: Removing the object
        Object removedValue = serializedCache.removeObject(key);
        
        // Then: Should return the removed value
        assertEquals(value, removedValue);
    }
    
    @Test
    public void shouldReturnNullWhenRemovingNonExistentObject() {
        // When: Removing non-existent object
        Object result = serializedCache.removeObject("nonExistentKey");
        
        // Then: Should return null
        assertNull(result);
    }

    // ========== Object Identity and Equality ==========
    
    @Test
    public void shouldDelegateHashCodeToUnderlyingCache() {
        // When: Getting hash code
        // Then: Should not throw exception for valid cache
        serializedCache.hashCode(); // Should complete without exception
    }
    
    @Test
    public void shouldDelegateEqualsToUnderlyingCache() {
        // Given: Another cache instance
        Cache anotherCache = new PerpetualCache("testCache");
        SerializedCache anotherSerializedCache = new SerializedCache(anotherCache);
        
        // When: Comparing caches
        boolean isEqual = serializedCache.equals(anotherSerializedCache);
        
        // Then: Should delegate to underlying cache equality
        assertTrue(isEqual);
    }
    
    @Test
    public void shouldNotEqualDifferentCacheTypes() {
        // Given: A different type of cache
        Cache differentCache = new PerpetualCache("differentCache");
        
        // When: Comparing with different cache
        boolean isEqual = serializedCache.equals(differentCache);
        
        // Then: Should not be equal
        assertFalse(isEqual);
    }

    // ========== Error Handling ==========
    
    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWithNullDelegate() {
        // Given: Null delegate cache
        SerializedCache nullDelegateCache = new SerializedCache(null);
        
        // When: Attempting to use cache operations
        // Then: Should throw NullPointerException
        nullDelegateCache.getId();
    }
    
    @Test(expected = RuntimeException.class)
    public void shouldThrowExceptionWhenCacheRequiresId() {
        // Given: Cache with null ID
        Cache nullIdCache = new PerpetualCache(null);
        SerializedCache cacheWithNullId = new SerializedCache(nullIdCache);
        
        // When: Attempting operations that require ID
        // Then: Should throw RuntimeException
        cacheWithNullId.hashCode();
    }
    
    @Test(expected = ClassCastException.class)
    public void shouldThrowExceptionWhenDeserializingInvalidData() {
        // Given: Cache with invalid serialized data (stored as String instead of byte[])
        baseCache.putObject("invalidKey", "notByteArray");
        
        // When: Attempting to retrieve (deserialize) invalid data
        // Then: Should throw ClassCastException
        serializedCache.getObject("invalidKey");
    }

    // ========== Integration with Other Cache Decorators ==========
    
    @Test
    public void shouldWorkWithSynchronizedCache() {
        // Given: SerializedCache wrapping SynchronizedCache
        Cache synchronizedCache = new SynchronizedCache(baseCache);
        SerializedCache wrappedCache = new SerializedCache(synchronizedCache);
        
        // When: Using the wrapped cache
        wrappedCache.putObject("syncKey", "syncValue");
        Object result = wrappedCache.getObject("syncKey");
        
        // Then: Should work correctly
        assertEquals("syncValue", result);
    }
    
    @Test(expected = IllegalStateException.class)
    public void shouldPropagateBlockingCacheExceptions() {
        // Given: SerializedCache wrapping BlockingCache
        Cache blockingCache = new BlockingCache(baseCache);
        SerializedCache wrappedCache = new SerializedCache(blockingCache);
        
        // When: Attempting to remove without acquiring lock
        // Then: Should propagate BlockingCache's IllegalStateException
        wrappedCache.removeObject("blockedKey");
    }

    // ========== Custom Object Input Stream Tests ==========
    
    @Test(expected = EOFException.class)
    public void customObjectInputStream_shouldThrowEOFExceptionOnEmptyStream() throws Exception {
        // Given: Empty enumeration for SequenceInputStream
        @SuppressWarnings("unchecked")
        Enumeration<MockFileInputStream> emptyEnumeration = mock(Enumeration.class);
        when(emptyEnumeration.hasMoreElements()).thenReturn(false);
        SequenceInputStream emptyStream = new SequenceInputStream(emptyEnumeration);
        
        // When: Creating CustomObjectInputStream with empty stream
        // Then: Should throw EOFException
        new SerializedCache.CustomObjectInputStream(emptyStream);
    }
}