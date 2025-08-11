package com.google.gson.internal;

import static org.junit.Assert.*;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

/**
 * Readable, behavior-focused tests for LinkedTreeMap.
 *
 * These tests target the public Map API exposed by LinkedTreeMap and verify:
 * - Basic map semantics (put/get/size/remove/clear)
 * - Insertion-order iteration (unlike TreeMap)
 * - View behavior for entrySet() and keySet()
 * - Handling of null keys/values according to constructor options
 * - Default Map helpers (putIfAbsent, putAll, merge)
 *
 * The tests avoid internal implementation details so they are easier to maintain.
 */
public class LinkedTreeMapReadableTest {

  // ---------- Basic behavior ----------

  @Test
  public void newMap_hasSizeZero() {
    LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
    assertEquals(0, map.size());
    assertNull(map.get("missing"));
    assertFalse(map.containsKey("missing"));
  }

  @Test
  public void putAndGet_allowsNullValuesByDefault() {
    LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>(); // allows null values
    map.put("a", null);
    assertTrue(map.containsKey("a"));
    assertNull(map.get("a"));
    assertEquals(1, map.size());
  }

  @Test
  public void nullKey_disallowedWithNaturalOrder() {
    LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>(); // natural order by default
    try {
      map.put(null, 1);
      fail("Expected NullPointerException for null key");
    } catch (NullPointerException npe) {
      // Implementation uses message "key == null"
      assertTrue(npe.getMessage() == null || npe.getMessage().contains("key == null"));
    }
  }

  @Test
  public void disallowNullValues_constructorThrowsOnNullValue() {
    LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>(false); // disallow null values
    try {
      map.put("k", null);
      fail("Expected NullPointerException for null value");
    } catch (NullPointerException npe) {
      // Implementation uses message "value == null"
      assertTrue(npe.getMessage() == null || npe.getMessage().contains("value == null"));
    }
  }

  // ---------- Iteration order (insertion order) ----------

  @Test
  public void iterationOrder_isInsertionOrder() {
    LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
    map.put("b", 2);
    map.put("a", 1);
    map.put("c", 3);

    assertEquals(Arrays.asList("b", "a", "c"), keysInIterationOrder(map));
  }

  @Test
  public void iterationOrder_isInsertionOrder_evenWithCustomComparator() {
    Comparator<Integer> reverse = Comparator.reverseOrder(); // comparator should not affect iteration order
    LinkedTreeMap<Integer, String> map = new LinkedTreeMap<>(reverse, true);
    map.put(2, "two");
    map.put(1, "one");
    map.put(3, "three");

    assertEquals(Arrays.asList(2, 1, 3), keysInIterationOrder(map));
  }

  // ---------- Map operations ----------

  @Test
  public void containsKey_falseForMissingOrDifferentType() {
    LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
    map.put(1, 1);

    assertFalse(map.containsKey(2));
    assertFalse(map.containsKey(new Object()));
  }

  @Test
  public void remove_returnsPreviousValue_andShrinks() {
    LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
    map.put("a", 1);

    Integer removed = map.remove("a");
    assertEquals(Integer.valueOf(1), removed);
    assertEquals(0, map.size());
    assertFalse(map.containsKey("a"));
  }

  @Test
  public void clear_emptiesMap_andViews() {
    LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
    map.put("a", 1);
    map.put("b", 2);

    Set<String> keys = map.keySet();
    Set<Map.Entry<String, Integer>> entries = map.entrySet();

    assertFalse(keys.isEmpty());
    assertFalse(entries.isEmpty());

    map.clear();

    assertEquals(0, map.size());
    assertTrue(keys.isEmpty());
    assertTrue(entries.isEmpty());
  }

  // ---------- Views: keySet ----------

  @Test
  public void keySet_remove_removesEntry_andReturnsTrue() {
    LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
    map.put("x", 42);

    assertTrue(map.keySet().remove("x"));
    assertEquals(0, map.size());
    assertFalse(map.containsKey("x"));
  }

  @Test
  public void keySet_remove_nonExisting_returnsFalse() {
    LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
    map.put("x", 42);

    assertFalse(map.keySet().remove("y"));
    assertEquals(1, map.size());
  }

  @Test
  public void keySet_isBackedByMap() {
    LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
    map.put("a", 1);
    Set<String> keys = map.keySet();

    assertTrue(keys.contains("a"));
    map.remove("a");
    assertFalse(keys.contains("a"));
  }

  // ---------- Views: entrySet ----------

  @Test
  public void entrySet_contains_existingEntry_byKeyAndValue() {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    map.put("k", "v");

    assertTrue(map.entrySet().contains(new SimpleEntry<>("k", "v")));
    assertFalse(map.entrySet().contains(new SimpleEntry<>("k", "other")));
  }

  @Test
  public void entrySet_remove_existingEntry_removesAndReturnsTrue() {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    map.put("k", "v");

    assertTrue(map.entrySet().remove(new SimpleEntry<>("k", "v")));
    assertEquals(0, map.size());
    assertFalse(map.containsKey("k"));
  }

  @Test
  public void entrySet_iterator_remove_removesLastReturned() {
    LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
    map.put("a", 1);
    map.put("b", 2);

    Iterator<Map.Entry<String, Integer>> it = map.entrySet().iterator();
    assertTrue(it.hasNext());
    it.next();
    it.remove();

    assertEquals(1, map.size());
    assertFalse(map.containsKey("a"));
    assertTrue(map.containsKey("b"));
  }

  // ---------- Default Map helpers ----------

  @Test
  public void putIfAbsent_doesNotOverrideExisting() {
    LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
    map.put("a", "first");

    String existing = map.putIfAbsent("a", "second");
    assertEquals("first", existing);
    assertEquals("first", map.get("a"));
    assertEquals(1, map.size());
  }

  @Test
  public void putAll_fromSelf_isNoOp() {
    LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
    map.put("a", 1);

    map.putAll(map); // no-op
    assertEquals(1, map.size());
    assertEquals(Integer.valueOf(1), map.get("a"));
  }

  @Test
  public void merge_insertsWhenAbsent_andRemapsWhenPresent() {
    LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();

    // Absent -> insert
    map.merge("a", 1, (oldV, newV) -> oldV + newV);
    assertEquals(Integer.valueOf(1), map.get("a"));

    // Present -> remap
    map.merge("a", 2, (oldV, newV) -> oldV + newV);
    assertEquals(Integer.valueOf(3), map.get("a"));
  }

  // ---------- Comparator behavior around null keys ----------

  @Test
  public void customComparator_canAllowNullKey() {
    Comparator<String> nullsFirst = (a, b) -> {
      if (a == b) return 0;
      if (a == null) return -1;
      if (b == null) return 1;
      return a.compareTo(b);
    };
    LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>(nullsFirst, true);

    map.put(null, 0);     // allowed by comparator
    map.put("a", 1);

    assertTrue(map.containsKey(null));
    assertEquals(Integer.valueOf(0), map.get(null));
    assertEquals(Arrays.asList(null, "a"), keysInIterationOrder(map)); // insertion order
  }

  // ---------- Helpers ----------

  private static <K, V> List<K> keysInIterationOrder(Map<K, V> map) {
    List<K> keys = new ArrayList<>();
    for (Map.Entry<K, V> e : map.entrySet()) {
      keys.add(e.getKey());
    }
    return keys;
  }
}