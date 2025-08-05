/*
 *    Copyright 2009-2023 the original author or authors.
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

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests for the {@link CacheKey} class.
 * This suite verifies the correctness of its state, equality, hashing, and cloning logic.
 */
public class CacheKeyTest {

    //region Constructor Tests

    @Test
    public void constructor_shouldCreateEmptyKeyFromDefault() {
        // Arrange & Act
        CacheKey key = new CacheKey();

        // Assert
        assertEquals(0, key.getUpdateCount());
        assertEquals("17:0", key.toString()); // Default hashcode:checksum
    }

    @Test
    public void constructor_shouldCreateKeyFromObjectArray() {
        // Arrange & Act
        CacheKey key = new CacheKey(new Object[]{"hello", 123});

        // Assert
        assertEquals(2, key.getUpdateCount());
    }

    @Test(expected = NullPointerException.class)
    public void constructor_shouldThrowNullPointerException_whenGivenNullArray() {
        // Act
        new CacheKey(null);
    }

    //endregion

    //region Update and State Tests

    @Test
    public void getUpdateCount_shouldReturnCorrectCountAfterUpdates() {
        // Arrange
        CacheKey key = new CacheKey();
        assertEquals("Initial count should be zero", 0, key.getUpdateCount());

        // Act: Single update
        key.update("first");

        // Assert
        assertEquals("Count should be 1 after single update", 1, key.getUpdateCount());

        // Act: Bulk update
        key.updateAll(new Object[]{"second", "third"});

        // Assert
        assertEquals("Count should be 3 after bulk update", 3, key.getUpdateCount());
    }

    @Test
    public void update_shouldAcceptNullObject() {
        // Arrange
        CacheKey key = new CacheKey();

        // Act
        key.update(null);

        // Assert
        assertEquals(1, key.getUpdateCount());
    }

    @Test(expected = NullPointerException.class)
    public void updateAll_shouldThrowNullPointerException_whenGivenNullArray() {
        // Arrange
        CacheKey key = new CacheKey();

        // Act
        key.updateAll(null);
    }

    //endregion

    //region Equals and HashCode Contract

    @Test
    public void equals_shouldReturnTrue_forTwoEmptyKeys() {
        // Arrange
        CacheKey key1 = new CacheKey();
        CacheKey key2 = new CacheKey(new Object[]{});

        // Act & Assert
        assertEquals(key1, key2);
    }

    @Test
    public void equals_shouldReturnTrue_forIdenticalKeys() {
        // Arrange
        Object[] objects = new Object[]{1, "two", null};
        CacheKey key1 = new CacheKey(objects);
        CacheKey key2 = new CacheKey(objects);

        // Act & Assert
        assertEquals(key1, key2);
    }

    @Test
    public void equals_shouldReturnTrue_whenComparedToSelf() {
        // Arrange
        CacheKey key = new CacheKey();

        // Act & Assert
        assertEquals(key, key);
    }

    @Test
    public void equals_shouldReturnFalse_whenOneKeyIsUpdated() {
        // Arrange
        CacheKey key1 = new CacheKey();
        CacheKey key2 = new CacheKey();
        assertTrue("Keys should be equal initially", key1.equals(key2));

        // Act
        key1.update("something");

        // Assert
        assertNotEquals("Keys should not be equal after one is updated", key1, key2);
    }

    @Test
    public void equals_shouldReturnFalse_forKeysWithDifferentObjects() {
        // Arrange
        CacheKey key1 = new CacheKey(new Object[]{"one", 2});
        CacheKey key2 = new CacheKey(new Object[]{"one", "two"});

        // Act & Assert
        assertNotEquals(key1, key2);
    }

    @Test
    public void equals_shouldReturnFalse_whenComparedToNull() {
        // Arrange
        CacheKey key = new CacheKey();

        // Act & Assert
        assertFalse(key.equals(null));
    }

    @Test
    public void equals_shouldReturnFalse_whenComparedToDifferentClass() {
        // Arrange
        CacheKey key = new CacheKey();
        Object otherObject = new Object();

        // Act & Assert
        assertNotEquals(key, otherObject);
    }

    @Test
    public void hashCode_shouldBeEqual_forEqualKeys() {
        // Arrange
        CacheKey key1 = new CacheKey(new Object[]{1, "two", null});
        CacheKey key2 = new CacheKey(new Object[]{1, "two", null});

        // Act & Assert
        assertEquals(key1, key2);
        assertEquals(key1.hashCode(), key2.hashCode());
    }

    @Test
    public void hashCode_shouldBeDifferent_forUnequalKeys() {
        // Arrange
        CacheKey key1 = new CacheKey(new Object[]{1, "two"});
        CacheKey key2 = new CacheKey(new Object[]{1, "three"});

        // Act & Assert
        assertNotEquals(key1, key2);
        // Note: Hash collisions are possible but highly unlikely with this data.
        assertNotEquals(key1.hashCode(), key2.hashCode());
    }

    //endregion

    //region Clone Tests

    @Test
    public void clone_shouldProduceEqualButNotSameInstance() throws CloneNotSupportedException {
        // Arrange
        CacheKey originalKey = new CacheKey(new Object[]{"A", 1, null});

        // Act
        CacheKey clonedKey = originalKey.clone();

        // Assert
        assertNotSame("Cloned key should be a different instance", originalKey, clonedKey);
        assertEquals("Cloned key should be equal to the original", originalKey, clonedKey);
    }

    //endregion

    //region ToString Tests

    @Test
    public void toString_shouldReturnFormattedStringOfHashCodeAndChecksum() {
        // Arrange
        CacheKey key = new CacheKey();
        key.update("test");
        String keyAsString = key.toString();
        int hashCode = key.hashCode();

        // Assert
        // The format is hashcode:checksum, e.g., "524165338:88805143"
        assertTrue("toString() should match the format 'hashcode:checksum'", keyAsString.matches("-?\\d+:-?\\d+"));
        assertTrue("toString() should start with the hashcode and a colon", keyAsString.startsWith(hashCode + ":"));
    }

    //endregion

    //region NULL_CACHE_KEY Tests

    @Test(expected = RuntimeException.class)
    public void nullCacheKey_shouldThrowException_whenUpdateIsCalled() {
        // Act
        CacheKey.NULL_CACHE_KEY.update("test");
    }

    @Test(expected = RuntimeException.class)
    public void nullCacheKey_shouldThrowException_whenUpdateAllIsCalled() {
        // Act
        CacheKey.NULL_CACHE_KEY.updateAll(new Object[]{"test"});
    }

    //endregion
}