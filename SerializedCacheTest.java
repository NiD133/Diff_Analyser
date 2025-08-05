package org.apache.ibatis.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.Serializable;
import java.util.Objects;

import org.apache.ibatis.cache.decorators.SerializedCache;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.jupiter.api.Test;

class SerializedCacheTest {

  @Test
  void testSerializedObjectsEquality() {
    // Create a SerializedCache with a PerpetualCache as the delegate
    SerializedCache cache = new SerializedCache(new PerpetualCache("default"));
    
    // Add CachingObject instances to the cache
    for (int i = 0; i < 5; i++) {
      cache.putObject(i, new CachingObject(i));
    }
    
    // Verify that the objects retrieved from the cache are equal to the original objects
    for (int i = 0; i < 5; i++) {
      assertEquals(new CachingObject(i), cache.getObject(i), "Cached object should be equal to the original");
    }
  }

  @Test
  void testNullValuesAreSerializable() {
    // Create a SerializedCache with a PerpetualCache as the delegate
    SerializedCache cache = new SerializedCache(new PerpetualCache("default"));
    
    // Add null values to the cache
    for (int i = 0; i < 5; i++) {
      cache.putObject(i, null);
    }
    
    // Verify that the cache returns null for the keys
    for (int i = 0; i < 5; i++) {
      assertNull(cache.getObject(i), "Cached value should be null");
    }
  }

  @Test
  void testExceptionForNonSerializableObject() {
    // Create a SerializedCache with a PerpetualCache as the delegate
    SerializedCache cache = new SerializedCache(new PerpetualCache("default"));
    
    // Verify that attempting to cache a non-serializable object throws a CacheException
    assertThrows(CacheException.class, () -> cache.putObject(0, new NonSerializableCachingObject(0)),
        "Putting a non-serializable object should throw CacheException");
  }

  // Serializable object for caching
  static class CachingObject implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int value;

    public CachingObject(int value) {
      this.value = value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CachingObject that = (CachingObject) o;
      return value == that.value;
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }
  }

  // Non-serializable object for testing exception
  static class NonSerializableCachingObject {
    private final int value;

    public NonSerializableCachingObject(int value) {
      this.value = value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      NonSerializableCachingObject that = (NonSerializableCachingObject) o;
      return value == that.value;
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }
  }
}