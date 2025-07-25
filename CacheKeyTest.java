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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CacheKeyTest {

  @Test
  @DisplayName("Should create equal CacheKeys with the same parameters")
  void shouldCreateEqualCacheKeys() {
    // Arrange
    Date date = new Date();
    Object[] params1 = { 1, "hello", null, new Date(date.getTime()) };
    Object[] params2 = { 1, "hello", null, new Date(date.getTime()) };

    // Act
    CacheKey key1 = new CacheKey(params1);
    CacheKey key2 = new CacheKey(params2);

    // Assert
    assertEquals(key1, key2, "CacheKeys should be equal");
    assertEquals(key2, key1, "CacheKeys should be equal (symmetric)");
    assertEquals(key1.hashCode(), key2.hashCode(), "Hash codes should be equal");
    assertEquals(key1.toString(), key2.toString(), "toString() representations should be equal");
  }

  @Test
  @DisplayName("Should create unequal CacheKeys when Date parameters have different timestamps")
  void shouldCreateUnequalCacheKeys_dueToDateDifference() throws InterruptedException {
    // Arrange
    CacheKey key1 = new CacheKey(new Object[] { 1, "hello", null, new Date() });
    Thread.sleep(1000); // Simulate time passing
    CacheKey key2 = new CacheKey(new Object[] { 1, "hello", null, new Date() });

    // Act & Assert
    assertNotEquals(key1, key2, "CacheKeys should not be equal due to different dates");
    assertNotEquals(key2, key1, "CacheKeys should not be equal (symmetric)");
    assertNotEquals(key1.hashCode(), key2.hashCode(), "Hash codes should not be equal");
    assertNotEquals(key1.toString(), key2.toString(), "toString() representations should not be equal");
  }

  @Test
  @DisplayName("Should create unequal CacheKeys when parameters are in different order")
  void shouldCreateUnequalCacheKeys_dueToOrder() throws InterruptedException {
    // Arrange
    CacheKey key1 = new CacheKey(new Object[] { 1, "hello", null });
    Thread.sleep(1000); // Simulate time passing (though not relevant to the test)
    CacheKey key2 = new CacheKey(new Object[] { 1, null, "hello" });

    // Act & Assert
    assertNotEquals(key1, key2, "CacheKeys should not be equal due to different order");
    assertNotEquals(key2, key1, "CacheKeys should not be equal (symmetric)");
    assertNotEquals(key1.hashCode(), key2.hashCode(), "Hash codes should not be equal");
    assertNotEquals(key1.toString(), key2.toString(), "toString() representations should not be equal");
  }

  @Test
  @DisplayName("Should treat empty and null-updated CacheKeys as equal")
  void shouldTreatEmptyAndNullKeysAsEqual() {
    // Arrange
    CacheKey key1 = new CacheKey();
    CacheKey key2 = new CacheKey();

    // Act & Assert
    assertEquals(key1, key2, "Initially, CacheKeys should be equal");
    assertEquals(key2, key1, "Initially, CacheKeys should be equal (symmetric)");

    key1.update(null);
    key2.update(null);
    assertEquals(key1, key2, "After updating with null, CacheKeys should be equal");
    assertEquals(key2, key1, "After updating with null, CacheKeys should be equal (symmetric)");

    key1.update(null);
    key2.update(null);
    assertEquals(key1, key2, "After multiple null updates, CacheKeys should be equal");
    assertEquals(key2, key1, "After multiple null updates, CacheKeys should be equal (symmetric)");
  }

  @Test
  @DisplayName("Should create equal CacheKeys with equal binary arrays as parameters")
  void shouldCreateEqualCacheKeys_withBinaryArrays() {
    // Arrange
    byte[] array1 = { 1 };
    byte[] array2 = { 1 };

    // Act
    CacheKey key1 = new CacheKey(new Object[] { array1 });
    CacheKey key2 = new CacheKey(new Object[] { array2 });

    // Assert
    assertEquals(key1, key2, "CacheKeys with equal binary arrays should be equal");
  }

  @Test
  @DisplayName("Should throw CacheException when trying to update NULL_CACHE_KEY")
  void shouldThrowException_whenUpdatingNullCacheKey() {
    // Arrange
    CacheKey cacheKey = CacheKey.NULL_CACHE_KEY;

    // Act & Assert
    assertThrows(CacheException.class, () -> cacheKey.update("null"), "Updating NULL_CACHE_KEY should throw an exception");
  }

  @Test
  @DisplayName("Should throw CacheException when trying to updateAll NULL_CACHE_KEY")
  void shouldThrowException_whenUpdatingAllNullCacheKey() {
    // Arrange
    CacheKey cacheKey = CacheKey.NULL_CACHE_KEY;

    // Act & Assert
    assertThrows(CacheException.class, () -> cacheKey.updateAll(new Object[] { "null", "null" }),
        "Updating ALL NULL_CACHE_KEY should throw an exception");
  }

  @Test
  @DisplayName("Should demonstrate that cloned NULL_CACHE_KEYs are equal")
  void shouldDemonstrateClonedNullCacheKeysAreEqual() throws CloneNotSupportedException {
    // Arrange
    CacheKey cacheKey = CacheKey.NULL_CACHE_KEY;

    // Act
    CacheKey clonedCacheKey = cacheKey.clone();

    // Assert
    assertEquals(cacheKey, clonedCacheKey, "Cloned NULL_CACHE_KEY should be equal to the original");
    assertEquals(cacheKey.hashCode(), clonedCacheKey.hashCode(), "Cloned NULL_CACHE_KEY should have the same hashCode");
  }

  @Test
  @DisplayName("Should throw NotSerializableException when CacheKey contains a non-serializable object")
  void shouldThrowNotSerializableException_whenCacheKeyContainsNonSerializableObject() {
    // Arrange
    CacheKey cacheKey = new CacheKey();
    cacheKey.update(new Object()); // Add a non-serializable object

    // Act & Assert
    assertThrows(NotSerializableException.class, () -> serialize(cacheKey),
        "Serializing a CacheKey with a non-serializable object should throw NotSerializableException");
  }

  @Test
  @DisplayName("Should successfully serialize and deserialize a CacheKey with serializable content")
  void shouldSerializeCacheKey_withSerializableContent() throws Exception {
    // Arrange
    CacheKey cacheKey = new CacheKey();
    cacheKey.update("serializable");

    // Act
    CacheKey serializedCacheKey = serialize(cacheKey);

    // Assert
    assertEquals(cacheKey, serializedCacheKey, "The serialized CacheKey should be equal to the original");
  }

  private static <T> T serialize(T object) throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    new ObjectOutputStream(baos).writeObject(object);

    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    return (T) new ObjectInputStream(bais).readObject();
  }
}