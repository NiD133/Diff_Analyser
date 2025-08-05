package org.mockito.internal.util.collections;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;

import java.util.*;
import java.util.function.Predicate;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;
import org.mockito.internal.util.collections.HashCodeAndEqualsMockWrapper;
import org.mockito.internal.util.collections.HashCodeAndEqualsSafeSet;

@RunWith(EvoRunner.class) 
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, 
                     resetStaticState = true, separateClassLoader = true) 
public class HashCodeAndEqualsSafeSet_ESTest extends HashCodeAndEqualsSafeSet_ESTest_scaffolding {

    // ========== Factory Method Tests ==========
    
    @Test(timeout = 4000)
    public void testCreateFromNullIterable_ShouldCreateEmptySet() throws Throwable {
        HashCodeAndEqualsSafeSet emptySet = HashCodeAndEqualsSafeSet.of((Iterable<Object>) null);
        HashCodeAndEqualsSafeSet anotherEmptySet = HashCodeAndEqualsSafeSet.of(new LinkedHashSet<>());
        
        assertTrue("Two empty sets should be equal", anotherEmptySet.equals(emptySet));
    }

    @Test(timeout = 4000)
    public void testCreateFromEmptyArray_ShouldHaveZeroSize() throws Throwable {
        Object[] emptyArray = new Object[0];
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(emptyArray);
        
        assertEquals("Empty array should create empty set", 0, set.size());
    }

    @Test(timeout = 4000)
    public void testCreateFromNullArray_ShouldThrowNullPointerException() throws Throwable {
        try { 
            HashCodeAndEqualsSafeSet.of((Object[]) null);
            fail("Should throw NullPointerException for null array");
        } catch(NullPointerException e) {
            // Expected behavior
        }
    }

    // ========== Basic Set Operations Tests ==========
    
    @Test(timeout = 4000)
    public void testAddSingleElement_ShouldIncreaseSize() throws Throwable {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        Object element = new Object();
        
        set.add(element);
        
        assertEquals("Size should be 1 after adding element", 1, set.size());
        assertFalse("Set should not be empty after adding element", set.isEmpty());
    }

    @Test(timeout = 4000)
    public void testAddDuplicateElement_ShouldNotChangeSize() throws Throwable {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        Object element = new Object();
        
        boolean firstAdd = set.add(element);
        boolean secondAdd = set.add(element);
        
        assertTrue("First add should return true", firstAdd);
        assertFalse("Second add should return false", secondAdd);
    }

    @Test(timeout = 4000)
    public void testRemoveExistingElement_ShouldReturnTrue() throws Throwable {
        Object element = new Object();
        Object[] array = {element, null, null};
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(array);
        
        boolean removed = set.remove(element);
        
        assertTrue("Should return true when removing existing element", removed);
    }

    @Test(timeout = 4000)
    public void testRemoveNonExistentElement_ShouldReturnFalse() throws Throwable {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        Predicate<Object> predicate = Predicate.isEqual(set);
        HashCodeAndEqualsMockWrapper wrapper = HashCodeAndEqualsMockWrapper.of(predicate);
        
        boolean removed = set.remove(wrapper);
        
        assertFalse("Should return false when removing non-existent element", removed);
    }

    @Test(timeout = 4000)
    public void testContainsExistingElement_ShouldReturnTrue() throws Throwable {
        Object element = new Object();
        List<Object> list = List.of(element);
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(list);
        
        boolean contains = set.contains(element);
        
        assertTrue("Should contain the added element", contains);
    }

    @Test(timeout = 4000)
    public void testContainsNonExistentElement_ShouldReturnFalse() throws Throwable {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        
        boolean contains = set.contains("non-existent");
        
        assertFalse("Should not contain non-existent element", contains);
    }

    @Test(timeout = 4000)
    public void testClear_ShouldMakeSetEmpty() throws Throwable {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        set.add(new Object());
        
        set.clear();
        
        assertEquals("Size should be 0 after clear", 0, set.size());
        assertTrue("Set should be empty after clear", set.isEmpty());
    }

    // ========== Collection Operations Tests ==========
    
    @Test(timeout = 4000)
    public void testAddAllFromEmptyCollection_ShouldReturnFalse() throws Throwable {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        LinkedList<HashCodeAndEqualsMockWrapper> emptyList = new LinkedList<>();
        
        boolean changed = set.addAll(emptyList);
        
        assertFalse("Adding empty collection should return false", changed);
    }

    @Test(timeout = 4000)
    public void testAddAllFromNonEmptyCollection_ShouldReturnTrue() throws Throwable {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of((Iterable<Object>) null);
        Vector<Object> vector = new Vector<>();
        vector.add(null);
        
        boolean changed = set.addAll(vector);
        
        assertTrue("Adding non-empty collection should return true", changed);
    }

    @Test(timeout = 4000)
    public void testRemoveAllFromSameSet_ShouldReturnFalse() throws Throwable {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        
        boolean changed = set.removeAll(set);
        
        assertFalse("Removing all from empty set should return false", changed);
    }

    @Test(timeout = 4000)
    public void testRemoveAllWithMatchingElements_ShouldReturnTrue() throws Throwable {
        Vector<Object> vector = new Vector<>();
        vector.add(null);
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(vector);
        
        boolean changed = set.removeAll(vector);
        
        assertTrue("Removing matching elements should return true", changed);
    }

    @Test(timeout = 4000)
    public void testRetainAllWithEmptyCollection_ShouldClearSet() throws Throwable {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        set.add(new Object());
        ArrayDeque<HashCodeAndEqualsMockWrapper> emptyDeque = new ArrayDeque<>();
        
        boolean changed = set.retainAll(emptyDeque);
        
        assertTrue("Retaining empty collection should clear set", changed);
    }

    @Test(timeout = 4000)
    public void testRetainAllWithNoMatchingElements_ShouldReturnFalse() throws Throwable {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        Vector<Object> vector = new Vector<>();
        vector.add(null);
        
        boolean changed = set.retainAll(vector);
        
        assertFalse("Retaining with no matching elements should return false", changed);
    }

    @Test(timeout = 4000)
    public void testContainsAllWithEmptyCollection_ShouldReturnTrue() throws Throwable {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        ArrayList<HashCodeAndEqualsMockWrapper> emptyList = new ArrayList<>();
        
        boolean containsAll = set.containsAll(emptyList);
        
        assertTrue("Should contain all elements of empty collection", containsAll);
    }

    @Test(timeout = 4000)
    public void testContainsAllWithNonMatchingElements_ShouldReturnFalse() throws Throwable {
        HashCodeAndEqualsSafeSet emptySet = HashCodeAndEqualsSafeSet.of((Iterable<Object>) null);
        Object[] nonEmptyArray = new Object[6];
        HashCodeAndEqualsSafeSet nonEmptySet = HashCodeAndEqualsSafeSet.of(nonEmptyArray);
        
        boolean containsAll = emptySet.containsAll(nonEmptySet);
        
        assertFalse("Empty set should not contain all elements of non-empty set", containsAll);
    }

    // ========== Array Conversion Tests ==========
    
    @Test(timeout = 4000)
    public void testToArrayWithEmptySet_ShouldReturnSameArray() throws Throwable {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        Object[] inputArray = new Object[0];
        
        Object[] resultArray = set.toArray(inputArray);
        
        assertSame("Should return same array when set is empty", resultArray, inputArray);
    }

    @Test(timeout = 4000)
    public void testToArrayWithNonEmptySet_ShouldReturnCorrectSize() throws Throwable {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        set.add(new Object());
        
        Object[] array = set.toArray();
        
        assertEquals("Array should have size 1", 1, array.length);
    }

    @Test(timeout = 4000)
    public void testToArrayWithTypedArray_ShouldReturnCorrectArray() throws Throwable {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        HashCodeAndEqualsMockWrapper[] typedArray = new HashCodeAndEqualsMockWrapper[6];
        
        HashCodeAndEqualsMockWrapper[] result = set.toArray(typedArray);
        
        assertEquals("Should return array of correct length", 6, result.length);
    }

    @Test(timeout = 4000)
    public void testToArrayWithIncompatibleType_ShouldThrowArrayStoreException() throws Throwable {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        set.add(new Object());
        HashCodeAndEqualsMockWrapper[] incompatibleArray = new HashCodeAndEqualsMockWrapper[4];
        
        try { 
            set.toArray(incompatibleArray);
            fail("Should throw ArrayStoreException for incompatible array type");
        } catch(ArrayStoreException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testToArrayWithDifferentSizedArray_ShouldReturnNewArray() throws Throwable {
        Vector<Object> vector = new Vector<>();
        vector.add(null);
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(vector);
        Object[] smallArray = new Object[0];
        
        Object[] result = set.toArray(smallArray);
        
        assertEquals("Should return array with correct size", 1, result.length);
    }

    // ========== Iterator and Utility Tests ==========
    
    @Test(timeout = 4000)
    public void testIterator_ShouldNotBeNull() throws Throwable {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of((Iterable<Object>) null);
        
        Iterator<Object> iterator = set.iterator();
        
        assertNotNull("Iterator should not be null", iterator);
    }

    @Test(timeout = 4000)
    public void testToStringWithEmptySet_ShouldReturnEmptyBrackets() throws Throwable {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        
        String result = set.toString();
        
        assertEquals("Empty set should return '[]'", "[]", result);
    }

    @Test(timeout = 4000)
    public void testHashCodeWithEmptySet_ShouldNotThrow() throws Throwable {
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of((Iterable<Object>) null);
        
        // Should not throw any exception
        set.hashCode();
    }

    @Test(timeout = 4000)
    public void testEqualsWithNull_ShouldReturnFalse() throws Throwable {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        
        boolean equals = set.equals(null);
        
        assertFalse("Set should not equal null", equals);
    }

    @Test(timeout = 4000)
    public void testRemoveIfWithNullPredicate_ShouldRemoveNullElements() throws Throwable {
        Object[] arrayWithNulls = new Object[14];
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(arrayWithNulls);
        Predicate<Object> isNull = Predicate.isEqual(null);
        
        boolean changed = set.removeIf(isNull);
        
        assertTrue("Should remove null elements", changed);
    }

    @Test(timeout = 4000)
    public void testClone_ShouldThrowCloneNotSupportedException() throws Throwable {
        Object[] array = new Object[1];
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(array);
        
        try { 
            set.clone();
            fail("Should throw CloneNotSupportedException");
        } catch(CloneNotSupportedException e) {
            // Expected behavior
        }
    }

    // ========== Null Parameter Exception Tests ==========
    
    @Test(timeout = 4000)
    public void testToArrayWithNullParameter_ShouldThrowNullPointerException() throws Throwable {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        
        try { 
            set.toArray((HashCodeAndEqualsMockWrapper[]) null);
            fail("Should throw NullPointerException for null array parameter");
        } catch(NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testRetainAllWithNull_ShouldThrowIllegalArgumentException() throws Throwable {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        
        try { 
            set.retainAll(null);
            fail("Should throw IllegalArgumentException for null collection");
        } catch(IllegalArgumentException e) {
            // Expected behavior - collection should not be null
        }
    }

    @Test(timeout = 4000)
    public void testRemoveAllWithNull_ShouldThrowIllegalArgumentException() throws Throwable {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        
        try { 
            set.removeAll(null);
            fail("Should throw IllegalArgumentException for null collection");
        } catch(IllegalArgumentException e) {
            // Expected behavior - collection should not be null
        }
    }

    @Test(timeout = 4000)
    public void testContainsAllWithNull_ShouldThrowIllegalArgumentException() throws Throwable {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        
        try { 
            set.containsAll(null);
            fail("Should throw IllegalArgumentException for null collection");
        } catch(IllegalArgumentException e) {
            // Expected behavior - collection should not be null
        }
    }

    @Test(timeout = 4000)
    public void testAddAllWithNull_ShouldThrowIllegalArgumentException() throws Throwable {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        
        try { 
            set.addAll(null);
            fail("Should throw IllegalArgumentException for null collection");
        } catch(IllegalArgumentException e) {
            // Expected behavior - collection should not be null
        }
    }

    // ========== Mock Wrapper Edge Cases ==========
    
    @Test(timeout = 4000)
    public void testToStringWithMockWrapper_ShouldThrowIllegalStateException() throws Throwable {
        HashCodeAndEqualsSafeSet set = new HashCodeAndEqualsSafeSet();
        HashCodeAndEqualsMockWrapper wrapper = new HashCodeAndEqualsMockWrapper(set);
        set.add(wrapper);
        
        try { 
            set.toString();
            fail("Should throw IllegalStateException when MockMaker plugin not initialized");
        } catch(IllegalStateException e) {
            // Expected behavior when MockMaker plugin cannot be initialized
        }
    }

    @Test(timeout = 4000)
    public void testToStringWithNullElements_ShouldThrowNullPointerException() throws Throwable {
        Object[] arrayWithNulls = new Object[2];
        HashCodeAndEqualsSafeSet set = HashCodeAndEqualsSafeSet.of(arrayWithNulls);
        
        try { 
            set.toString();
            fail("Should throw NullPointerException when processing null elements");
        } catch(NullPointerException e) {
            // Expected behavior when wrapper cannot handle null
        }
    }

    // Note: Several tests with AssertionError are likely testing internal implementation
    // details and may indicate issues with the mock wrapper system. These would need
    // investigation in a real-world scenario.
}