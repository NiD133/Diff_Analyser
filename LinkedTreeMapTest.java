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
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import org.junit.Test;

public final class LinkedTreeMapTest {

  @Test
  public void iteration_maintainsInsertionOrder() {
    // Arrange
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    map.put("a", "android");
    map.put("c", "cola");
    map.put("b", "bbq");

    // Act & Assert
    assertThat(map.keySet()).containsExactly("a", "c", "b").inOrder();
    assertThat(map.values()).containsExactly("android", "cola", "bbq").inOrder();
  }

  @Test
  public void iteratorRemove_afterIteratingToEnd_succeeds() {
    // This test addresses a past bug where removing the last-iterated element
    // could cause an error due to incorrect node unlinking.

    // Arrange
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    map.put("a", "android");
    map.put("c", "cola");
    map.put("b", "bbq"); // The element to be removed

    Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();

    // Act
    // Iterate to the end of the map
    iterator.next(); // a
    iterator.next(); // c
    iterator.next(); // b
    iterator.remove(); // Remove the last element returned by next(), which is "b"

    // Assert
    assertThat(map.keySet()).containsExactly("a", "c").inOrder();
  }

  @Test
  public void put_withNullKey_throwsNullPointerException() {
    // Arrange
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();

    // Act & Assert
    NullPointerException e =
        assertThrows(NullPointerException.class, () -> map.put(null, "android"));
    assertThat(e).hasMessageThat().isEqualTo("key == null");
  }

  @Test
  public void put_withNonComparableKey_throwsClassCastException() {
    // Arrange
    // The map uses natural ordering by default, but Object does not implement Comparable.
    LinkedTreeMap<Object, String> map = new LinkedTreeMap<>();

    // Act & Assert
    assertThrows(ClassCastException.class, () -> map.put(new Object(), "android"));
  }

  @Test
  public void put_withNullValue_isAllowedByDefault() {
    // Arrange
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();

    // Act
    map.put("a", null);

    // Assert
    assertThat(map).hasSize(1);
    assertThat(map.get("a")).isNull();
    assertThat(map.containsKey("a")).isTrue();
    assertThat(map.containsValue(null)).isTrue();
  }

  @Test
  public void put_withNullValue_throwsExceptionWhenDisallowed() {
    // Arrange
    // Create a map that does not allow null values.
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(/* allowNullValues= */ false);

    // Act & Assert for exception
    NullPointerException e = assertThrows(NullPointerException.class, () -> map.put("a", null));
    assertThat(e).hasMessageThat().isEqualTo("value == null");

    // Assert that the map remains unchanged.
    assertThat(map).isEmpty();
  }

  @Test
  public void entrySetSetValue_toNull_isAllowedByDefault() {
    // Arrange
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    map.put("a", "1");
    Entry<String, String> entry = map.entrySet().iterator().next();
    assertThat(entry.getValue()).isEqualTo("1");

    // Act
    entry.setValue(null);

    // Assert
    assertThat(entry.getValue()).isNull();
    assertThat(map.get("a")).isNull();
    assertThat(map.containsValue(null)).isTrue();
  }

  @Test
  public void entrySetSetValue_toNull_throwsExceptionWhenDisallowed() {
    // Arrange
    // Create a map that does not allow null values.
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(/* allowNullValues= */ false);
    map.put("a", "1");
    Entry<String, String> entry = map.entrySet().iterator().next();

    // Act & Assert for exception
    NullPointerException e = assertThrows(NullPointerException.class, () -> entry.setValue(null));
    assertThat(e).hasMessageThat().isEqualTo("value == null");

    // Assert that the entry and map remain unchanged.
    assertThat(entry.getValue()).isEqualTo("1");
    assertThat(map.get("a")).isEqualTo("1");
    assertThat(map.containsValue(null)).isFalse();
  }

  @Test
  public void containsKey_withNonComparableType_returnsFalse() {
    // Arrange
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    map.put("a", "android");

    // Act & Assert
    // `containsKey` should not throw ClassCastException, but return false.
    assertThat(map.containsKey(new Object())).isFalse();
  }

  @Test
  public void containsKey_withNullKey_returnsFalse() {
    // Arrange
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();

    // Assert on empty map
    assertThat(map.containsKey(null)).isFalse();

    // Arrange for non-empty map
    map.put("a", "android");

    // Assert on non-empty map
    assertThat(map.containsKey(null)).isFalse();
  }

  @Test
  public void put_withExistingKey_overwritesValueAndReturnsOldValue() {
    // Arrange
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    map.put("d", "donut");
    map.put("e", "eclair");
    assertThat(map.get("d")).isEqualTo("donut");
    assertThat(map).hasSize(2);

    // Act
    String oldValue = map.put("d", "done");

    // Assert
    assertThat(oldValue).isEqualTo("donut");
    assertThat(map.get("d")).isEqualTo("done");
    assertThat(map).hasSize(2); // Size should not change
  }

  @Test
  public void put_withEmptyStringValue_succeeds() {
    // Arrange
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();

    // Act
    map.put("a", "");

    // Assert
    assertThat(map.get("a")).isEqualTo("");
    assertThat(map.containsKey("a")).isTrue();
  }

  @Test
  public void putAndGet_withManyRandomKeys_succeeds() {
    // Arrange
    // Use a fixed seed for reproducible tests.
    Random random = new Random(1367593214724L);
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    int mapSize = 1000;
    String[] keys = new String[mapSize];

    // Act: Populate the map with many random entries.
    for (int i = 0; i < mapSize; i++) {
      // Generate a unique random key.
      keys[i] = Integer.toString(random.nextInt(), 36) + "-" + i;
      map.put(keys[i], String.valueOf(i));
    }

    // Assert: Verify that all entries can be retrieved correctly.
    assertThat(map).hasSize(mapSize);
    for (int i = 0; i < mapSize; i++) {
      String key = keys[i];
      assertThat(map.get(key)).isEqualTo(String.valueOf(i));
    }
  }

  @Test
  public void clear_removesAllMappings() {
    // Arrange
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    map.put("a", "android");
    map.put("c", "cola");
    map.put("b", "bbq");

    // Act
    map.clear();

    // Assert
    assertThat(map).isEmpty();
  }

  @Test
  public void equalsAndHashCode_areBasedOnContentNotInsertionOrder() {
    // Arrange
    LinkedTreeMap<String, Integer> map1 = new LinkedTreeMap<>();
    map1.put("A", 1);
    map1.put("B", 2);
    map1.put("C", 3);

    // Create a second map with the same content but different insertion order.
    LinkedTreeMap<String, Integer> map2 = new LinkedTreeMap<>();
    map2.put("C", 3);
    map2.put("A", 1);
    map2.put("B", 2);

    // Act & Assert
    // `AbstractMap`'s `equals` and `hashCode` are based on the entry set,
    // so the two maps should be considered equal.
    MoreAsserts.assertEqualsAndHashCode(map1, map2);
  }

  @Test
  public void serialization_deserializesToEquivalentMap() throws IOException, ClassNotFoundException {
    // Arrange
    LinkedTreeMap<String, Integer> originalMap = new LinkedTreeMap<>();
    originalMap.put("a", 1);
    originalMap.put("b", 2);

    // Act
    byte[] serializedForm = serialize(originalMap);
    @SuppressWarnings("unchecked")
    Map<String, Integer> deserializedMap = (Map<String, Integer>) deserialize(serializedForm);

    // Assert
    // The deserialized object is a LinkedHashMap, not a LinkedTreeMap, due to `writeReplace`.
    // We just need to check for map equivalence.
    assertThat(deserializedMap).isEqualTo(originalMap);
    assertThat(deserializedMap).isInstanceOf(java.util.LinkedHashMap.class);
  }

  /** Helper method to serialize an object to a byte array. */
  private static byte[] serialize(Object object) throws IOException {
    ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
    try (ObjectOutputStream objOut = new ObjectOutputStream(byteOut)) {
      objOut.writeObject(object);
    }
    return byteOut.toByteArray();
  }

  /** Helper method to deserialize an object from a byte array. */
  private static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
    ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
    try (ObjectInputStream objIn = new ObjectInputStream(byteIn)) {
      return objIn.readObject();
    }
  }
}