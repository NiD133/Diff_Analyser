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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for CacheKey.
 *
 * Goals
 * - Use clear, scenario-based test names.
 * - Avoid time-based flakiness (no Thread.sleep; use fixed Dates instead).
 * - Remove duplication with helpers for equality/inequality assertions and key creation.
 * - Use AAA (Arrange-Act-Assert) structure and small comments to clarify intent.
 */
class CacheKeyTest {

  private static final String GREETING = "hello";
  // Fixed timestamps to avoid flakiness and unnecessary sleeps.
  private static final long BASE_TIME = 1_700_000_000_000L;
  private static final Date DATE_1 = new Date(BASE_TIME);
  private static final Date DATE_2 = new Date(BASE_TIME + 1_000L);

  @Test
  @DisplayName("Keys with same elements (including equal Date) are equal")
  void keysWithSameElementsAreEqual() {
    // Arrange
    CacheKey key1 = key(1, GREETING, null, new Date(DATE_1.getTime()));
    CacheKey key2 = key(1, GREETING, null, new Date(DATE_1.getTime()));

    // Assert
    assertEquivalent(key1, key2);
  }

  @Test
  @DisplayName("Keys differ when Dates differ")
  void keysDifferWhenDatesDiffer() {
    // Arrange
    CacheKey key1 = key(1, GREETING, null, DATE_1);
    CacheKey key2 = key(1, GREETING, null, DATE_2);

    // Assert
    assertDifferent(key1, key2);
  }

  @Test
  @DisplayName("Keys are order-sensitive")
  void keysAreOrderSensitive() {
    // Arrange
    CacheKey key1 = key(1, GREETING, null);
    CacheKey key2 = key(1, null, GREETING);

    // Assert
    assertDifferent(key1, key2);
  }

  @Test
  @DisplayName("Empty keys and keys updated with nulls remain equal")
  void emptyAndNullUpdatedKeysAreEqual() {
    // Arrange
    CacheKey key1 = new CacheKey();
    CacheKey key2 = new CacheKey();

    // Assert – empty keys equal
    assertEquivalent(key1, key2);

    // Act – update with null once
    key1.update(null);
    key2.update(null);

    // Assert – still equal
    assertEquivalent(key1, key2);

    // Act – update with null again
    key1.update(null);
    key2.update(null);

    // Assert – still equal
    assertEquivalent(key1, key2);
  }

  @Test
  @DisplayName("Byte array elements are compared by contents")
  void byteArrayElementsComparedByContents() {
    // Arrange
    byte[] array1 = { 1 };
    byte[] array2 = { 1 };

    CacheKey key1 = key(array1);
    CacheKey key2 = key(array2);

    // Assert
    assertEquivalent(key1, key2);
  }

  @Test
  @DisplayName("NULL_CACHE_KEY cannot be updated (single element)")
  void updatingNullCacheKeyThrowsOnUpdate() {
    // Arrange
    CacheKey cacheKey = CacheKey.NULL_CACHE_KEY;

    // Assert
    assertThrows(CacheException.class, () -> cacheKey.update("null"));
  }

  @Test
  @DisplayName("NULL_CACHE_KEY cannot be updated (multiple elements)")
  void updatingNullCacheKeyThrowsOnUpdateAll() {
    // Arrange
    CacheKey cacheKey = CacheKey.NULL_CACHE_KEY;

    // Assert
    assertThrows(CacheException.class, () -> cacheKey.updateAll(new Object[] { "null", "null" }));
  }

  @Test
  @DisplayName("Cloning NULL_CACHE_KEY yields an equal key with same hash")
  void clonedNullCacheKeyIsEqual() throws Exception {
    // Arrange
    CacheKey cacheKey = CacheKey.NULL_CACHE_KEY;

    // Act
    CacheKey cloned = cacheKey.clone();

    // Assert
    assertAll(
      () -> assertEquals(cacheKey, cloned),
      () -> assertEquals(cacheKey.hashCode(), cloned.hashCode())
    );
  }

  @Test
  @DisplayName("Serialization fails when key contains a non-serializable element")
  void serializationFailsForNonSerializableElement() {
    // Arrange
    CacheKey cacheKey = new CacheKey();
    cacheKey.update(new Object()); // not Serializable

    // Assert
    assertThrows(NotSerializableException.class, () -> roundTripSerialize(cacheKey));
  }

  @Test
  @DisplayName("Serialization round-trip preserves equality for serializable elements")
  void serializationRoundTripPreservesEquality() throws Exception {
    // Arrange
    CacheKey cacheKey = new CacheKey();
    cacheKey.update("serializable");

    // Act
    CacheKey deserialized = roundTripSerialize(cacheKey);

    // Assert
    assertEquals(cacheKey, deserialized);
  }

  // ---------- Helpers ----------

  private static CacheKey key(Object... parts) {
    return new CacheKey(parts);
  }

  private static void assertEquivalent(CacheKey a, CacheKey b) {
    assertAll(
      () -> assertEquals(a, b, "expected keys to be equal"),
      () -> assertEquals(b, a, "equality should be symmetric"),
      () -> assertEquals(a.hashCode(), b.hashCode(), "equal keys must have equal hash codes"),
      () -> assertEquals(a.toString(), b.toString(), "equal keys should have equal string forms")
    );
  }

  private static void assertDifferent(CacheKey a, CacheKey b) {
    assertAll(
      () -> assertNotEquals(a, b, "expected keys to be different"),
      () -> assertNotEquals(b, a, "inequality should be symmetric"),
      () -> assertNotEquals(a.hashCode(), b.hashCode(), "different keys should have different hash codes"),
      () -> assertNotEquals(a.toString(), b.toString(), "different keys should have different string forms")
    );
  }

  @SuppressWarnings("unchecked")
  private static <T> T roundTripSerialize(T object) throws Exception {
    // Serialize
    byte[] bytes;
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
         ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(object);
      oos.flush();
      bytes = baos.toByteArray();
    }

    // Deserialize
    try (ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
         ObjectInputStream ois = new ObjectInputStream(bais)) {
      return (T) ois.readObject();
    }
  }
}