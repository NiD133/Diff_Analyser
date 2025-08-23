package com.google.gson.internal;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import com.google.gson.internal.LinkedTreeMap;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class LinkedTreeMap_ESTest extends LinkedTreeMap_ESTest_scaffolding {

    // Test for verifying the behavior of getting a value from a node
    @Test(timeout = 4000)
    public void testGetNodeValue() throws Throwable {
        LinkedTreeMap.Node<LinkedTreeMap<Integer, Integer>, Integer> node = new LinkedTreeMap.Node<>(true);
        Integer value = node.getValue();
        assertNull(value);
    }

    // Test for verifying the size of an empty EntrySet
    @Test(timeout = 4000)
    public void testEmptyEntrySetSize() throws Throwable {
        Comparator<Integer> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>(comparator, false);
        LinkedTreeMap.EntrySet entrySet = map.new EntrySet();
        entrySet.iterator();
        assertEquals(0, entrySet.size());
    }

    // Test for verifying putIfAbsent and containsKey methods
    @Test(timeout = 4000)
    public void testPutIfAbsentAndContainsKey() throws Throwable {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Integer key = -22;
        map.putIfAbsent(key, null);
        Object nonExistentKey = new Object();
        boolean containsKey = map.containsKey(nonExistentKey);
        assertEquals(1, map.size());
        assertFalse(containsKey);
    }

    // Test for verifying the size after adding an element
    @Test(timeout = 4000)
    public void testSizeAfterAddingElement() throws Throwable {
        Integer key = -2139;
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        map.putIfAbsent(key, key);
        int size = map.size();
        assertEquals(1, size);
    }

    // Test for verifying removeInternal method with a node
    @Test(timeout = 4000)
    public void testRemoveInternalWithNode() throws Throwable {
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        LinkedTreeMap<Object, Object> map = new LinkedTreeMap<>(comparator, true);
        LinkedTreeMap.Node<Object, Object> node = new LinkedTreeMap.Node<>(true);
        map.removeInternal(node, true);
        int size = map.size();
        assertEquals(-1, size);
    }

    // Test for verifying removeInternalByKey with a KeySet
    @Test(timeout = 4000)
    public void testRemoveInternalByKeyWithKeySet() throws Throwable {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        LinkedTreeMap.KeySet keySet = map.new KeySet();
        LinkedTreeMap<Integer, Object> anotherMap = new LinkedTreeMap<>(false);
        LinkedTreeMap.Node<Integer, Object> node = anotherMap.removeInternalByKey(keySet);
        assertNull(node);
        assertEquals(0, anotherMap.size());
    }

    // Test for verifying merge and remove methods
    @Test(timeout = 4000)
    public void testMergeAndRemove() throws Throwable {
        Comparator<Object> comparator = mock(Comparator.class, new ViolatedAssumptionAnswer());
        doReturn(0).when(comparator).compare(any(), any());
        LinkedTreeMap<LinkedTreeMap<Integer, Integer>, Object> map = new LinkedTreeMap<>(comparator, false);
        LinkedTreeMap<Integer, Integer> innerMap = new LinkedTreeMap<>();
        BiFunction<Object, Object, Object> mergeFunction = mock(BiFunction.class, new ViolatedAssumptionAnswer());
        map.merge(innerMap, innerMap, mergeFunction);
        map.remove(new LinkedTreeMap.Node<>(false));
        assertEquals(0, map.size());
    }

    // Test for verifying the behavior of keySet method
    @Test(timeout = 4000)
    public void testKeySetBehavior() throws Throwable {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        map.keySet();
        Set<Integer> keySet = map.keySet();
        assertTrue(keySet.isEmpty());
        assertNotNull(keySet);
    }

    // Test for verifying the behavior of entrySet method
    @Test(timeout = 4000)
    public void testEntrySetBehavior() throws Throwable {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        map.entrySet();
        Set<Map.Entry<Integer, Integer>> entrySet = map.entrySet();
        assertEquals(0, entrySet.size());
    }

    // Test for verifying the behavior of find method
    @Test(timeout = 4000)
    public void testFindBehavior() throws Throwable {
        LinkedTreeMap<LinkedTreeMap<Integer, Integer>, Integer> map = new LinkedTreeMap<>(true);
        LinkedTreeMap<Integer, Integer> innerMap = new LinkedTreeMap<>();
        map.find(innerMap, false);
        assertEquals(0, innerMap.size());
        assertEquals(0, map.size());
    }

    // Test for verifying the behavior of removeInternal method with null node
    @Test(timeout = 4000)
    public void testRemoveInternalWithNullNode() throws Throwable {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        try {
            map.removeInternal(null, true);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("com.google.gson.internal.LinkedTreeMap", e);
        }
    }

    // Test for verifying the behavior of remove method with non-existent key
    @Test(timeout = 4000)
    public void testRemoveWithNonExistentKey() throws Throwable {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Integer key = 1;
        LinkedTreeMap.Node<Integer, Integer> node = map.find(key, true);
        LinkedTreeMap.Node<Integer, Integer> newNode = new LinkedTreeMap.Node<>(true, node, null, node, node);
        map.root = newNode;
        try {
            map.remove(key);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // Test for verifying the behavior of put method with non-existent root
    @Test(timeout = 4000)
    public void testPutWithNonExistentRoot() throws Throwable {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Integer key = -1916;
        LinkedTreeMap.Node<Integer, Integer> node = new LinkedTreeMap.Node<>(false);
        map.root = node;
        try {
            map.put(key, key);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    // Test for verifying the behavior of findByEntry method
    @Test(timeout = 4000)
    public void testFindByEntry() throws Throwable {
        Integer key = -1960;
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        LinkedTreeMap.Node<Integer, Integer> node = map.find(key, true);
        map.findByEntry(node);
        assertEquals(1, map.size());
    }

    // Test for verifying the behavior of putIfAbsent method
    @Test(timeout = 4000)
    public void testPutIfAbsent() throws Throwable {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        Integer key = -1;
        map.putIfAbsent(key, key);
        map.entrySet();
        assertEquals(1, map.size());
    }

    // Test for verifying the behavior of clear method
    @Test(timeout = 4000)
    public void testClearMethod() throws Throwable {
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>(false);
        map.clear();
        assertEquals(0, map.size());
    }

    // Test for verifying the behavior of remove method with KeySet
    @Test(timeout = 4000)
    public void testRemoveWithKeySet() throws Throwable {
        Integer key = -2139;
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        map.putIfAbsent(key, key);
        LinkedTreeMap.KeySet keySet = map.new KeySet();
        boolean removed = keySet.remove(key);
        assertEquals(0, keySet.size());
        assertTrue(removed);
    }

    // Test for verifying the behavior of remove method with EntrySet
    @Test(timeout = 4000)
    public void testRemoveWithEntrySet() throws Throwable {
        Integer key = -2139;
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        LinkedTreeMap.Node<Integer, Integer> node = map.find(key, true);
        LinkedTreeMap.EntrySet entrySet = map.new EntrySet();
        boolean removed = entrySet.remove(node);
        assertEquals(0, entrySet.size());
        assertTrue(removed);
    }

    // Test for verifying the behavior of contains method with EntrySet
    @Test(timeout = 4000)
    public void testContainsWithEntrySet() throws Throwable {
        Integer key = -2139;
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        map.putIfAbsent(key, key);
        LinkedTreeMap.EntrySet entrySet = map.new EntrySet();
        LinkedTreeMap.Node<Map.Entry<Integer, Integer>, Object> node = new LinkedTreeMap.Node<>(false);
        AbstractMap.SimpleEntry<Integer, Integer> entry = new AbstractMap.SimpleEntry<>(key, key);
        LinkedTreeMap.Node<Map.Entry<Integer, Integer>, Object> newNode = new LinkedTreeMap.Node<>(false, node, entry, node, node);
        Object object = newNode.getKey();
        boolean contains = entrySet.contains(object);
        assertEquals(1, map.size());
        assertTrue(contains);
    }

    // Test for verifying the behavior of equals method in Node
    @Test(timeout = 4000)
    public void testNodeEquals() throws Throwable {
        LinkedTreeMap.Node<LinkedTreeMap<Object, Object>, LinkedTreeMap<Integer, Object>> node1 = new LinkedTreeMap.Node<>(false);
        LinkedTreeMap.Node<LinkedTreeMap<Object, Object>, LinkedTreeMap<Integer, Object>> node2 = new LinkedTreeMap.Node<>(false);
        node1.next = node2;
        LinkedTreeMap<Integer, Object> map = new LinkedTreeMap<>();
        LinkedTreeMap<Integer, Object> value = node1.next.setValue(map);
        assertEquals(0, map.size());
        assertNull(value);
        boolean equals = node2.equals(node1.next);
        assertTrue(equals);
    }

    // Test for verifying the behavior of toString method in Node
    @Test(timeout = 4000)
    public void testNodeToString() throws Throwable {
        Integer key = -1960;
        LinkedTreeMap<Integer, Integer> map = new LinkedTreeMap<>();
        LinkedTreeMap.Node<Integer, Integer> node = map.find(key, true);
        String string = node.toString();
        assertEquals(1, map.size());
        assertEquals("-1960=null", string);
    }
}