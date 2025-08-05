package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;
import java.util.function.Predicate;
import org.apache.commons.collections4.functors.*;
import org.apache.commons.collections4.set.CompositeSet;

public class CompositeSetTest {

    @Test
    public void testRemoveIfWithExceptionPredicate() {
        Set<Integer> baseSet = createSetWith(-3865);
        CompositeSet<Integer> compositeSet = new CompositeSet<>(baseSet);
        
        Predicate<Integer> exceptionPredicate = ExceptionPredicate.exceptionPredicate();
        
        try {
            compositeSet.removeIf(exceptionPredicate);
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("ExceptionPredicate invoked", e.getMessage());
        }
    }

    @Test
    public void testConstructorWithCircularReference() {
        Set<Object> baseSet = new LinkedHashSet<>();
        Set<Object>[] setArray = createSetArray(3);
        setArray[0] = baseSet;
        setArray[1] = baseSet;
        baseSet.add(baseSet); // Create circular reference
        setArray[2] = baseSet;
        
        try {
            new CompositeSet<>(setArray);
            fail("Expected StackOverflowError");
        } catch (StackOverflowError e) {
            // Expected due to circular reference
        }
    }

    @Test
    public void testBasicAddAndRemove() {
        Set<Object> baseSet = new LinkedHashSet<>();
        UniquePredicate<Integer> predicate = new UniquePredicate<>();
        baseSet.add(predicate);
        
        CompositeSet<Object> compositeSet = new CompositeSet<>(baseSet);
        
        assertTrue(compositeSet.remove(predicate));
        assertTrue(baseSet.isEmpty());
    }

    @Test
    public void testClearOperation() {
        Set<Integer> baseSet = new LinkedHashSet<>();
        CompositeSet<Integer> compositeSet1 = new CompositeSet<>();
        CompositeSet<Integer> compositeSet2 = new CompositeSet<>(baseSet);
        
        compositeSet2.clear();
        assertEquals(compositeSet1, compositeSet2);
    }

    @Test
    public void testRemoveIfWithNullPredicate() {
        CompositeSet<Integer> compositeSet = new CompositeSet<>((Set<Integer>) null);
        
        assertFalse(compositeSet.removeIf(null));
    }

    @Test
    public void testEqualsAndHashCode() {
        Set<Object> baseSet = createEmptySet();
        CompositeSet<Object> compositeSet1 = new CompositeSet<>();
        CompositeSet<Object> compositeSet2 = new CompositeSet<>(baseSet);
        
        Set<Object> toSet = compositeSet1.toSet();
        compositeSet1.addComposited(toSet);
        
        assertNotEquals(compositeSet1, compositeSet2);
    }

    @Test
    public void testAddCompositedWithCollision() {
        Set<Integer> baseSet = createSetWith(113);
        CompositeSet<Integer> compositeSet = new CompositeSet<>(baseSet);
        
        try {
            compositeSet.addComposited(baseSet);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("Collision adding composited set with no SetMutator set", e.getMessage());
        }
    }

    @Test
    public void testIteratorAndSize() {
        Set<Integer> baseSet = createSetWith(512);
        CompositeSet<Integer> compositeSet = new CompositeSet<>(baseSet);
        
        assertEquals(1, compositeSet.size());
        
        Iterator<Integer> iterator = compositeSet.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(512), iterator.next());
    }

    @Test
    public void testContainsAndContainsAll() {
        Set<Integer> baseSet = createSetWith(512);
        CompositeSet<Object> compositeSet = new CompositeSet<>();
        
        assertFalse(compositeSet.containsAll(baseSet));
    }

    @Test
    public void testToArrayOperations() {
        Set<Integer> baseSet = createSetWith(-3881);
        CompositeSet<Integer> compositeSet = new CompositeSet<>(baseSet);
        
        Object[] array1 = compositeSet.toArray();
        assertEquals(1, array1.length);
        assertEquals(Integer.valueOf(-3881), array1[0]);
        
        Object[] array2 = compositeSet.toArray(new Object[0]);
        assertEquals(1, array2.length);
        assertEquals(Integer.valueOf(-3881), array2[0]);
    }

    @Test
    public void testRemoveAll() {
        Set<Integer> baseSet = createSetWith(3270);
        baseSet.add(null); // Add null value
        CompositeSet<Integer> compositeSet = new CompositeSet<>(baseSet);
        
        assertTrue(compositeSet.removeAll(baseSet));
        assertTrue(compositeSet.isEmpty());
    }

    @Test
    public void testRetainAll() {
        Set<Integer> baseSet = createSetWith(-1);
        CompositeSet<Integer> compositeSet = new CompositeSet<>(baseSet);
        
        List<Set<Integer>> sets = compositeSet.getSets();
        assertTrue(compositeSet.retainAll(sets));
        assertFalse(compositeSet.contains(-1));
    }

    @Test
    public void testWithMockMutator() {
        CompositeSet<Integer> compositeSet = new CompositeSet<>();
        CompositeSet.SetMutator<Integer> mockMutator = mock(CompositeSet.SetMutator.class);
        when(mockMutator.add(any(), any(), any())).thenReturn(true);
        
        compositeSet.setMutator(mockMutator);
        assertTrue(compositeSet.add(-3018));
        
        verify(mockMutator).add(eq(compositeSet), any(), eq(-3018));
    }

    @Test
    public void testAddAllWithoutMutator() {
        Set<Integer> baseSet = createSetWith(-6);
        CompositeSet<Integer> compositeSet = new CompositeSet<>(baseSet);
        
        try {
            compositeSet.addAll(baseSet);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("addAll() is not supported on CompositeSet without a SetMutator strategy", e.getMessage());
        }
    }

    @Test
    public void testAddWithoutMutator() {
        CompositeSet<Object> compositeSet = new CompositeSet<>();
        
        try {
            compositeSet.add(new Object());
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            assertEquals("add() is not supported on CompositeSet without a SetMutator strategy", e.getMessage());
        }
    }

    @Test
    public void testNullHandling() {
        CompositeSet<Integer> compositeSet = new CompositeSet<>();
        
        try {
            compositeSet.retainAll(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
        
        try {
            compositeSet.toArray((Object[]) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testArrayStoreException() {
        Set<Integer> baseSet = createSetWith(-6);
        CompositeSet<Integer> compositeSet = new CompositeSet<>(baseSet);
        
        try {
            compositeSet.toArray(new LinkedHashSet[1]);
            fail("Expected ArrayStoreException");
        } catch (ArrayStoreException e) {
            assertEquals("java.lang.Integer", e.getMessage());
        }
    }

    @Test
    public void testEmptyCompositeSet() {
        CompositeSet<Integer> compositeSet = new CompositeSet<>();
        
        assertTrue(compositeSet.isEmpty());
        assertEquals(0, compositeSet.size());
        assertNull(compositeSet.getMutator());
        
        Object[] array = compositeSet.toArray();
        assertEquals(0, array.length);
        
        Set<Integer> toSet = compositeSet.toSet();
        assertTrue(toSet.isEmpty());
    }

    @Test
    public void testStreamOperations() {
        Set<Integer> baseSet = createSetWith(-468);
        CompositeSet<Integer> compositeSet = new CompositeSet<>(baseSet);
        
        assertNotNull(compositeSet.stream());
        assertNotNull(compositeSet.parallelStream());
        assertNotNull(compositeSet.spliterator());
    }

    // Helper methods
    private Set<Integer> createSetWith(int value) {
        Set<Integer> set = new LinkedHashSet<>();
        set.add(value);
        return set;
    }

    private Set<Object> createEmptySet() {
        return new LinkedHashSet<>();
    }

    @SuppressWarnings("unchecked")
    private <T> Set<T>[] createSetArray(int size) {
        return (Set<T>[]) new Set[size];
    }
}