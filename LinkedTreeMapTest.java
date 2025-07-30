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

public final class LinkedTreeMapTest {

  @Test
  public void testIterationOrder() {
    // Test that the iteration order is the same as the insertion order
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    map.put("a", "android");
    map.put("c", "cola");
    map.put("b", "bbq");

    assertThat(map.keySet()).containsExactly("a", "c", "b").inOrder();
    assertThat(map.values()).containsExactly("android", "cola", "bbq").inOrder();
  }

  @Test
  public void testRemoveRootDoesNotDoubleUnlink() {
    // Test that removing the root does not cause double unlinking
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    map.put("a", "android");
    map.put("c", "cola");
    map.put("b", "bbq");

    Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
    iterator.next(); // "a"
    iterator.next(); // "c"
    iterator.next(); // "b"
    iterator.remove(); // Remove "b"

    assertThat(map.keySet()).containsExactly("a", "c").inOrder();
  }

  @Test
  public void testPutNullKeyFails() {
    // Test that putting a null key throws NullPointerException
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    NullPointerException exception = assertThrows(NullPointerException.class, () -> map.put(null, "android"));
    assertThat(exception).hasMessageThat().isEqualTo("key == null");
  }

  @Test
  public void testPutNonComparableKeyFails() {
    // Test that putting a non-comparable key throws ClassCastException
    LinkedTreeMap<Object, String> map = new LinkedTreeMap<>();
    assertThrows(ClassCastException.class, () -> map.put(new Object(), "android"));
  }

  @Test
  public void testPutNullValue() {
    // Test that putting a null value is allowed
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    map.put("a", null);

    assertThat(map).hasSize(1);
    assertThat(map.containsKey("a")).isTrue();
    assertThat(map.containsValue(null)).isTrue();
    assertThat(map.get("a")).isNull();
  }

  @Test
  public void testPutNullValue_Forbidden() {
    // Test that putting a null value is forbidden when specified
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(false);
    NullPointerException exception = assertThrows(NullPointerException.class, () -> map.put("a", null));
    assertThat(exception).hasMessageThat().isEqualTo("value == null");

    assertThat(map).hasSize(0);
    assertThat(map).doesNotContainKey("a");
    assertThat(map.containsValue(null)).isFalse();
  }

  @Test
  public void testEntrySetValueNull() {
    // Test that setting an entry's value to null is allowed
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    map.put("a", "1");
    Entry<String, String> entry = map.entrySet().iterator().next();
    entry.setValue(null);

    assertThat(map.containsKey("a")).isTrue();
    assertThat(map.containsValue(null)).isTrue();
    assertThat(map.get("a")).isNull();
  }

  @Test
  public void testEntrySetValueNull_Forbidden() {
    // Test that setting an entry's value to null is forbidden when specified
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>(false);
    map.put("a", "1");
    Entry<String, String> entry = map.entrySet().iterator().next();
    NullPointerException exception = assertThrows(NullPointerException.class, () -> entry.setValue(null));
    assertThat(exception).hasMessageThat().isEqualTo("value == null");

    assertThat(entry.getValue()).isEqualTo("1");
    assertThat(map.get("a")).isEqualTo("1");
    assertThat(map.containsValue(null)).isFalse();
  }

  @Test
  public void testContainsNonComparableKeyReturnsFalse() {
    // Test that containsKey returns false for non-comparable keys
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    map.put("a", "android");
    assertThat(map).doesNotContainKey(new Object());
  }

  @Test
  public void testContainsNullKeyIsAlwaysFalse() {
    // Test that containsKey returns false for null keys
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    assertThat(map.containsKey(null)).isFalse();
    map.put("a", "android");
    assertThat(map.containsKey(null)).isFalse();
  }

  @Test
  public void testPutOverrides() {
    // Test that put overrides existing values
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    assertThat(map.put("d", "donut")).isNull();
    assertThat(map.put("e", "eclair")).isNull();
    assertThat(map.put("f", "froyo")).isNull();
    assertThat(map).hasSize(3);

    assertThat(map.get("d")).isEqualTo("donut");
    assertThat(map.put("d", "done")).isEqualTo("donut");
    assertThat(map).hasSize(3);
  }

  @Test
  public void testEmptyStringValues() {
    // Test that empty string values are handled correctly
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    map.put("a", "");
    assertThat(map.containsKey("a")).isTrue();
    assertThat(map.get("a")).isEqualTo("");
  }

  @Test
  public void testLargeSetOfRandomKeys() {
    // Test the map with a large set of random keys
    Random random = new Random(1367593214724L);
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    String[] keys = new String[1000];
    for (int i = 0; i < keys.length; i++) {
      keys[i] = Integer.toString(random.nextInt(), 36) + "-" + i;
      map.put(keys[i], "" + i);
    }

    for (int i = 0; i < keys.length; i++) {
      String key = keys[i];
      assertThat(map.containsKey(key)).isTrue();
      assertThat(map.get(key)).isEqualTo("" + i);
    }
  }

  @Test
  public void testClear() {
    // Test that clear removes all entries
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    map.put("a", "android");
    map.put("c", "cola");
    map.put("b", "bbq");
    map.clear();
    assertThat(map.keySet()).isEmpty();
    assertThat(map).isEmpty();
  }

  @Test
  public void testEqualsAndHashCode() {
    // Test that two maps with the same entries are equal and have the same hash code
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

    MoreAsserts.assertEqualsAndHashCode(map1, map2);
  }

  @Test
  public void testJavaSerialization() throws IOException, ClassNotFoundException {
    // Test that the map can be serialized and deserialized correctly
    ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();
    ObjectOutputStream objectOutStream = new ObjectOutputStream(byteOutStream);
    Map<String, Integer> map = new LinkedTreeMap<>();
    map.put("a", 1);
    objectOutStream.writeObject(map);
    objectOutStream.close();

    ObjectInputStream objectInStream = new ObjectInputStream(new ByteArrayInputStream(byteOutStream.toByteArray()));
    @SuppressWarnings("unchecked")
    Map<String, Integer> deserializedMap = (Map<String, Integer>) objectInStream.readObject();
    assertThat(deserializedMap).isEqualTo(Collections.singletonMap("a", 1));
  }
}