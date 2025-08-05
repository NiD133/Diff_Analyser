/*
 *    Copyright 2009-2024 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.apache.ibatis.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import org.junit.jupiter.api.Test;

/**
 * Tests for CacheKey equality, hashing, serialization, and special behaviors.
 * CacheKey is used in MyBatis to uniquely identify cached query results.
 */
class CacheKeyTest {

    // ========== EQUALITY TESTS ==========

    @Test
    void cacheKeysWithIdenticalContent_shouldBeEqual() {
        // Given: Two cache keys created with identical content at the same time
        Date fixedDate = new Date();
        Object[] keyContent = { 1, "hello", null, new Date(fixedDate.getTime()) };

        CacheKey firstKey = new CacheKey(keyContent);
        CacheKey secondKey = new CacheKey(new Object[] { 1, "hello", null, new Date(fixedDate.getTime()) });

        // When & Then: Keys should be equal in all aspects
        assertEquals(firstKey, secondKey, "Cache keys with identical content should be equal");
        assertEquals(secondKey, firstKey, "Equality should be symmetric");
        assertEquals(firstKey.hashCode(), secondKey.hashCode(), "Equal objects must have equal hash codes");
        assertEquals(firstKey.toString(), secondKey.toString(), "String representations should be identical");
    }

    @Test
    void cacheKeysWithDifferentTimestamps_shouldNotBeEqual() throws InterruptedException {
        // Given: Two cache keys with different Date objects (different timestamps)
        CacheKey keyWithEarlierDate = new CacheKey(new Object[] { 1, "hello", null, new Date() });

        Thread.sleep(10); // Ensure different timestamps

        CacheKey keyWithLaterDate = new CacheKey(new Object[] { 1, "hello", null, new Date() });

        // When & Then: Keys should not be equal due to timestamp difference
        assertNotEquals(keyWithEarlierDate, keyWithLaterDate, "Cache keys with different timestamps should not be equal");
        assertNotEquals(keyWithLaterDate, keyWithEarlierDate, "Inequality should be symmetric");
        assertNotEquals(keyWithEarlierDate.hashCode(), keyWithLaterDate.hashCode(), "Different objects should have different hash codes");
        assertNotEquals(keyWithEarlierDate.toString(), keyWithLaterDate.toString(), "String representations should differ");
    }

    @Test
    void cacheKeysWithDifferentElementOrder_shouldNotBeEqual() {
        // Given: Two cache keys with same elements but in different order
        CacheKey keyWithOrderABC = new CacheKey(new Object[] { 1, "hello", null });
        CacheKey keyWithOrderACB = new CacheKey(new Object[] { 1, null, "hello" });

        // When & Then: Keys should not be equal because order matters
        assertNotEquals(keyWithOrderABC, keyWithOrderACB, "Cache keys with different element order should not be equal");
        assertNotEquals(keyWithOrderACB, keyWithOrderABC, "Inequality should be symmetric");
        assertNotEquals(keyWithOrderABC.hashCode(), keyWithOrderACB.hashCode(), "Different objects should have different hash codes");
        assertNotEquals(keyWithOrderABC.toString(), keyWithOrderACB.toString(), "String representations should differ");
    }

    @Test
    void cacheKeysWithByteArrays_shouldBeEqualByContent() {
        // Given: Two cache keys containing byte arrays with identical content
        byte[] firstArray = { 1, 2, 3 };
        byte[] secondArray = { 1, 2, 3 }; // Same content, different array instance

        CacheKey keyWithFirstArray = new CacheKey(new Object[] { firstArray });
        CacheKey keyWithSecondArray = new CacheKey(new Object[] { secondArray });

        // When & Then: Keys should be equal based on array content, not reference
        assertEquals(keyWithFirstArray, keyWithSecondArray,
                "Cache keys with byte arrays of identical content should be equal");
    }

    // ========== EMPTY AND NULL KEY TESTS ==========

    @Test
    void emptyCacheKeys_shouldBeEqual() {
        // Given: Two empty cache keys
        CacheKey firstEmptyKey = new CacheKey();
        CacheKey secondEmptyKey = new CacheKey();

        // When & Then: Empty keys should be equal
        assertEquals(firstEmptyKey, secondEmptyKey, "Empty cache keys should be equal");
        assertEquals(secondEmptyKey, firstEmptyKey, "Equality should be symmetric");
    }

    @Test
    void cacheKeysAfterUpdatingWithNull_shouldRemainEqual() {
        // Given: Two empty cache keys
        CacheKey firstKey = new CacheKey();
        CacheKey secondKey = new CacheKey();

        // When: Both keys are updated with null values
        firstKey.update(null);
        secondKey.update(null);

        // Then: Keys should still be equal
        assertEquals(firstKey, secondKey, "Cache keys should remain equal after updating with null");
        assertEquals(secondKey, firstKey, "Equality should be symmetric");

        // When: Updated again with null
        firstKey.update(null);
        secondKey.update(null);

        // Then: Keys should still be equal
        assertEquals(firstKey, secondKey, "Cache keys should remain equal after multiple null updates");
        assertEquals(secondKey, firstKey, "Equality should be symmetric");
    }

    // ========== NULL_CACHE_KEY BEHAVIOR TESTS ==========

    @Test
    void nullCacheKey_shouldThrowExceptionWhenUpdated() {
        // Given: The special NULL_CACHE_KEY constant
        CacheKey nullCacheKey = CacheKey.NULL_CACHE_KEY;

        // When & Then: Attempting to update should throw an exception
        assertThrows(CacheException.class,
                () -> nullCacheKey.update("any value"),
                "NULL_CACHE_KEY should throw CacheException when updated");
    }

    @Test
    void nullCacheKey_shouldThrowExceptionWhenUpdatedWithArray() {
        // Given: The special NULL_CACHE_KEY constant
        CacheKey nullCacheKey = CacheKey.NULL_CACHE_KEY;

        // When & Then: Attempting to update with array should throw an exception
        assertThrows(CacheException.class,
                () -> nullCacheKey.updateAll(new Object[] { "value1", "value2" }),
                "NULL_CACHE_KEY should throw CacheException when updated with array");
    }

    @Test
    void clonedNullCacheKey_shouldEqualOriginal() throws CloneNotSupportedException {
        // Given: The special NULL_CACHE_KEY constant
        CacheKey originalNullKey = CacheKey.NULL_CACHE_KEY;

        // When: The null cache key is cloned
        CacheKey clonedNullKey = originalNullKey.clone();

        // Then: Clone should equal the original
        assertEquals(originalNullKey, clonedNullKey, "Cloned NULL_CACHE_KEY should equal original");
        assertEquals(originalNullKey.hashCode(), clonedNullKey.hashCode(),
                "Cloned NULL_CACHE_KEY should have same hash code as original");
    }

    // ========== SERIALIZATION TESTS ==========

    @Test
    void cacheKeyWithNonSerializableContent_shouldThrowSerializationException() {
        // Given: A cache key containing a non-serializable object
        CacheKey keyWithNonSerializableContent = new CacheKey();
        keyWithNonSerializableContent.update(new Object()); // Object is not serializable

        // When & Then: Serialization should fail
        assertThrows(NotSerializableException.class,
                () -> serializeAndDeserialize(keyWithNonSerializableContent),
                "Cache key with non-serializable content should throw NotSerializableException");
    }

    @Test
    void cacheKeyWithSerializableContent_shouldSerializeSuccessfully() throws Exception {
        // Given: A cache key containing only serializable content
        CacheKey originalKey = new CacheKey();
        originalKey.update("this string is serializable");

        // When: The cache key is serialized and deserialized
        CacheKey deserializedKey = serializeAndDeserialize(originalKey);

        // Then: The deserialized key should equal the original
        assertEquals(originalKey, deserializedKey,
                "Deserialized cache key should equal the original");
    }

    // ========== HELPER METHODS ==========

    /**
     * Serializes an object to bytes and then deserializes it back to an object.
     * This simulates the full serialization/deserialization cycle.
     */
    private static <T> T serializeAndDeserialize(T object) throws Exception {
        // Serialize to byte array
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        try (ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput)) {
            objectOutput.writeObject(object);
        }

        // Deserialize from byte array
        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteOutput.toByteArray());
        try (ObjectInputStream objectInput = new ObjectInputStream(byteInput)) {
            return (T) objectInput.readObject();
        }
    }
}