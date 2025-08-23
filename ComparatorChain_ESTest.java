package org.apache.commons.collections4.comparators;

import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

/**
 * Readable, behavior-focused tests for ComparatorChain.
 * 
 * These tests cover:
 * - Empty chain behavior
 * - Adding comparators and basic comparison
 * - Reverse ordering
 * - Locking after first comparison
 * - Mutations before/after lock
 * - Index bounds
 * - Equality and hash code
 * - Multi-comparator (tie-breaker) behavior
 */
public class ComparatorChainTest {

    @Test
    public void testEmptyChainCompareThrows() {
        ComparatorChain<Integer> chain = new ComparatorChain<>();

        try {
            chain.compare(1, 2);
            fail("Expected UnsupportedOperationException for empty chain");
        } catch (UnsupportedOperationException ex) {
            // Message comes from the implementation; assert it if stable
            assertTrue(ex.getMessage().toLowerCase().contains("at least one comparator"));
        }
    }

    @Test
    public void testAddComparatorEnablesCompareAndLocks() {
        ComparatorChain<Integer> chain = new ComparatorChain<>();
        chain.addComparator(Integer::compareTo);

        assertEquals(1, chain.size());
        assertFalse(chain.isLocked());

        // 1 < 2 => negative
        assertTrue(chain.compare(1, 2) < 0);
        assertTrue("Chain should be locked after first comparison", chain.isLocked());
    }

    @Test
    public void testAddComparatorWithReverseSort() {
        ComparatorChain<Integer> chain = new ComparatorChain<>();
        chain.addComparator(Integer::compareTo, true);

        // Reverse: 1 vs 2 should be > 0
        assertTrue(chain.compare(1, 2) > 0);
    }

    @Test
    public void testCannotModifyAfterFirstComparison() {
        Comparator<Integer> cmp = Integer::compareTo;
        ComparatorChain<Integer> chain = new ComparatorChain<>(cmp);

        // Lock the chain
        assertTrue(chain.compare(1, 1) == 0);
        assertTrue(chain.isLocked());

        // Any mutation should fail after locking
        try {
            chain.setComparator(0, cmp);
            fail("Expected UnsupportedOperationException after locking");
        } catch (UnsupportedOperationException expected) {
            // ok
        }

        try {
            chain.addComparator(cmp);
            fail("Expected UnsupportedOperationException after locking");
        } catch (UnsupportedOperationException expected) {
            // ok
        }

        try {
            chain.setReverseSort(0);
            fail("Expected UnsupportedOperationException after locking");
        } catch (UnsupportedOperationException expected) {
            // ok
        }
    }

    @Test
    public void testEqualsAndHashCodeBasedOnComparatorsAndOrderingBits() {
        Comparator<Integer> cmp = Integer::compareTo;

        ComparatorChain<Integer> a = new ComparatorChain<>(cmp);
        ComparatorChain<Integer> b = new ComparatorChain<>(cmp);
        ComparatorChain<Integer> c = new ComparatorChain<>(cmp, true); // different ordering

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());

        assertNotEquals(a, c);
        assertNotEquals(a.hashCode(), c.hashCode());

        assertNotEquals(a, null);
        assertEquals(a, a);
    }

    @Test
    public void testSetComparatorByIndexBeforeLock() {
        Comparator<Integer> natural = Integer::compareTo;
        Comparator<Integer> reverse = Comparator.reverseOrder();

        ComparatorChain<Integer> chain = new ComparatorChain<>(natural);
        chain.setComparator(0, reverse);

        // Now reversed: 1 vs 2 should be > 0
        assertTrue(chain.compare(1, 2) > 0);
    }

    @Test
    public void testSetComparatorOutOfBoundsOnEmptyChain() {
        ComparatorChain<Integer> chain = new ComparatorChain<>();

        try {
            chain.setComparator(0, Integer::compareTo);
            fail("Expected IndexOutOfBoundsException setting comparator on empty chain");
        } catch (IndexOutOfBoundsException expected) {
            // ok
        }
    }

    @Test
    public void testChangeSortDirectionBeforeLock() {
        ComparatorChain<Integer> chain1 = new ComparatorChain<>(Integer::compareTo);
        chain1.setReverseSort(0);
        assertTrue("Should reverse the natural order", chain1.compare(1, 2) > 0);

        ComparatorChain<Integer> chain2 = new ComparatorChain<>(Comparator.reverseOrder());
        chain2.setForwardSort(0);
        assertTrue("Should set to forward (natural) order", chain2.compare(1, 2) < 0);
    }

    @Test
    public void testSizeReflectsNumberOfComparators() {
        ComparatorChain<Integer> chain = new ComparatorChain<>();
        assertEquals(0, chain.size());

        chain.addComparator(Integer::compareTo);
        assertEquals(1, chain.size());

        chain.addComparator(Comparator.reverseOrder());
        assertEquals(2, chain.size());
    }

    @Test
    public void testMultiComparatorTieBreakerBehavior() {
        // Compare by 'a' first, then by 'b'
        Comparator<Pair> byA = Comparator.comparingInt(p -> p.a);
        Comparator<Pair> byB = Comparator.comparingInt(p -> p.b);

        ComparatorChain<Pair> chain = new ComparatorChain<>();
        chain.addComparator(byA);
        chain.addComparator(byB);

        Pair p12 = new Pair(1, 2);
        Pair p13 = new Pair(1, 3);
        Pair p22 = new Pair(2, 2);
        Pair p12b = new Pair(1, 2);

        // Primary key (a) differs
        assertTrue(chain.compare(p12, p22) < 0);
        assertTrue(chain.compare(p22, p12) > 0);

        // Primary equal, use secondary (b)
        assertTrue(chain.compare(p12, p13) < 0);
        assertTrue(chain.compare(p13, p12) > 0);

        // Both equal
        assertEquals(0, chain.compare(p12, p12b));
    }

    @Test
    public void testSetForwardSortWithNegativeIndexThrows() {
        ComparatorChain<Integer> chain = new ComparatorChain<>(Integer::compareTo);

        try {
            chain.setForwardSort(-1);
            fail("Expected IndexOutOfBoundsException for negative index");
        } catch (IndexOutOfBoundsException expected) {
            // ok
        }
    }

    private static final class Pair {
        final int a;
        final int b;

        Pair(int a, int b) {
            this.a = a;
            this.b = b;
        }
    }
}