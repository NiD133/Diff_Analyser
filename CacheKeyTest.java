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

class CacheKeyTest {

  // Test that identical parameters generate equal CacheKeys
  @Test
  void equalsHashcodeToString_shouldBeEqualForSameParameters() {
    Date date = new Date();
    CacheKey key1 = new CacheKey(new Object[] { 1, "hello", null, new Date(date.getTime()) });
    CacheKey key2 = new CacheKey(new Object[] { 1, "hello", null, new Date(date.getTime()) });

    // Validate symmetry and consistency of equals, hashCode, and toString
    assertEquals(key1, key2, "CacheKeys with same parameters should be equal");
    assertEquals(key2, key1, "equals() should be symmetric");
    assertEquals(key1.hashCode(), key2.hashCode(), "hashCode() should be consistent");
    assertEquals(key1.toString(), key2.toString(), "toString() output should match");
  }

  // Test that different dates result in unequal CacheKeys
  @Test
  void equalsHashcodeToString_shouldDifferForDifferentDates() {
    Date date1 = new Date();
    Date date2 = new Date(date1.getTime() + 1000); // Explicitly different time

    CacheKey key1 = new CacheKey(new Object[] { 1, "hello", null, date1 });
    CacheKey key2 = new CacheKey(new Object[] { 1, "hello", null, date2 });

    assertNotEquals(key1, key2, "CacheKeys with different dates should not be equal");
    assertNotEquals(key2, key1, "Inequality should be symmetric");
    assertNotEquals(key1.hashCode(), key2.hashCode(), "hashCode should differ for different dates");
    assertNotEquals(key1.toString(), key2.toString(), "toString output should differ");
  }

  // Test that parameter order affects equality
  @Test
  void equalsHashcodeToString_shouldDifferForDifferentObjectOrder() {
    CacheKey key1 = new CacheKey(new Object[] { 1, "hello", null });
    CacheKey key2 = new CacheKey(new Object[] { 1, null, "hello" });

    assertNotEquals(key1, key2, "CacheKey should be order-sensitive");
    assertNotEquals(key2, key1, "Inequality should be symmetric");
    assertNotEquals(key1.hashCode(), key2.hashCode(), "hashCode should differ for different orders");
    assertNotEquals(key1.toString(), key2.toString(), "toString output should differ");
  }

  // Test that empty CacheKeys and null updates behave correctly
  @Test
  void equals_shouldTreatEmptyAndNullUpdatesAsEqual() {
    CacheKey key1 = new CacheKey();
    CacheKey key2 = new CacheKey();
    assertEquals(key1, key2, "Empty CacheKeys should be equal");

    key1.update(null);
    key2.update(null);
    assertEquals(key1, key2, "CacheKeys after null update should remain equal");

    key1.update(null);
    key2.update(null);
    assertEquals(key1, key2, "Multiple null updates should maintain equality");
  }

  // Test that byte arrays with same content are considered equal
  @Test
  void equals_shouldBeEqualForSameByteArrayContent() {
    byte[] array1 = { 1 };
    byte[] array2 = { 1 };
    CacheKey key1 = new CacheKey(new Object[] { array1 });
    CacheKey key2 = new CacheKey(new Object[] { array2 });

    assertEquals(key1, key2, "CacheKeys with equivalent byte arrays should be equal");
  }

  // Test NULL_CACHE_KEY update restrictions
  @Test
  void update_shouldBlockUpdatesOnNullCacheKey() {
    CacheKey nullKey = CacheKey.NULL_CACHE_KEY;
    assertThrows(CacheException.class, () -> nullKey.update("value"),
        "Updating NULL_CACHE_KEY should throw CacheException");
  }

  // Test NULL_CACHE_KEY bulk update restrictions
  @Test
  void updateAll_shouldBlockBulkUpdatesOnNullCacheKey() {
    CacheKey nullKey = CacheKey.NULL_CACHE_KEY;
    assertThrows(CacheException.class, () -> nullKey.updateAll(new Object[] { "a", "b" }),
        "Bulk updating NULL_CACHE_KEY should throw CacheException");
  }

  // Test cloning behavior of NULL_CACHE_KEY
  @Test
  void clone_shouldProduceEqualInstanceForNullCacheKey() throws Exception {
    CacheKey nullKey = CacheKey.NULL_CACHE_KEY;
    CacheKey clonedKey = nullKey.clone();

    assertEquals(nullKey, clonedKey, "Cloned NULL_CACHE_KEY should be equal");
    assertEquals(nullKey.hashCode(), clonedKey.hashCode(), "Cloned NULL_CACHE_KEY should have same hashCode");
  }

  // Test serialization with non-serializable content
  @Test
  void serialize_shouldFailForNonSerializableContent() {
    CacheKey key = new CacheKey();
    key.update(new Object()); // Non-serializable

    assertThrows(NotSerializableException.class, () -> serialize(key),
        "Serialization should fail for non-serializable content");
  }

  // Test serialization with serializable content
  @Test
  void serialize_shouldMaintainEqualityForSerializableContent() throws Exception {
    CacheKey key = new CacheKey();
    key.update("serializable");

    CacheKey deserializedKey = serialize(key);
    assertEquals(key, deserializedKey, "Deserialized CacheKey should equal original");
  }

  // Helper method for serialization/deserialization
  private static <T> T serialize(T object) throws Exception {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(object);
    }

    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    try (ObjectInputStream ois = new ObjectInputStream(bais)) {
      return (T) ois.readObject();
    }
  }
}