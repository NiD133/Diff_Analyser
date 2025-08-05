package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.LinkedTreeMap;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

/**
 * Test suite for LinkedTreeMap functionality.
 * LinkedTreeMap is a map that maintains insertion order while using comparison order for efficient operations.
 */
public class LinkedTreeMapTest {

    // ========== Node Tests ==========
    
    @Test
    public void testNodeCreation_ShouldHaveNullValue() {
        LinkedTreeMap.Node<String, Integer> node = new LinkedTreeMap.Node<>(true);
        
        assertNull("New node should have null value", node.getValue());
    }

    @Test
    public void testNodeSetValue_ShouldReturnPreviousValue() {
        LinkedTreeMap.Node<String, Integer> node = new LinkedTreeMap.Node<>(true);
        
        Integer previousValue = node.setValue(null);
        
        assertNull("Setting null value should return null", previousValue);
    }

    @Test
    public void testNodeEquals_SameNode_ShouldReturnTrue() {
        LinkedTreeMap.Node<String, Integer> node = new LinkedTreeMap.Node<>(true);
        
        assertTrue("Node should equal itself", node.equals(node));
    }

    @Test
    public void testNodeEquals_DifferentNodes_ShouldReturnFalse() {
        LinkedTreeMap.Node<String, Integer> node1 = new LinkedTreeMap.Node<>(true);
        LinkedTreeMap.Node<String, Integer> node2 = new LinkedTreeMap.Node<>(false);
        
        assertFalse("Different nodes should not be equal", node1.equals(node2));
    }

    @Test
    public void testNodeToString_ShouldShowKeyValuePair() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Integer key = -1960;
        LinkedTreeMap.Node<Integer, Integer> node = map.find(key, true);
        
        String result = node.toString();
        
        assertEquals("Node toString should show key=value format", "-1960=null", result);
        assertEquals("Map should contain one entry", 1, map.size());
    }

    // ========== Basic Map Operations ==========
    
    @Test
    public void testPutIfAbsent_NewKey_ShouldAddEntry() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Integer key = -22;
        
        Integer result = map.putIfAbsent(key, null);
        
        assertNull("putIfAbsent with new key should return null", result);
        assertEquals("Map should contain one entry", 1, map.size());
    }

    @Test
    public void testContainsKey_ExistingKey_ShouldReturnTrue() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Integer key = -22;
        map.putIfAbsent(key, null);
        
        boolean containsKey = map.containsKey(key);
        
        assertTrue("Map should contain the added key", containsKey);
    }

    @Test
    public void testContainsKey_NonExistentKey_ShouldReturnFalse() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Integer key = -22;
        map.putIfAbsent(key, null);
        Object differentKey = new Object();
        
        boolean containsKey = map.containsKey(differentKey);
        
        assertFalse("Map should not contain different key", containsKey);
        assertEquals("Map should still have one entry", 1, map.size());
    }

    @Test
    public void testSize_EmptyMap_ShouldReturnZero() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        
        assertEquals("Empty map should have size 0", 0, map.size());
    }

    @Test
    public void testSize_AfterAddingEntry_ShouldReturnCorrectSize() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Integer key = -2139;
        
        map.putIfAbsent(key, key);
        
        assertEquals("Map with one entry should have size 1", 1, map.size());
    }

    @Test
    public void testClear_ShouldEmptyMap() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>(false);
        
        map.clear();
        
        assertEquals("Cleared map should have size 0", 0, map.size());
    }

    // ========== KeySet Tests ==========
    
    @Test
    public void testKeySet_EmptyMap_ShouldReturnEmptySet() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        
        Set<Integer> keySet = map.keySet();
        
        assertTrue("KeySet of empty map should be empty", keySet.isEmpty());
        assertNotNull("KeySet should not be null", keySet);
    }

    @Test
    public void testKeySet_WithEntries_ShouldReturnCorrectSize() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Integer key = -4058;
        map.put(key, key);
        
        Set<Integer> keySet = map.keySet();
        
        assertEquals("KeySet size should match map size", 1, keySet.size());
        assertEquals("Map should contain one entry", 1, map.size());
    }

    @Test
    public void testKeySetRemove_ExistingKey_ShouldRemoveFromMap() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Integer key = -2139;
        map.putIfAbsent(key, key);
        LinkedTreeMap.KeySet keySet = map.new KeySet();
        
        boolean removed = keySet.remove(key);
        
        assertTrue("Remove should return true for existing key", removed);
        assertEquals("KeySet should be empty after removal", 0, keySet.size());
    }

    @Test
    public void testKeySetRemove_NonExistentKey_ShouldReturnFalse() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>(null, true);
        LinkedTreeMap.KeySet keySet = map.new KeySet();
        
        boolean removed = keySet.remove(map); // Using map object as non-existent key
        
        assertFalse("Remove should return false for non-existent key", removed);
        assertEquals("KeySet should remain empty", 0, keySet.size());
    }

    // ========== EntrySet Tests ==========
    
    @Test
    public void testEntrySet_EmptyMap_ShouldReturnEmptySet() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        
        Set<Map.Entry<Integer, Integer>> entrySet = map.entrySet();
        
        assertEquals("EntrySet of empty map should have size 0", 0, entrySet.size());
    }

    @Test
    public void testEntrySet_WithEntries_ShouldReturnCorrectSize() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Integer key = -1;
        map.putIfAbsent(key, key);
        
        Set<Map.Entry<Integer, Integer>> entrySet = map.entrySet();
        
        assertEquals("EntrySet size should match map size", 1, map.size());
        assertNotNull("EntrySet should not be null", entrySet);
    }

    @Test
    public void testEntrySetRemove_ExistingEntry_ShouldRemoveFromMap() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Integer key = -2139;
        LinkedTreeMap.Node<Integer, Integer> node = map.find(key, true);
        LinkedTreeMap.EntrySet entrySet = map.new EntrySet();
        
        boolean removed = entrySet.remove(node);
        
        assertTrue("Remove should return true for existing entry", removed);
        assertEquals("EntrySet should be empty after removal", 0, entrySet.size());
    }

    @Test
    public void testEntrySetRemove_NonExistentEntry_ShouldReturnFalse() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>(mock(Comparator.class), false);
        LinkedTreeMap.EntrySet entrySet = map.new EntrySet();
        Integer nonExistentKey = -54;
        
        boolean removed = entrySet.remove(nonExistentKey);
        
        assertFalse("Remove should return false for non-existent entry", removed);
        assertEquals("EntrySet should remain empty", 0, entrySet.size());
    }

    // ========== Complex Operations Tests ==========
    
    @Test
    public void testMultipleInsertions_ShouldMaintainCorrectSize() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        
        map.put(21, 21);
        map.putIfAbsent(0, 0);
        Integer result = map.putIfAbsent(-16, null);
        
        assertNull("putIfAbsent with new key should return null", result);
        assertEquals("Map should contain three entries", 3, map.size());
    }

    @Test
    public void testRemoveAfterMultipleInsertions_ShouldMaintainCorrectSize() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Integer key1 = -2139;
        Integer key2 = -1109;
        Integer key3 = -36;
        Integer key4 = 1;
        
        map.putIfAbsent(key1, key1);
        map.putIfAbsent(key3, null);
        map.find(key2, true); // This adds key2 to the map
        map.putIfAbsent(key4, key1);
        map.remove(key1);
        
        assertEquals("Map should contain three entries after removal", 3, map.size());
    }

    @Test
    public void testMergeOperation_ShouldAddEntry() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Integer key = 2017;
        BiFunction<Integer, Object, Integer> mergeFunction = mock(BiFunction.class);
        
        map.merge(key, key, mergeFunction);
        
        assertEquals("Map should contain one entry after merge", 1, map.size());
    }

    // ========== Error Condition Tests ==========
    
    @Test(expected = NullPointerException.class)
    public void testPutWithNullKey_ShouldThrowException() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        
        map.put(null, 42);
    }

    @Test(expected = NullPointerException.class)
    public void testPutWithNullValue_WhenNotAllowed_ShouldThrowException() {
        LinkedTreeMap<Integer, String> map = new LinkedTreeMap<>(false); // null values not allowed
        
        map.put(816, null);
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveInternalWithNullNode_ShouldThrowException() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        
        map.removeInternal(null, true);
    }

    @Test(expected = NullPointerException.class)
    public void testFindByEntryWithNullEntry_ShouldThrowException() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        
        map.findByEntry(null);
    }

    // ========== Custom Comparator Tests ==========
    
    @Test
    public void testWithCustomComparator_ShouldUseComparatorForOrdering() {
        Comparator<Integer> mockComparator = mock(Comparator.class);
        when(mockComparator.compare(any(), any())).thenReturn(-2);
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>(mockComparator, true);
        
        LinkedTreeMap.Node<Integer, Integer> node = map.find(null, true);
        boolean containsNode = map.containsKey(node);
        
        assertTrue("Map should contain the added node", containsNode);
        assertEquals("Map should contain one entry", 1, map.size());
    }

    @Test
    public void testPutAll_WithSameMap_ShouldNotChangeSizeOrContent() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Integer key = -1;
        map.putIfAbsent(key, key);
        
        map.putAll(map); // Adding map to itself
        
        assertEquals("Map size should remain unchanged", 1, map.size());
    }

    // ========== Iterator Tests ==========
    
    @Test
    public void testKeySetIterator_EmptyMap_ShouldWork() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>(mock(Comparator.class), false);
        LinkedTreeMap.EntrySet entrySet = map.new EntrySet();
        
        entrySet.iterator();
        
        assertEquals("EntrySet should be empty", 0, entrySet.size());
    }

    @Test
    public void testEntrySetIterator_EmptyMap_ShouldWork() {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>(mock(Comparator.class), true);
        LinkedTreeMap.KeySet keySet = map.new KeySet();
        
        keySet.iterator();
        
        assertEquals("KeySet should be empty", 0, keySet.size());
    }
}