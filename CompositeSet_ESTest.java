package org.apache.commons.collections4.set;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.junit.Test;

/**
 * Focused and readable tests for CompositeSet.
 * These tests cover core behaviors without relying on EvoSuite scaffolding
 * or Apache functor predicates.
 */
public class CompositeSetTest {

    @Test
    public void emptyComposite_hasZeroSize_andIsEmpty_andToArrayIsEmpty() {
        CompositeSet<Integer> composite = new CompositeSet<>();

        assertTrue(composite.isEmpty());
        assertEquals(0, composite.size());
        assertArrayEquals(new Object[0], composite.toArray());

        Integer[] target = new Integer[0];
        assertArrayEquals(target, composite.toArray(target));
    }

    @Test
    public void singleBackingSet_size_contains_iterator_toArray() {
        LinkedHashSet<Integer> backing = new LinkedHashSet<>(Arrays.asList(1, 2, 3));
        CompositeSet<Integer> composite = new CompositeSet<>(backing);

        assertFalse(composite.isEmpty());
        assertEquals(3, composite.size());
        assertTrue(composite.contains(1));
        assertTrue(composite.contains(2));
        assertTrue(composite.contains(3));
        assertFalse(composite.contains(4));

        // iterator traverses all elements (order not asserted)
        HashSet<Integer> seen = new HashSet<>();
        for (Integer i : composite) {
            seen.add(i);
        }
        assertEquals(new HashSet<>(Arrays.asList(1, 2, 3)), seen);

        // toArray variants
        Object[] asObjects = composite.toArray();
        assertEquals(3, asObjects.length);
        HashSet<Object> asSet = new HashSet<>(Arrays.asList(asObjects));
        assertEquals(new HashSet<>(Arrays.asList(1, 2, 3)), asSet);

        Integer[] target = new Integer[3];
        Integer[] filled = composite.toArray(target);
        assertSame(target, filled);
        assertEquals(new HashSet<>(Arrays.asList(1, 2, 3)), new HashSet<>(Arrays.asList(filled)));
    }

    @Test
    public void toSet_returnsCopy_notBackedByComposite() {
        LinkedHashSet<Integer> backing = new LinkedHashSet<>(Arrays.asList(10, 20));
        CompositeSet<Integer> composite = new CompositeSet<>(backing);

        Set<Integer> copy = composite.toSet();
        assertEquals(new HashSet<>(Arrays.asList(10, 20)), copy);

        // Mutating the returned copy does not affect the composite/backing
        copy.add(30);
        assertFalse(composite.contains(30));
        assertEquals(2, composite.size());
        assertEquals(new HashSet<>(Arrays.asList(10, 20)), backing);
    }

    @Test
    public void removeAll_removesFromBackingSets() {
        LinkedHashSet<Integer> backing = new LinkedHashSet<>(Arrays.asList(1, 2, 3));
        CompositeSet<Integer> composite = new CompositeSet<>(backing);

        boolean modified = composite.removeAll(Collections.singleton(2));
        assertTrue(modified);
        assertFalse(composite.contains(2));
        assertFalse(backing.contains(2));
        assertEquals(2, composite.size());
    }

    @Test
    public void retainAll_keepsOnlySpecifiedElements_inBackingSets() {
        LinkedHashSet<Integer> backing = new LinkedHashSet<>(Arrays.asList(1, 2, 3));
        CompositeSet<Integer> composite = new CompositeSet<>(backing);

        boolean modified = composite.retainAll(Collections.singleton(1));
        assertTrue(modified);
        assertTrue(composite.contains(1));
        assertFalse(composite.contains(2));
        assertFalse(composite.contains(3));
        assertEquals(new LinkedHashSet<>(Collections.singletonList(1)), backing);
        assertEquals(1, composite.size());
    }

    @Test
    public void iterator_traversesAllCompositedSets() {
        LinkedHashSet<Integer> a = new LinkedHashSet<>(Arrays.asList(1, 2));
        LinkedHashSet<Integer> b = new LinkedHashSet<>(Arrays.asList(3, 4));
        CompositeSet<Integer> composite = new CompositeSet<>(a, b);

        HashSet<Integer> seen = new HashSet<>();
        for (Integer i : composite) {
            seen.add(i);
        }
        assertEquals(new HashSet<>(Arrays.asList(1, 2, 3, 4)), seen);
        assertEquals(4, composite.size());
    }

    @Test
    public void add_withoutMutator_throwsUnsupportedOperation() {
        CompositeSet<Integer> composite = new CompositeSet<>(new LinkedHashSet<>());

        try {
            composite.add(42);
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
            // OK
        }
    }

    @Test
    public void addAll_withoutMutator_throwsUnsupportedOperation() {
        CompositeSet<Integer> composite = new CompositeSet<>(new LinkedHashSet<>());

        try {
            composite.addAll(Arrays.asList(1, 2));
            fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
            // OK
        }
    }

    @Test
    public void addComposited_disjointSet_isAllowed_withoutMutator() {
        LinkedHashSet<Integer> a = new LinkedHashSet<>(Arrays.asList(1, 2));
        LinkedHashSet<Integer> b = new LinkedHashSet<>(Arrays.asList(3, 4));

        CompositeSet<Integer> composite = new CompositeSet<>(a);
        // Disjoint addition should succeed without a mutator
        composite.addComposited(b);

        assertTrue(composite.containsAll(Arrays.asList(1, 2, 3, 4)));
        assertEquals(4, composite.size());
    }

    @Test
    public void addComposited_overlappingSet_throwsUnsupportedOperation_withoutMutator() {
        LinkedHashSet<Integer> a = new LinkedHashSet<>(Arrays.asList(1, 2));
        LinkedHashSet<Integer> b = new LinkedHashSet<>(Arrays.asList(2, 3));

        CompositeSet<Integer> composite = new CompositeSet<>(a);

        try {
            composite.addComposited(b);
            fail("Expected UnsupportedOperationException due to overlap and no mutator");
        } catch (UnsupportedOperationException expected) {
            // OK
        }
    }

    @Test
    public void addComposited_null_throwsNullPointerException() {
        CompositeSet<Integer> composite = new CompositeSet<>();

        try {
            composite.addComposited((Set<Integer>) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // OK
        }
    }

    @Test
    public void removeIf_removesMatchingElements() {
        LinkedHashSet<Integer> backing = new LinkedHashSet<>(Arrays.asList(1, 2, 3, 4));
        CompositeSet<Integer> composite = new CompositeSet<>(backing);

        Predicate<Integer> isEven = n -> n % 2 == 0;
        boolean modified = composite.removeIf(isEven);

        assertTrue(modified);
        assertEquals(new LinkedHashSet<>(Arrays.asList(1, 3)), backing);
        assertEquals(2, composite.size());
        assertFalse(composite.contains(2));
        assertFalse(composite.contains(4));
    }

    @Test
    public void equalsAndHashCode_basedOnElements_notOnBackingStructure() {
        LinkedHashSet<Integer> a = new LinkedHashSet<>(Arrays.asList(1, 2));
        LinkedHashSet<Integer> b = new LinkedHashSet<>(Arrays.asList(2, 1));

        CompositeSet<Integer> c1 = new CompositeSet<>(a);
        CompositeSet<Integer> c2 = new CompositeSet<>(b);

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());

        // equals should also be true against a plain Set with same elements
        assertEquals(new HashSet<>(Arrays.asList(1, 2)), c1.toSet());
        assertEquals(new HashSet<>(Arrays.asList(1, 2)), c2.toSet());
    }

    @Test
    public void clear_clearsAllContainedSets() {
        LinkedHashSet<Integer> a = new LinkedHashSet<>(Arrays.asList(1, 2));
        LinkedHashSet<Integer> b = new LinkedHashSet<>(Arrays.asList(3, 4));
        CompositeSet<Integer> composite = new CompositeSet<>(a, b);

        composite.clear();

        assertTrue(composite.isEmpty());
        assertTrue(a.isEmpty());
        assertTrue(b.isEmpty());
    }

    @Test
    public void remove_removesFromFirstContainingSet() {
        LinkedHashSet<Integer> a = new LinkedHashSet<>(Arrays.asList(1));
        LinkedHashSet<Integer> b = new LinkedHashSet<>(Arrays.asList(2));
        CompositeSet<Integer> composite = new CompositeSet<>(a, b);

        assertTrue(composite.remove(1));
        assertFalse(a.contains(1));
        assertEquals(1, composite.size());
        assertTrue(composite.contains(2));
    }

    @Test
    public void getSets_returnsUnmodifiableView_withAllBackingSets() {
        LinkedHashSet<Integer> a = new LinkedHashSet<>();
        LinkedHashSet<Integer> b = new LinkedHashSet<>();
        CompositeSet<Integer> composite = new CompositeSet<>(a, b);

        List<Set<Integer>> setsView = composite.getSets();
        assertEquals(2, setsView.size());
        assertTrue(setsView.contains(a));
        assertTrue(setsView.contains(b));

        try {
            setsView.add(new LinkedHashSet<>());
            fail("Expected UnsupportedOperationException for unmodifiable list");
        } catch (UnsupportedOperationException expected) {
            // OK
        }
    }

    @Test
    public void equals_emptyComposites_areEqual_andEqualToEmptySet() {
        CompositeSet<Integer> a = new CompositeSet<>();
        CompositeSet<Integer> b = new CompositeSet<>();

        assertEquals(a, b);
        assertEquals(new HashSet<>(), a.toSet());
        assertEquals(new HashSet<>(), b.toSet());
    }

    @Test
    public void removeComposited_removesThatSetFromComposite() {
        LinkedHashSet<Integer> a = new LinkedHashSet<>(Arrays.asList(1, 2));
        LinkedHashSet<Integer> b = new LinkedHashSet<>(Arrays.asList(3, 4));
        CompositeSet<Integer> composite = new CompositeSet<>(a, b);

        composite.removeComposited(a);

        assertFalse(composite.contains(1));
        assertFalse(composite.contains(2));
        assertTrue(composite.contains(3));
        assertTrue(composite.contains(4));
        assertEquals(2, composite.size());
    }

    @Test
    public void constructor_withVarargs_nullOrEmpty_areAllowed() {
        CompositeSet<Integer> fromNullVarargs = new CompositeSet<>((Set<Integer>[]) null);
        assertTrue(fromNullVarargs.isEmpty());

        CompositeSet<Integer> fromEmptyVarargs = new CompositeSet<>(new Set<?>[] { });
        assertTrue(fromEmptyVarargs.isEmpty());
    }

    @Test
    public void toArray_withLargerTargetArray_returnsSameArray_andLeavesExtraNulls() {
        LinkedHashSet<Integer> backing = new LinkedHashSet<>(Arrays.asList(7, 8));
        CompositeSet<Integer> composite = new CompositeSet<>(backing);

        Integer[] target = new Integer[5];
        Integer[] result = composite.toArray(target);

        assertSame(target, result);
        // Elements present in any order in first two positions; remaining positions stay null.
        HashSet<Integer> firstTwo = new HashSet<>(Arrays.asList(result[0], result[1]));
        assertEquals(new HashSet<>(Arrays.asList(7, 8)), firstTwo);
        assertNull(result[2]);
        assertNull(result[3]);
        assertNull(result[4]);
    }
}