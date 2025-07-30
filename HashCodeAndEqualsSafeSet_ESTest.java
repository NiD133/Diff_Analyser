package org.mockito.internal.util.collections;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class HashCodeAndEqualsSafeSetTest extends HashCodeAndEqualsSafeSet_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testEqualityOfEmptySets() {
        HashCodeAndEqualsSafeSet emptySet1 = HashCodeAndEqualsSafeSet.of((Iterable<Object>) null);
        HashCodeAndEqualsSafeSet emptySet2 = HashCodeAndEqualsSafeSet.of(new LinkedHashSet<>());
        assertTrue(emptySet2.equals(emptySet1));
    }

    @Test(timeout = 4000)
    public void testToArrayWithEmptySet() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        Object[] emptyArray = new Object[0];
        assertSame(set.toArray(emptyArray), emptyArray);
    }

    @Test(timeout = 4000)
    public void testToArrayWithSingleElement() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        set.add(new Object());
        assertEquals(1, set.toArray().length);
    }

    @Test(timeout = 4000)
    public void testSizeAfterAddingElement() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        set.add(new Object());
        assertEquals(1, set.size());
    }

    @Test(timeout = 4000)
    public void testRetainAllWithEmptyCollection() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        set.add(new Object());
        assertTrue(set.retainAll(new ArrayDeque<>()));
    }

    @Test(timeout = 4000)
    public void testRemoveAllWithVectorContainingNull() {
        Vector<Object> vector = new Vector<>();
        vector.add(null);
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(vector);
        assertTrue(set.removeAll(vector));
    }

    @Test(timeout = 4000)
    public void testRemoveAllWithSelf() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        assertFalse(set.removeAll(set));
    }

    @Test(timeout = 4000)
    public void testRemoveElement() {
        Object element = new Object();
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(new Object[]{element});
        assertTrue(set.remove(element));
    }

    @Test(timeout = 4000)
    public void testEmptySetSize() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(new Object[0]);
        assertEquals(0, set.size());
    }

    @Test(timeout = 4000)
    public void testIteratorNotNullForEmptySet() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of((Iterable<Object>) null);
        assertNotNull(set.iterator());
    }

    @Test(timeout = 4000)
    public void testIsEmptyAfterAddingElement() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        set.add(new Object());
        assertFalse(set.isEmpty());
    }

    @Test(timeout = 4000)
    public void testContainsAllWithEmptyCollection() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        assertTrue(set.containsAll(new ArrayList<>()));
    }

    @Test(timeout = 4000)
    public void testContainsAllWithNonEmptyCollection() {
        HashCodeAndEqualsSafeSet emptySet = HashCodeAndEqualsSafeSet.of((Iterable<Object>) null);
        HashCodeAndEqualsSafeSet nonEmptySet = HashCodeAndEqualsSafeSet.of(new Object[6]);
        assertFalse(emptySet.containsAll(nonEmptySet));
    }

    @Test(timeout = 4000)
    public void testContainsElement() {
        Object element = new Object();
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(List.of(element));
        assertTrue(set.contains(element));
    }

    @Test(timeout = 4000)
    public void testAddAllWithVectorContainingNull() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of((Iterable<Object>) null);
        Vector<Object> vector = new Vector<>();
        vector.add(null);
        assertTrue(set.addAll(vector));
    }

    @Test(timeout = 4000)
    public void testAddAllWithEmptyCollection() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        assertFalse(set.addAll(new LinkedList<>()));
    }

    @Test(timeout = 4000)
    public void testAddDuplicateElement() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        Object element = new Object();
        assertTrue(set.add(element));
        assertFalse(set.add(element));
    }

    @Test(timeout = 4000)
    public void testToStringThrowsIllegalStateException() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        HashCodeAndEqualsMockWrapper wrapper = new HashCodeAndEqualsMockWrapper(set);
        set.add(wrapper);
        try {
            set.toString();
            fail("Expecting exception: IllegalStateException");
        } catch (IllegalStateException e) {
            verifyException("org.mockito.internal.configuration.plugins.PluginLoader$1", e);
        }
    }

    @Test(timeout = 4000)
    public void testToArrayThrowsNullPointerException() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        try {
            set.toArray((HashCodeAndEqualsMockWrapper[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.mockito.internal.util.collections.HashCodeAndEqualsSafeSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testRetainAllThrowsIllegalArgumentException() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        try {
            set.retainAll((Collection<?>) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.mockito.internal.util.Checks", e);
        }
    }

    @Test(timeout = 4000)
    public void testRetainAllThrowsAssertionError() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        HashCodeAndEqualsMockWrapper wrapper = new HashCodeAndEqualsMockWrapper(set);
        List<HashCodeAndEqualsMockWrapper> list = List.of(wrapper, wrapper, wrapper, wrapper, wrapper, wrapper, wrapper);
        try {
            set.retainAll(list);
            fail("Expecting exception: AssertionError");
        } catch (AssertionError e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testRemoveAllThrowsIllegalArgumentException() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        try {
            set.removeAll((Collection<?>) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.mockito.internal.util.Checks", e);
        }
    }

    @Test(timeout = 4000)
    public void testOfThrowsNullPointerException() {
        try {
            HashCodeAndEqualsSafeSet.of((Object[]) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("java.util.Objects", e);
        }
    }

    @Test(timeout = 4000)
    public void testContainsAllThrowsIllegalArgumentException() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        try {
            set.containsAll((Collection<?>) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.mockito.internal.util.Checks", e);
        }
    }

    @Test(timeout = 4000)
    public void testAddAllThrowsAssertionError() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        LinkedList<HashCodeAndEqualsMockWrapper> list = new LinkedList<>();
        HashCodeAndEqualsMockWrapper wrapper = HashCodeAndEqualsMockWrapper.of(list);
        list.add(wrapper);
        try {
            set.addAll(list);
            fail("Expecting exception: AssertionError");
        } catch (AssertionError e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testEmptySetSizeIsZero() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        assertEquals(0, set.size());
    }

    @Test(timeout = 4000)
    public void testToArrayThrowsArrayStoreException() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        set.add(new Object());
        HashCodeAndEqualsMockWrapper[] array = new HashCodeAndEqualsMockWrapper[4];
        try {
            set.toArray(array);
            fail("Expecting exception: ArrayStoreException");
        } catch (ArrayStoreException e) {
            verifyException("org.mockito.internal.util.collections.HashCodeAndEqualsSafeSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testRetainAllWithVectorContainingNull() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        Vector<Object> vector = new Vector<>();
        vector.add(null);
        assertFalse(set.retainAll(vector));
    }

    @Test(timeout = 4000)
    public void testToArrayWithTypedArray() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        HashCodeAndEqualsMockWrapper[] array = new HashCodeAndEqualsMockWrapper[6];
        assertEquals(6, set.toArray(array).length);
    }

    @Test(timeout = 4000)
    public void testEqualsWithNull() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        assertFalse(set.equals(null));
    }

    @Test(timeout = 4000)
    public void testRemoveIfWithPredicate() {
        Object[] array = new Object[14];
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(array);
        Predicate<Object> predicate = Predicate.isEqual(null);
        assertTrue(set.removeIf(predicate));
    }

    @Test(timeout = 4000)
    public void testToArrayWithNonEmptySet() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        Vector<Object> vector = new Vector<>();
        vector.add(null);
        Object[] array = set.toArray();
        HashCodeAndEqualsSafeSet nonEmptySet = HashCodeAndEqualsSafeSet.of(vector);
        assertEquals(1, nonEmptySet.toArray(array).length);
    }

    @Test(timeout = 4000)
    public void testCloneThrowsCloneNotSupportedException() {
        Object[] array = new Object[1];
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(array);
        try {
            set.clone();
            fail("Expecting exception: CloneNotSupportedException");
        } catch (CloneNotSupportedException e) {
            verifyException("org.mockito.internal.util.collections.HashCodeAndEqualsSafeSet", e);
        }
    }

    @Test(timeout = 4000)
    public void testRemoveWithPredicate() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        Predicate<Object> predicate = Predicate.isEqual(set);
        HashCodeAndEqualsMockWrapper wrapper = HashCodeAndEqualsMockWrapper.of(predicate);
        assertFalse(set.remove(wrapper));
    }

    @Test(timeout = 4000)
    public void testContainsWithString() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        assertFalse(set.contains("[]"));
    }

    @Test(timeout = 4000)
    public void testToStringWithEmptySet() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        assertEquals("[]", set.toString());
    }

    @Test(timeout = 4000)
    public void testHashCodeWithEmptySet() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of((Iterable<Object>) null);
        set.hashCode(); // Ensure no exception is thrown
    }

    @Test(timeout = 4000)
    public void testRemoveAllThrowsAssertionError() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        Object element = new Object();
        HashCodeAndEqualsMockWrapper wrapper = HashCodeAndEqualsMockWrapper.of(element);
        List<HashCodeAndEqualsMockWrapper> list = List.of(wrapper, wrapper, wrapper, wrapper);
        try {
            set.removeAll(list);
            fail("Expecting exception: AssertionError");
        } catch (AssertionError e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testIsEmptyWithEmptySet() {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of((Iterable<Object>) null);
        assertTrue(set.isEmpty());
    }

    @Test(timeout = 4000)
    public void testAddAllThrowsIllegalArgumentException() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        try {
            set.addAll((Collection<?>) null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.mockito.internal.util.Checks", e);
        }
    }

    @Test(timeout = 4000)
    public void testContainsAllThrowsAssertionError() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        Object element = new Object();
        HashCodeAndEqualsMockWrapper wrapper = HashCodeAndEqualsMockWrapper.of(element);
        List<HashCodeAndEqualsMockWrapper> list = List.of(wrapper, wrapper, wrapper, wrapper, wrapper, wrapper, wrapper, wrapper, wrapper);
        try {
            set.containsAll(list);
            fail("Expecting exception: AssertionError");
        } catch (AssertionError e) {
            // Expected exception
        }
    }

    @Test(timeout = 4000)
    public void testClear() {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        set.clear();
        assertEquals(0, set.size());
    }

    @Test(timeout = 4000)
    public void testToStringThrowsNullPointerException() {
        Object[] array = new Object[2];
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(array);
        try {
            set.toString();
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.mockito.internal.util.collections.HashCodeAndEqualsMockWrapper", e);
        }
    }
}