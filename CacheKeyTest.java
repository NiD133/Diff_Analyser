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

class CacheKeyTest {

  @Test
  void testCacheKeysEqualityWithSameValues() {
    // Arrange
    Date date = new Date();
    CacheKey key1 = new CacheKey(new Object[] { 1, "hello", null, new Date(date.getTime()) });
    CacheKey key2 = new CacheKey(new Object[] { 1, "hello", null, new Date(date.getTime()) });

    // Act & Assert
    assertEquals(key1, key2, "CacheKeys with identical values should be equal");
    assertEquals(key1.hashCode(), key2.hashCode(), "Hash codes should be equal for identical CacheKeys");
    assertEquals(key1.toString(), key2.toString(), "String representations should be equal for identical CacheKeys");
  }

  @Test
  void testCacheKeysInequalityDueToDateDifference() throws InterruptedException {
    // Arrange
    CacheKey key1 = new CacheKey(new Object[] { 1, "hello", null, new Date() });
    Thread.sleep(1000); // Ensure a time difference
    CacheKey key2 = new CacheKey(new Object[] { 1, "hello", null, new Date() });

    // Act & Assert
    assertNotEquals(key1, key2, "CacheKeys with different dates should not be equal");
  }

  @Test
  void testCacheKeysInequalityDueToOrderDifference() {
    // Arrange
    CacheKey key1 = new CacheKey(new Object[] { 1, "hello", null });
    CacheKey key2 = new CacheKey(new Object[] { 1, null, "hello" });

    // Act & Assert
    assertNotEquals(key1, key2, "CacheKeys with different order of elements should not be equal");
  }

  @Test
  void testEmptyAndNullKeysEquality() {
    // Arrange
    CacheKey key1 = new CacheKey();
    CacheKey key2 = new CacheKey();

    // Act & Assert
    assertEquals(key1, key2, "Empty CacheKeys should be equal");

    // Update with null values
    key1.update(null);
    key2.update(null);
    assertEquals(key1, key2, "CacheKeys updated with null should remain equal");
  }

  @Test
  void testCacheKeysEqualityWithBinaryArrays() {
    // Arrange
    byte[] array1 = { 1 };
    byte[] array2 = { 1 };
    CacheKey key1 = new CacheKey(new Object[] { array1 });
    CacheKey key2 = new CacheKey(new Object[] { array2 });

    // Act & Assert
    assertEquals(key1, key2, "CacheKeys with identical binary arrays should be equal");
  }

  @Test
  void testExceptionWhenUpdatingNullCacheKey() {
    // Arrange
    CacheKey cacheKey = CacheKey.NULL_CACHE_KEY;

    // Act & Assert
    assertThrows(CacheException.class, () -> cacheKey.update("null"), "Updating NULL_CACHE_KEY should throw CacheException");
  }

  @Test
  void testExceptionWhenUpdatingAllNullCacheKey() {
    // Arrange
    CacheKey cacheKey = CacheKey.NULL_CACHE_KEY;

    // Act & Assert
    assertThrows(CacheException.class, () -> cacheKey.updateAll(new Object[] { "null", "null" }), "Updating all NULL_CACHE_KEY should throw CacheException");
  }

  @Test
  void testClonedNullCacheKeysEquality() throws CloneNotSupportedException {
    // Arrange
    CacheKey cacheKey = CacheKey.NULL_CACHE_KEY;
    CacheKey clonedCacheKey = cacheKey.clone();

    // Act & Assert
    assertEquals(cacheKey, clonedCacheKey, "Cloned NULL_CACHE_KEY should be equal to the original");
    assertEquals(cacheKey.hashCode(), clonedCacheKey.hashCode(), "Hash codes of cloned and original NULL_CACHE_KEY should be equal");
  }

  @Test
  void testSerializationExceptionForNonSerializableObject() {
    // Arrange
    CacheKey cacheKey = new CacheKey();
    cacheKey.update(new Object());

    // Act & Assert
    assertThrows(NotSerializableException.class, () -> serialize(cacheKey), "Serializing a CacheKey with non-serializable object should throw NotSerializableException");
  }

  @Test
  void testSerializationOfSerializableObject() throws Exception {
    // Arrange
    CacheKey cacheKey = new CacheKey();
    cacheKey.update("serializable");

    // Act & Assert
    assertEquals(cacheKey, serialize(cacheKey), "Serialized and deserialized CacheKey should be equal");
  }

  private static <T> T serialize(T object) throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    new ObjectOutputStream(baos).writeObject(object);

    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    return (T) new ObjectInputStream(bais).readObject();
  }
}