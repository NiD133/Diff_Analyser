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
 * Tests for LinkedTreeMap focusing on understandability:
 * - Descriptive test names explain behavior under test
 * - AAA (Arrange-Act-Assert) structure
 * - Small helper methods encapsulate repeated setup
 * - Explicit types and no "magic values" in tests
 */
public final class LinkedTreeMapTest {

  private static LinkedTreeMap<String, String> newMap() {
    return new LinkedTreeMap<>();
  }

  private static LinkedTreeMap<String, String> newMapDisallowingNullValues() {
    return new LinkedTreeMap<>(false);
  }

  private static <K, V> Map<K, V> javaDeserialize(byte[] bytes)
      throws IOException, ClassNotFoundException {
    try (ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
      @SuppressWarnings("unchecked")
      Map<K, V> result = (Map<K, V>) in.readObject();
      return result;
    }
  }

  private static byte[] javaSerialize(Object o) throws IOException {
    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out)) {
      objOut.writeObject(o);
      objOut.flush();
      return out.toByteArray();
    }
  }

  @Test
  public void iterationOrder_insertionOrderPreserved() {
    // Arrange
    LinkedTreeMap<String, String> map = newMap();
    map.put("a", "android");
    map.put("c", "cola");
    map.put("b", "bbq");

    // Assert
    assertThat(map.keySet()).containsExactly("a", "c", "b").inOrder();
    assertThat(map.values()).containsExactly("android", "cola", "bbq").inOrder();
  }

  @Test
  public void iteratorRemove_afterIteratingOverAllEntries_removesOnlyOnce() {
    // Arrange
    LinkedTreeMap<String, String> map = newMap();
    map.put("a", "android");
    map.put("c", "cola");
    map.put("b", "bbq");

    // Act: iterate to the end, then remove the last returned entry
    Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
    it.next(); // a
    it.next(); // c
    it.next(); // b
    it.remove(); // remove "b"

    // Assert
    assertThat(map.keySet()).containsExactly("a", "c").inOrder();
  }

  @Test
  public void put_nullKey_throwsNPEWithMessage() {
    // Arrange
    LinkedTreeMap<String, String> map = newMap();

    // Act + Assert
    NullPointerException e =
        assertThrows(NullPointerException.class, () -> map.put(null, "android"));
    assertThat(e).hasMessageThat().isEqualTo("key == null");
  }

  @Test
  public void put_nonComparableKey_throwsClassCastException() {
    // Arrange
    LinkedTreeMap<Object, String> map = new LinkedTreeMap<>();

    // Act + Assert
    assertThrows(ClassCastException.class, () -> map.put(new Object(), "android"));
  }

  @Test
  public void put_nullValue_allowedByDefault() {
    // Arrange
    LinkedTreeMap<String, String> map = newMap();

    // Act
    map.put("a", null);

    // Assert
    assertThat(map).hasSize(1);
    assertThat(map.containsKey("a")).isTrue();
    assertThat(map.containsValue(null)).isTrue();
    assertThat(map.get("a")).isNull();
  }

  @Test
  public void put_nullValue_disallowedWhenConfigured() {
    // Arrange
    LinkedTreeMap<String, String> map = newMapDisallowingNullValues();

    // Act + Assert
    NullPointerException e = assertThrows(NullPointerException.class, () -> map.put("a", null));
    assertThat(e).hasMessageThat().isEqualTo("value == null");

    assertThat(map).hasSize(0);
    assertThat(map).doesNotContainKey("a");
    assertThat(map.containsValue(null)).isFalse();
  }

  @Test
  public void entrySet_setValueToNull_allowedByDefault() {
    // Arrange
    LinkedTreeMap<String, String> map = newMap();
    map.put("a", "1");
    Entry<String, String> entry = map.entrySet().iterator().next();

    // Precondition
    assertThat(entry.getKey()).isEqualTo("a");
    assertThat(entry.getValue()).isEqualTo("1");

    // Act
    entry.setValue(null);

    // Assert
    assertThat(entry.getValue()).isNull();
    assertThat(map.containsKey("a")).isTrue();
    assertThat(map.containsValue(null)).isTrue();
    assertThat(map.get("a")).isNull();
  }

  @Test
  public void entrySet_setValueToNull_disallowedWhenConfigured() {
    // Arrange
    LinkedTreeMap<String, String> map = newMapDisallowingNullValues();
    map.put("a", "1");
    Entry<String, String> entry = map.entrySet().iterator().next();

    // Act + Assert
    NullPointerException e = assertThrows(NullPointerException.class, () -> entry.setValue(null));
    assertThat(e).hasMessageThat().isEqualTo("value == null");

    assertThat(entry.getValue()).isEqualTo("1");
    assertThat(map.get("a")).isEqualTo("1");
    assertThat(map.containsValue(null)).isFalse();
  }

  @Test
  public void containsKey_withNonComparableKey_returnsFalse() {
    // Arrange
    LinkedTreeMap<String, String> map = newMap();
    map.put("a", "android");

    // Act + Assert
    assertThat(map).doesNotContainKey(new Object());
  }

  @Test
  public void containsKey_withNullKey_returnsFalseAlways() {
    // Arrange
    LinkedTreeMap<String, String> map = newMap();

    // Act + Assert
    assertThat(map.containsKey(null)).isFalse();
    map.put("a", "android");
    assertThat(map.containsKey(null)).isFalse();
  }

  @Test
  public void put_sameKey_overridesAndReturnsPreviousValue() {
    // Arrange
    LinkedTreeMap<String, String> map = newMap();

    // Act + Assert
    assertThat(map.put("d", "donut")).isNull();
    assertThat(map.put("e", "eclair")).isNull();
    assertThat(map.put("f", "froyo")).isNull();
    assertThat(map).hasSize(3);

    assertThat(map.get("d")).isEqualTo("donut");
    assertThat(map.put("d", "done")).isEqualTo("donut"); // returns previous
    assertThat(map).hasSize(3); // size unchanged
  }

  @Test
  public void put_emptyStringValue_supported() {
    // Arrange
    LinkedTreeMap<String, String> map = newMap();

    // Act
    map.put("a", "");

    // Assert
    assertThat(map.containsKey("a")).isTrue();
    assertThat(map.get("a")).isEqualTo("");
  }

  @Test
  public void randomKeys_putAndGet_roundTrip() {
    // Arrange
    final long seed = 1367593214724L;
    final int entryCount = 1000;
    Random random = new Random(seed);
    LinkedTreeMap<String, String> map = newMap();

    String[] keys = new String[entryCount];
    for (int i = 0; i < entryCount; i++) {
      keys[i] = Integer.toString(random.nextInt(), 36) + "-" + i;
      map.put(keys[i], String.valueOf(i));
    }

    // Assert
    for (int i = 0; i < entryCount; i++) {
      String key = keys[i];
      assertThat(map.containsKey(key)).isTrue();
      assertThat(map.get(key)).isEqualTo(String.valueOf(i));
    }
  }

  @Test
  public void clear_removesAllEntriesAndIterationOrder() {
    // Arrange
    LinkedTreeMap<String, String> map = newMap();
    map.put("a", "android");
    map.put("c", "cola");
    map.put("b", "bbq");

    // Act
    map.clear();

    // Assert
    assertThat(map.keySet()).isEmpty();
    assertThat(map).isEmpty();
  }

  @Test
  public void equalsAndHashCode_consistentForDifferentInsertionOrders() {
    // Arrange
    LinkedTreeMap<String, Integer> map1 = new LinkedTreeMap<>();
    map1.put("A", 1);
    map1.put("B", 2);
    map1.put("C", 3);
    map1.put("D", 4);

    LinkedTreeMap<String, Integer> map2 = new LinkedTreeMap<>();
    map2.put("C", 3);
    map2.put("B", 2);
    map2.put("D", 4);
    map2.put("A", 1);

    // Assert
    MoreAsserts.assertEqualsAndHashCode(map1, map2);
  }

  @Test
  public void javaSerialization_roundTrip() throws IOException, ClassNotFoundException {
    // Arrange
    Map<String, Integer> map = new LinkedTreeMap<>();
    map.put("a", 1);

    // Act
    byte[] bytes = javaSerialize(map);
    Map<String, Integer> deserialized = javaDeserialize(bytes);

    // Assert
    assertThat(deserialized).isEqualTo(Collections.singletonMap("a", 1));
  }
}