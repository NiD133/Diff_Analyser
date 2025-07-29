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
  void testCacheKeysEquality() {
    Date date = new Date();
    CacheKey key1 = createCacheKey(1, "hello", null, new Date(date.getTime()));
    CacheKey key2 = createCacheKey(1, "hello", null, new Date(date.getTime()));

    assertCacheKeysAreEqual(key1, key2);
  }

  @Test
  void testCacheKeysInequalityDueToDateDifference() throws InterruptedException {
    CacheKey key1 = createCacheKey(1, "hello", null, new Date());
    Thread.sleep(1000);
    CacheKey key2 = createCacheKey(1, "hello", null, new Date());

    assertCacheKeysAreNotEqual(key1, key2);
  }

  @Test
  void testCacheKeysInequalityDueToOrder() {
    CacheKey key1 = createCacheKey(1, "hello", null);
    CacheKey key2 = createCacheKey(1, null, "hello");

    assertCacheKeysAreNotEqual(key1, key2);
  }

  @Test
  void testEmptyAndNullKeysEquality() {
    CacheKey key1 = new CacheKey();
    CacheKey key2 = new CacheKey();

    assertCacheKeysAreEqual(key1, key2);

    key1.update(null);
    key2.update(null);
    assertCacheKeysAreEqual(key1, key2);
  }

  @Test
  void testCacheKeysWithBinaryArraysEquality() {
    byte[] array1 = { 1 };
    byte[] array2 = { 1 };
    CacheKey key1 = createCacheKey(array1);
    CacheKey key2 = createCacheKey(array2);

    assertCacheKeysAreEqual(key1, key2);
  }

  @Test
  void testExceptionOnUpdatingNullCacheKey() {
    CacheKey cacheKey = CacheKey.NULL_CACHE_KEY;
    assertThrows(CacheException.class, () -> cacheKey.update("null"));
  }

  @Test
  void testExceptionOnUpdatingAllNullCacheKey() {
    CacheKey cacheKey = CacheKey.NULL_CACHE_KEY;
    assertThrows(CacheException.class, () -> cacheKey.updateAll(new Object[] { "null", "null" }));
  }

  @Test
  void testClonedNullCacheKeysEquality() throws CloneNotSupportedException {
    CacheKey cacheKey = CacheKey.NULL_CACHE_KEY;
    CacheKey clonedCacheKey = cacheKey.clone();

    assertCacheKeysAreEqual(cacheKey, clonedCacheKey);
  }

  @Test
  void testSerializationException() {
    CacheKey cacheKey = new CacheKey();
    cacheKey.update(new Object());

    assertThrows(NotSerializableException.class, () -> serialize(cacheKey));
  }

  @Test
  void testSerialization() throws Exception {
    CacheKey cacheKey = new CacheKey();
    cacheKey.update("serializable");

    assertEquals(cacheKey, serialize(cacheKey));
  }

  private CacheKey createCacheKey(Object... objects) {
    return new CacheKey(objects);
  }

  private void assertCacheKeysAreEqual(CacheKey key1, CacheKey key2) {
    assertEquals(key1, key2);
    assertEquals(key2, key1);
    assertEquals(key1.hashCode(), key2.hashCode());
    assertEquals(key1.toString(), key2.toString());
  }

  private void assertCacheKeysAreNotEqual(CacheKey key1, CacheKey key2) {
    assertNotEquals(key1, key2);
    assertNotEquals(key2, key1);
    assertNotEquals(key1.hashCode(), key2.hashCode());
    assertNotEquals(key1.toString(), key2.toString());
  }

  private static <T> T serialize(T object) throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    new ObjectOutputStream(baos).writeObject(object);

    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    return (T) new ObjectInputStream(bais).readObject();
  }
}