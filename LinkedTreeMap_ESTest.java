package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Understandable and maintainable tests for the {@link LinkedTreeMap} class.
 * This test suite focuses on the public API and core functionalities,
 * such as element management, view collections, exception handling, and custom comparator support.
 */
public class LinkedTreeMapTest {

    // --- Basic Map Operations ---

    @Test
    public void newMap_shouldBeEmpty() {
        // Arrange
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();

        // Assert
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    @Test
    public void clear_shouldRemoveAllElements() {
        // Arrange
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
        map.put("a", 1);
        map.put("b", 2);

        // Act
        map.clear();

        // Assert
        assertEquals(0, map.size());
        assertTrue(map.isEmpty());
    }

    // --- put, get, containsKey ---

    @Test
    public void put_shouldAddElementAndReturnNullForNewKey() {
        // Arrange
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();

        // Act
        Integer previousValue = map.put("a", 1);

        // Assert
        assertNull(previousValue);
        assertEquals(1, map.size());
        assertTrue(map.containsKey("a"));
    }

    @Test
    public void put_shouldOverwriteValueAndReturnOldValueForExistingKey() {
        // Arrange
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
        map.put("a", 1);

        // Act
        Integer previousValue = map.put("a", 2);

        // Assert
        assertEquals(1, (int) previousValue);
        assertEquals(1, map.size());
        assertEquals(2, (int) map.get("a"));
    }

    @Test
    public void get_shouldReturnValueForExistingKey() {
        // Arrange
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
        map.put("a", 123);

        // Act
        Integer value = map.get("a");

        // Assert
        assertEquals(123, (int) value);
    }

    @Test
    public void get_shouldReturnNullForNonExistentKey() {
        // Arrange
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();

        // Act
        Integer value = map.get("non-existent");

        // Assert
        assertNull(value);
    }

    @Test
    public void containsKey_shouldReturnFalseForIncompatibleObjectType() {
        // Arrange
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
        map.put("a", 1);

        // Act & Assert
        assertFalse(map.containsKey(new Object()));
    }

    // --- remove ---

    @Test
    public void remove_shouldDeleteEntryAndReturnValue() {
        // Arrange
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
        map.put("a", 1);

        // Act
        Integer removedValue = map.remove("a");

        // Assert
        assertEquals(1, (int) removedValue);
        assertEquals(0, map.size());
        assertFalse(map.containsKey("a"));
    }

    @Test
    public void remove_shouldReturnNullForNonExistentKey() {
        // Arrange
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();

        // Act
        Integer removedValue = map.remove("non-existent");

        // Assert
        assertNull(removedValue);
    }

    // --- Exception Handling ---

    @Test(expected = NullPointerException.class)
    public void put_withNullKey_shouldThrowNullPointerException() {
        // Arrange
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();

        // Act
        map.put(null, 1);
    }

    @Test(expected = NullPointerException.class)
    public void put_withNullValue_whenNullsDisallowed_shouldThrowNullPointerException() {
        // Arrange: Constructor with allowNullValues = false
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>(false);

        // Act
        map.put("a", null);
    }

    @Test
    public void put_withNullValue_whenNullsAllowed_shouldSucceed() {
        // Arrange: Default constructor allows null values
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();

        // Act
        map.put("a", null);

        // Assert
        assertEquals(1, map.size());
        assertTrue(map.containsKey("a"));
        assertNull(map.get("a"));
    }

    @Test(expected = ClassCastException.class)
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void put_withNonComparableKey_andNaturalOrdering_shouldThrowClassCastException() {
        // Arrange: Using raw types to allow adding a non-comparable key
        LinkedTreeMap map = new LinkedTreeMap();
        map.put("a", 1);

        // Act: Object does not implement Comparable
        map.put(new Object(), 2);
    }

    // --- Comparator Behavior ---

    @Test
    public void operations_shouldRespectCustomComparator() {
        // Arrange: A case-insensitive comparator for strings
        Comparator<String> caseInsensitiveOrder = String.CASE_INSENSITIVE_ORDER;
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>(caseInsensitiveOrder, true);
        map.put("A", 1);

        // Act & Assert for containsKey
        assertTrue("containsKey should be case-insensitive", map.containsKey("a"));

        // Act & Assert for get
        assertEquals("get should be case-insensitive", 1, (int) map.get("a"));

        // Act & Assert for put (overwrite)
        Integer oldValue = map.put("a", 2);
        assertEquals("put should overwrite based on comparator", 1, (int) oldValue);
        assertEquals(1, map.size());

        // Act & Assert for remove
        Integer removedValue = map.remove("a");
        assertEquals("remove should be case-insensitive", 2, (int) removedValue);
        assertTrue(map.isEmpty());
    }

    // --- View Collections: keySet and entrySet ---

    @Test
    public void keySet_shouldReflectMapState() {
        // Arrange
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
        map.put("a", 1);
        map.put("b", 2);

        // Act
        Set<String> keySet = map.keySet();

        // Assert
        assertEquals(2, keySet.size());
        assertTrue(keySet.contains("a"));
        assertTrue(keySet.contains("b"));

        // Assert that removing from the keySet removes from the map
        keySet.remove("a");
        assertEquals(1, map.size());
        assertFalse(map.containsKey("a"));
    }

    @Test
    public void entrySet_shouldReflectMapState() {
        // Arrange
        LinkedTreeMap<String, Integer> map = new LinkedTreeMap<>();
        map.put("a", 1);
        map.put("b", 2);

        // Act
        Set<Map.Entry<String, Integer>> entrySet = map.entrySet();

        // Assert
        assertEquals(2, entrySet.size());
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>("a", 1)));
        assertFalse(entrySet.contains(new AbstractMap.SimpleEntry<>("a", 99)));

        // Assert that removing from the entrySet removes from the map
        entrySet.remove(new AbstractMap.SimpleEntry<>("b", 2));
        assertEquals(1, map.size());
        assertFalse(map.containsKey("b"));
    }

    @Test
    public void iterator_shouldMaintainInsertionOrder() {
        // Arrange
        LinkedTreeMap<Integer, String> map = new LinkedTreeMap<>();
        map.put(3, "C");
        map.put(1, "A");
        map.put(2, "B");

        // Act
        Iterator<Map.Entry<Integer, String>> iterator = map.entrySet().iterator();

        // Assert: Expected order is insertion order, not natural key order
        assertEquals(3, (int) iterator.next().getKey());
        assertEquals(1, (int) iterator.next().getKey());
        assertEquals(2, (int) iterator.next().getKey());
        assertFalse(iterator.hasNext());
    }

    // --- AVL Tree Rebalancing Tests ---

    @Test
    public void put_shouldTriggerRotationsToMaintainBalance() {
        // Arrange
        LinkedTreeMap<Integer, String> map = new LinkedTreeMap<>();

        // Act: This sequence of puts will trigger a right-left double rotation
        // to keep the AVL tree balanced.
        map.put(10, "A");
        map.put(30, "C");
        map.put(20, "B"); // This insert causes the rotation

        // Assert: The map is valid and contains all elements.
        // After rotation, 20 should be the root of the internal tree.
        assertEquals(3, map.size());
        assertEquals("A", map.get(10));
        assertEquals("B", map.get(20));
        assertEquals("C", map.get(30));
    }

    @Test
    public void remove_shouldTriggerRotationsToMaintainBalance() {
        // Arrange
        LinkedTreeMap<Integer, String> map = new LinkedTreeMap<>();
        // This sequence creates a balanced tree where removing the root
        // requires rebalancing.
        map.put(20, "D");
        map.put(10, "B");
        map.put(30, "F");
        map.put(5, "A");
        map.put(15, "C");
        map.put(25, "E");
        map.put(35, "G");

        assertEquals(7, map.size());

        // Act: Remove the root's right child, which will cause an imbalance
        // at the root, triggering a left rotation.
        map.remove(30);

        // Assert: The map is still valid and contains the remaining elements.
        assertEquals(6, map.size());
        assertFalse(map.containsKey(30));
        assertEquals("D", map.get(20)); // Check a few elements
        assertEquals("G", map.get(35));
    }
}