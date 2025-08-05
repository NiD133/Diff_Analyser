package org.apache.commons.collections4.comparators;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.BitSet;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Test suite for ComparatorChain class.
 * ComparatorChain allows chaining multiple comparators together,
 * similar to multi-column sorting in SQL.
 */
public class ComparatorChainTest {

    // ========== Basic Functionality Tests ==========

    @Test
    public void testEmptyChainSize() {
        ComparatorChain<String> chain = new ComparatorChain<>();
        assertEquals("Empty chain should have size 0", 0, chain.size());
    }

    @Test
    public void testSingleComparatorChainSize() {
        Comparator<String> stringComparator = String::compareTo;
        ComparatorChain<String> chain = new ComparatorChain<>(stringComparator);
        assertEquals("Chain with one comparator should have size 1", 1, chain.size());
    }

    @Test
    public void testAddComparatorIncreasesSize() {
        ComparatorChain<Integer> chain = new ComparatorChain<>();
        Comparator<Integer> naturalOrder = Integer::compareTo;
        
        chain.addComparator(naturalOrder);
        assertEquals("Adding comparator should increase size to 1", 1, chain.size());
        
        chain.addComparator(naturalOrder.reversed());
        assertEquals("Adding second comparator should increase size to 2", 2, chain.size());
    }

    // ========== Comparison Tests ==========

    @Test
    public void testSingleComparatorForwardSort() {
        Comparator<Integer> naturalOrder = Integer::compareTo;
        ComparatorChain<Integer> chain = new ComparatorChain<>(naturalOrder, false);
        
        int result = chain.compare(1, 2);
        assertTrue("1 should be less than 2 in forward sort", result < 0);
    }

    @Test
    public void testSingleComparatorReverseSort() {
        Comparator<Integer> naturalOrder = Integer::compareTo;
        ComparatorChain<Integer> chain = new ComparatorChain<>(naturalOrder, true);
        
        int result = chain.compare(1, 2);
        assertTrue("1 should be greater than 2 in reverse sort", result > 0);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEmptyChainThrowsExceptionOnCompare() {
        ComparatorChain<String> emptyChain = new ComparatorChain<>();
        emptyChain.compare("a", "b");
    }

    // ========== Locking Behavior Tests ==========

    @Test
    public void testChainIsNotLockedInitially() {
        ComparatorChain<String> chain = new ComparatorChain<>();
        assertFalse("New chain should not be locked", chain.isLocked());
    }

    @Test
    public void testChainBecomesLockedAfterFirstComparison() {
        Comparator<String> stringComparator = String::compareTo;
        ComparatorChain<String> chain = new ComparatorChain<>(stringComparator);
        
        assertFalse("Chain should not be locked before comparison", chain.isLocked());
        
        chain.compare("a", "b");
        
        assertTrue("Chain should be locked after comparison", chain.isLocked());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCannotModifyLockedChain() {
        Comparator<String> stringComparator = String::compareTo;
        ComparatorChain<String> chain = new ComparatorChain<>(stringComparator);
        
        // Lock the chain by performing a comparison
        chain.compare("a", "b");
        
        // This should throw an exception
        chain.setComparator(0, stringComparator.reversed());
    }

    // ========== Sort Order Modification Tests ==========

    @Test
    public void testSetForwardSort() {
        Comparator<Integer> naturalOrder = Integer::compareTo;
        ComparatorChain<Integer> chain = new ComparatorChain<>(naturalOrder, true); // Start with reverse
        
        chain.setForwardSort(0);
        
        int result = chain.compare(1, 2);
        assertTrue("Should use forward sort after setForwardSort", result < 0);
    }

    @Test
    public void testSetReverseSort() {
        Comparator<Integer> naturalOrder = Integer::compareTo;
        ComparatorChain<Integer> chain = new ComparatorChain<>(naturalOrder, false); // Start with forward
        
        chain.setReverseSort(0);
        
        int result = chain.compare(1, 2);
        assertTrue("Should use reverse sort after setReverseSort", result > 0);
    }

    @Test
    public void testReplaceComparator() {
        Comparator<String> originalComparator = String::compareTo;
        Comparator<String> newComparator = String.CASE_INSENSITIVE_ORDER;
        
        ComparatorChain<String> chain = new ComparatorChain<>(originalComparator);
        chain.setComparator(0, newComparator);
        
        // Verify the comparator was replaced by testing case-insensitive behavior
        int result = chain.compare("A", "a");
        assertEquals("Should be equal with case-insensitive comparator", 0, result);
    }

    // ========== Error Condition Tests ==========

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetComparatorWithInvalidIndex() {
        ComparatorChain<String> chain = new ComparatorChain<>();
        chain.setComparator(0, String::compareTo); // Index 0 doesn't exist in empty chain
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetForwardSortWithNegativeIndex() {
        ComparatorChain<String> chain = new ComparatorChain<>();
        chain.setForwardSort(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetReverseSortWithNegativeIndex() {
        ComparatorChain<String> chain = new ComparatorChain<>();
        chain.setReverseSort(-1);
    }

    // ========== Constructor Tests ==========

    @Test
    public void testConstructorWithList() {
        List<Comparator<String>> comparators = new LinkedList<>();
        comparators.add(String::compareTo);
        
        ComparatorChain<String> chain = new ComparatorChain<>(comparators);
        assertEquals("Chain should have same size as input list", 1, chain.size());
    }

    @Test
    public void testConstructorWithListAndBitSet() {
        List<Comparator<String>> comparators = new LinkedList<>();
        comparators.add(String::compareTo);
        BitSet sortOrder = new BitSet();
        sortOrder.set(0); // Set reverse sort for first comparator
        
        ComparatorChain<String> chain = new ComparatorChain<>(comparators, sortOrder);
        
        int result = chain.compare("a", "b");
        assertTrue("Should use reverse sort as specified by BitSet", result > 0);
    }

    // ========== Null Handling Tests ==========

    @Test(expected = NullPointerException.class)
    public void testConstructorWithNullList() {
        new ComparatorChain<String>((List<Comparator<String>>) null);
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullComparatorToNullBackedChain() {
        // Create chain with null internal list
        ComparatorChain<String> chain = new ComparatorChain<>((List<Comparator<String>>) null, new BitSet());
        chain.addComparator(String::compareTo);
    }

    @Test(expected = NullPointerException.class)
    public void testCompareWithNullComparator() {
        ComparatorChain<String> chain = new ComparatorChain<>((Comparator<String>) null, false);
        chain.compare("a", "b");
    }

    // ========== Equals and HashCode Tests ==========

    @Test
    public void testEqualsWithSameConfiguration() {
        ComparatorChain<String> chain1 = new ComparatorChain<>();
        ComparatorChain<String> chain2 = new ComparatorChain<>();
        
        assertTrue("Empty chains should be equal", chain1.equals(chain2));
    }

    @Test
    public void testEqualsWithDifferentConfiguration() {
        ComparatorChain<String> emptyChain = new ComparatorChain<>();
        ComparatorChain<String> chainWithComparator = new ComparatorChain<>(String::compareTo);
        
        assertFalse("Chains with different configurations should not be equal", 
                   emptyChain.equals(chainWithComparator));
    }

    @Test
    public void testEqualsWithNull() {
        ComparatorChain<String> chain = new ComparatorChain<>();
        assertFalse("Chain should not equal null", chain.equals(null));
    }

    @Test
    public void testEqualsWithDifferentType() {
        ComparatorChain<String> chain = new ComparatorChain<>();
        assertFalse("Chain should not equal string", chain.equals("not a comparator"));
    }

    @Test
    public void testHashCodeConsistency() {
        ComparatorChain<String> chain = new ComparatorChain<>();
        int hashCode1 = chain.hashCode();
        int hashCode2 = chain.hashCode();
        
        assertEquals("Hash code should be consistent", hashCode1, hashCode2);
    }

    @Test
    public void testHashCodeWithNullInternals() {
        // Test hash code calculation when internal structures might be null
        ComparatorChain<String> chain = new ComparatorChain<>((List<Comparator<String>>) null, null);
        // Should not throw exception
        chain.hashCode();
    }
}