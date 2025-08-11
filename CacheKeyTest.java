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

import static org.junit.jupiter.api.Assertions.assertAll;
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
 * Tests for {@link CacheKey}.
 * Focuses on equality, hashing, serialization, and immutability of the NULL_CACHE_KEY.
 */
class CacheKeyTest {

  @Test
  void keysWithIdenticalComponentsShouldBeEqualAndHaveSameHashCode() {
    // Arrange
    Date date = new Date();
    Object[] components = { 1, "hello", null, new Date(date.getTime()) };
    CacheKey keyA = new CacheKey(components);
    CacheKey keyB = new CacheKey(components);

    // Act & Assert
    assertAll("Keys with identical components should be equal and have the same hash code",
        () -> assertEquals(keyA, keyB),
        () -> assertEquals(keyA.hashCode(), keyB.hashCode())
    );
  }

  @Test
  void keysWithDifferentDatesShouldNotBeEqual() {
    // Arrange
    CacheKey keyA = new CacheKey(new Object[] { 1, "hello", null, new Date(10000) });
    CacheKey keyB = new CacheKey(new Object[] { 1, "hello", null, new Date(20000) });

    // Act & Assert
    assertAll("Keys with different date components should not be equal",
        () -> assertNotEquals(keyA, keyB),
        () -> assertNotEquals(keyA.hashCode(), keyB.hashCode())
    );
  }

  @Test
  void keysWithDifferentComponentOrderShouldNotBeEqual() {
    // Arrange
    CacheKey keyA = new CacheKey(new Object[] { 1, "hello", null });
    CacheKey keyB = new CacheKey(new Object[] { 1, null, "hello" });

    // Act & Assert
    assertAll("Keys with components in a different order should not be equal",
        () -> assertNotEquals(keyA, keyB),
        () -> assertNotEquals(keyA.hashCode(), keyB.hashCode())
    );
  }

  @Test
  void emptyKeysOrKeysWithOnlyNullsShouldBeEqual() {
    // Arrange: Create two empty keys
    CacheKey keyA = new CacheKey();
    CacheKey keyB = new CacheKey();

    // Assert: Empty keys are equal
    assertEquals(keyA, keyB, "Empty keys should be equal");
    assertEquals(keyA.hashCode(), keyB.hashCode(), "Empty keys should have the same hash code");

    // Act: Update both with a null value
    keyA.update(null);
    keyB.update(null);

    // Assert: Keys with a single null update are still equal
    assertEquals(keyA, keyB, "Keys with a single null update should be equal");
    assertEquals(keyA.hashCode(), keyB.hashCode(), "Hash code should be consistent after null update");
  }

  @Test
  void keysWithIdenticalByteArraysShouldBeEqual() {
    // Arrange
    byte[] arrayA = { 1, 2, 3 };
    byte[] arrayB = { 1, 2, 3 }; // Intentionally a different instance with same content
    CacheKey keyA = new CacheKey(new Object[] { arrayA });
    CacheKey keyB = new CacheKey(new Object[] { arrayB });

    // Act & Assert
    assertAll("Keys with identical byte arrays should be equal",
        () -> assertEquals(keyA, keyB),
        () -> assertEquals(keyA.hashCode(), keyB.hashCode())
    );
  }

  @Test
  void updateOnNullCacheKeyShouldThrowException() {
    // Arrange
    CacheKey nullCacheKey = CacheKey.NULL_CACHE_KEY;

    // Act & Assert
    assertThrows(CacheException.class, () -> nullCacheKey.update("some-object"),
        "Updating NULL_CACHE_KEY should throw CacheException");
  }

  @Test
  void updateAllOnNullCacheKeyShouldThrowException() {
    // Arrange
    CacheKey nullCacheKey = CacheKey.NULL_CACHE_KEY;
    Object[] components = { "some-object", "another-object" };

    // Act & Assert
    assertThrows(CacheException.class, () -> nullCacheKey.updateAll(components),
        "Updating NULL_CACHE_KEY with multiple objects should throw CacheException");
  }

  @Test
  void clonedNullCacheKeyShouldBeEqualToOriginal() throws CloneNotSupportedException {
    // Arrange
    CacheKey original = CacheKey.NULL_CACHE_KEY;

    // Act
    CacheKey clone = original.clone();

    // Assert
    assertEquals(original, clone);
    assertEquals(original.hashCode(), clone.hashCode());
  }

  @Test
  void serializingKeyWithNonSerializableComponentShouldThrowException() {
    // Arrange
    CacheKey cacheKey = new CacheKey();
    cacheKey.update(new Object()); // java.lang.Object is not serializable

    // Act & Assert
    assertThrows(NotSerializableException.class, () -> serialize(cacheKey),
        "Serialization should fail for a key with a non-serializable component");
  }

  @Test
  void deserializedKeyShouldBeEqualToOriginal() throws Exception {
    // Arrange
    CacheKey originalKey = new CacheKey();
    originalKey.update("serializable-string");

    // Act
    CacheKey deserializedKey = serialize(originalKey);

    // Assert
    assertEquals(originalKey, deserializedKey, "Deserialized key should be equal to the original");
  }

  /**
   * Helper method to serialize and deserialize an object.
   */
  private static <T> T serialize(T object) throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(object);
    }

    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    try (ObjectInputStream ois = new ObjectInputStream(bais)) {
      @SuppressWarnings("unchecked")
      T deserializedObject = (T) ois.readObject();
      return deserializedObject;
    }
  }
}