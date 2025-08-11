package org.apache.ibatis.cache.decorators;

import static org.junit.Assert.*;

import java.io.Serializable;
import java.util.Objects;

import org.apache.ibatis.cache.Cache;
import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.cache.impl.PerpetualCache;
import org.junit.Test;

/**
 * Tests for SerializedCache focusing on behavior and readability.
 * 
 * What is covered:
 * - Values are stored as serialized bytes and deserialized on read.
 * - A deep copy is made on write (mutating the original after put does not affect the cached value).
 * - Null values are supported.
 * - Size, clear and id delegation to the underlying cache.
 * - Equality and hashCode semantics are based on cache id (consistent with other Cache impls).
 * - Non-serializable values are rejected.
 */
public class SerializedCacheTest {

  private static final String CACHE_ID = "serialized-cache-test";

  private Cache newBackingCache() {
    return new PerpetualCache(CACHE_ID);
  }

  private SerializedCache newCache() {
    return new SerializedCache(newBackingCache());
  }

  private static final class Person implements Serializable {
    String name;

    Person(String name) {
      this.name = name;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof Person)) return false;
      Person person = (Person) o;
      return Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
      return Objects.hash(name);
    }
  }

  private static final class NotSerializable {
    final String value;
    NotSerializable(String value) { this.value = value; }
  }

  @Test
  public void putAndGet_serializableValue_returnsDeepCopy() {
    SerializedCache cache = newCache();
    Person original = new Person("Alice");

    cache.putObject("p1", original);

    // Mutate the original after caching
    original.name = "Bob";

    Person cached = (Person) cache.getObject("p1");

    // Cached value is a deserialized copy captured at put-time
    assertEquals(new Person("Alice"), cached);
    assertNotSame("Expected a deep copy (different reference)", original, cached);
  }

  @Test
  public void putAndGet_stringRoundTrip() {
    SerializedCache cache = newCache();

    cache.putObject("k", "hello");
    Object value = cache.getObject("k");

    assertEquals("hello", value);
    // For strings, deserialization typically returns a different instance
    assertNotSame("Expected a deep copy for serialized values", "hello", value);
  }

  @Test
  public void putAndGet_nullValue_supported() {
    SerializedCache cache = newCache();

    cache.putObject("k", null);
    assertNull(cache.getObject("k"));
  }

  @Test
  public void sizeAndClear_delegateToBackingCache() {
    SerializedCache cache = newCache();

    assertEquals(0, cache.getSize());

    cache.putObject("a", "x");
    cache.putObject("b", "y");
    assertEquals(2, cache.getSize());

    cache.clear();
    assertEquals(0, cache.getSize());
  }

  @Test
  public void getId_isForwardedToDelegate() {
    SerializedCache cache = newCache();
    assertEquals(CACHE_ID, cache.getId());
  }

  @Test
  public void equalsAndHashCode_basedOnId_acrossDifferentCacheTypes() {
    Cache otherTypeSameId = new PerpetualCache(CACHE_ID);
    SerializedCache cache = newCache();

    // Equal because both share the same id
    assertTrue(cache.equals(otherTypeSameId));
    assertTrue(otherTypeSameId.equals(cache));
    assertEquals(cache.hashCode(), otherTypeSameId.hashCode());

    // Not equal for different id
    Cache differentId = new PerpetualCache("different-id");
    assertFalse(cache.equals(differentId));
    assertFalse(differentId.equals(cache));
  }

  @Test
  public void remove_returnsNullWhenKeyAbsent() {
    SerializedCache cache = newCache();
    assertNull(cache.removeObject("absent"));
  }

  @Test
  public void put_nonSerializable_throwsCacheException() {
    SerializedCache cache = newCache();

    CacheException ex = assertThrows(CacheException.class,
        () -> cache.putObject("k", new NotSerializable("v")));
    // Keep the message assertion loose to avoid brittleness across versions
    assertTrue(ex.getMessage().toLowerCase().contains("non-serializable"));
  }
}