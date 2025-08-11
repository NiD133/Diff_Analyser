/*
 * Copyright (C) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.gson.internal;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertThrows;

import com.google.gson.common.MoreAsserts;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import org.junit.Test;

/**
 * Tests for LinkedTreeMap, which maintains insertion order for iteration
 * while using comparison order for efficient insertion and removal.
 */
public final class LinkedTreeMapTest {

  // Constants for test data
  private static final String KEY_A = "a";
  private static final String KEY_B = "b";
  private static final String KEY_C = "c";
  private static final String VALUE_ANDROID = "android";
  private static final String VALUE_BBQ = "bbq";
  private static final String VALUE_COLA = "cola";
  
  // Test iteration order preservation
  
  @Test
  public void testIterationOrder_preservesInsertionOrder() {
    // Given: A LinkedTreeMap with entries inserted in a specific order
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    map.put(KEY_A, VALUE_ANDROID);
    map.put(KEY_C, VALUE_COLA);  // Insert 'c' before 'b' to test insertion order
    map.put(KEY_B, VALUE_BBQ);
    
    // Then: Keys and values should be returned in insertion order, not sorted order
    assertThat(map.keySet()).containsExactly(KEY_A, KEY_C, KEY_B).inOrder();
    assertThat(map.values()).containsExactly(VALUE_ANDROID, VALUE_COLA, VALUE_BBQ).inOrder();
  }

  @Test
  public void testIteratorRemove_maintainsCorrectLinkage() {
    // Given: A LinkedTreeMap with three entries
    LinkedTreeMap<String, String> map = createMapWithThreeEntries();
    
    // When: We iterate through all entries and remove the last one
    Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
    iterator.next(); // First entry
    iterator.next(); // Second entry
    iterator.next(); // Third entry
    iterator.remove(); // Remove the third entry
    
    // Then: The remaining entries should maintain correct order
    assertThat(map.keySet()).containsExactly(KEY_A, KEY_C).inOrder();
  }

  // Test null key handling
  
  @Test
  public void testPutNullKey_throwsNullPointerException() {
    // Given: An empty LinkedTreeMap
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    
    // When/Then: Attempting to put a null key should throw NPE with specific message
    NullPointerException exception = assertThrows(
        NullPointerException.class, 
        () -> map.put(null, VALUE_ANDROID)
    );
    assertThat(exception).hasMessageThat().isEqualTo("key == null");
  }

  @Test
  public void testContainsNullKey_alwaysReturnsFalse() {
    // Given: Both empty and non-empty LinkedTreeMaps
    LinkedTreeMap<String, String> emptyMap = new LinkedTreeMap<>();
    LinkedTreeMap<String, String> nonEmptyMap = new LinkedTreeMap<>();
    nonEmptyMap.put(KEY_A, VALUE_ANDROID);
    
    // Then: containsKey(null) should always return false
    assertThat(emptyMap.containsKey(null)).isFalse();
    assertThat(nonEmptyMap.containsKey(null)).isFalse();
  }

  // Test non-comparable key handling
  
  @Test
  public void testPutNonComparableKey_throwsClassCastException() {
    // Given: A LinkedTreeMap expecting comparable keys
    LinkedTreeMap<Object, String> map = new LinkedTreeMap<>();
    
    // When/Then: Putting a non-comparable key should throw ClassCastException
    assertThrows(ClassCastException.class, () -> map.put(new Object(), VALUE_ANDROID));
  }

  @Test
  public void testContainsNonComparableKey_returnsFalse() {
    // Given: A LinkedTreeMap with a string key
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    map.put(KEY_A, VALUE_ANDROID);
    
    // When/Then: Checking for a non-comparable key should return false (not throw exception)
    assertThat(map).doesNotContainKey(new Object());
  }

  // Test null value handling with default configuration (allows nulls)
  
  @Test
  public void testPutNullValue_allowedByDefault() {
    // Given: A default LinkedTreeMap (allows null values)
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    
    // When: We put a null value
    map.put(KEY_A, null);
    
    // Then: The null value should be stored correctly
    assertThat(map).hasSize(1);
    assertThat(map.containsKey(KEY_A)).isTrue();
    assertThat(map.containsValue(null)).isTrue();
    assertThat(map.get(KEY_A)).isNull();
  }

  @Test
  public void testEntrySetValue_canSetToNull() {
    // Given: A LinkedTreeMap with a non-null value
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    map.put(KEY_A, "original_value");
    
    // When: We set the value to null via entry set
    Entry<String, String> entry = map.entrySet().iterator().next();
    assertThat(entry.getValue()).isEqualTo("original_value");
    entry.setValue(null);
    
    // Then: The value should be updated to null
    assertThat(entry.getValue()).isNull();
    assertThat(map.containsKey(KEY_A)).isTrue();
    assertThat(map.containsValue(null)).isTrue();
    assertThat(map.get(KEY_A)).isNull();
  }

  // Test null value handling with explicit configuration (forbids nulls)
  
  @Test
  public void testPutNullValue_forbiddenWhenConfigured() {
    // Given: A LinkedTreeMap configured to forbid null values
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(false);
    
    // When/Then: Attempting to put null value should throw NPE
    NullPointerException exception = assertThrows(
        NullPointerException.class, 
        () -> map.put(KEY_A, null)
    );
    assertThat(exception).hasMessageThat().isEqualTo("value == null");
    
    // And: The map should remain empty
    assertThat(map).hasSize(0);
    assertThat(map).doesNotContainKey(KEY_A);
    assertThat(map.containsValue(null)).isFalse();
  }

  @Test
  public void testEntrySetValue_forbiddenWhenConfigured() {
    // Given: A LinkedTreeMap that forbids null values, with one entry
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(false);
    map.put(KEY_A, "original_value");
    Entry<String, String> entry = map.entrySet().iterator().next();
    
    // When/Then: Attempting to set value to null should throw NPE
    NullPointerException exception = assertThrows(
        NullPointerException.class, 
        () -> entry.setValue(null)
    );
    assertThat(exception).hasMessageThat().isEqualTo("value == null");
    
    // And: The original value should be preserved
    assertThat(entry.getValue()).isEqualTo("original_value");
    assertThat(map.get(KEY_A)).isEqualTo("original_value");
    assertThat(map.containsValue(null)).isFalse();
  }

  // Test basic map operations
  
  @Test
  public void testPutAndGet_overridesPreviousValue() {
    // Given: An empty LinkedTreeMap
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    
    // When: We put initial values
    assertThat(map.put("d", "donut")).isNull();
    assertThat(map.put("e", "eclair")).isNull();
    assertThat(map.put("f", "froyo")).isNull();
    assertThat(map).hasSize(3);
    
    // And: We override an existing value
    assertThat(map.get("d")).isEqualTo("donut");
    assertThat(map.put("d", "done")).isEqualTo("donut"); // Returns old value
    
    // Then: Size should remain the same, value should be updated
    assertThat(map).hasSize(3);
    assertThat(map.get("d")).isEqualTo("done");
  }

  @Test
  public void testEmptyStringValues_handledCorrectly() {
    // Given: A LinkedTreeMap with an empty string value
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    map.put(KEY_A, "");
    
    // Then: Empty string should be treated as a valid value
    assertThat(map.containsKey(KEY_A)).isTrue();
    assertThat(map.get(KEY_A)).isEqualTo("");
  }

  @Test
  public void testClear_removesAllEntries() {
    // Given: A LinkedTreeMap with multiple entries
    LinkedTreeMap<String, String> map = createMapWithThreeEntries();
    
    // When: We clear the map
    map.clear();
    
    // Then: The map should be empty
    assertThat(map.keySet()).isEmpty();
    assertThat(map).isEmpty();
  }

  // Test performance and stress scenarios
  
  @Test
  public void testLargeNumberOfRandomKeys_maintainsCorrectness() {
    // Given: A large set of random keys with fixed seed for reproducibility
    Random random = new Random(1367593214724L);
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    String[] keys = new String[1000];
    
    // When: We insert 1000 random key-value pairs
    for (int i = 0; i < keys.length; i++) {
      keys[i] = Integer.toString(random.nextInt(), 36) + "-" + i;
      map.put(keys[i], String.valueOf(i));
    }
    
    // Then: All keys should be retrievable with correct values
    for (int i = 0; i < keys.length; i++) {
      String key = keys[i];
      assertThat(map.containsKey(key)).isTrue();
      assertThat(map.get(key)).isEqualTo(String.valueOf(i));
    }
  }

  // Test equality and hashing
  
  @Test
  public void testEqualsAndHashCode_independentOfInsertionOrder() {
    // Given: Two LinkedTreeMaps with same content but different insertion order
    LinkedTreeMap<String, Integer> firstMap = new LinkedTreeMap<>();
    firstMap.put("A", 1);
    firstMap.put("B", 2);
    firstMap.put("C", 3);
    firstMap.put("D", 4);

    LinkedTreeMap<String, Integer> secondMap = new LinkedTreeMap<>();
    secondMap.put("C", 3);  // Different insertion order
    secondMap.put("B", 2);
    secondMap.put("D", 4);
    secondMap.put("A", 1);

    // Then: Maps should be equal and have same hash code
    MoreAsserts.assertEqualsAndHashCode(firstMap, secondMap);
  }

  // Test serialization
  
  @Test
  public void testSerialization_preservesMapContent() throws IOException, ClassNotFoundException {
    // Given: A LinkedTreeMap with content
    Map<String, Integer> originalMap = new LinkedTreeMap<>();
    originalMap.put("a", 1);
    
    // When: We serialize and deserialize the map
    byte[] serializedData = serializeMap(originalMap);
    Map<String, Integer> deserializedMap = deserializeMap(serializedData);
    
    // Then: Content should be preserved
    assertThat(deserializedMap).isEqualTo(Collections.singletonMap("a", 1));
  }

  // Helper methods for better test organization
  
  private LinkedTreeMap<String, String> createMapWithThreeEntries() {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    map.put(KEY_A, VALUE_ANDROID);
    map.put(KEY_C, VALUE_COLA);
    map.put(KEY_B, VALUE_BBQ);
    return map;
  }
  
  private byte[] serializeMap(Map<String, Integer> map) throws IOException {
    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    try (ObjectOutputStream objectStream = new ObjectOutputStream(byteStream)) {
      objectStream.writeObject(map);
    }
    return byteStream.toByteArray();
  }
  
  @SuppressWarnings("unchecked")
  private Map<String, Integer> deserializeMap(byte[] data) throws IOException, ClassNotFoundException {
    try (ObjectInputStream objectStream = new ObjectInputStream(new ByteArrayInputStream(data))) {
      return (Map<String, Integer>) objectStream.readObject();
    }
  }
}