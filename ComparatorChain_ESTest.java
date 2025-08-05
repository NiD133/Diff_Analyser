package org.apache.commons.collections4.comparators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Comparator;
import java.util.List;
import org.junit.Test;

/**
 * Tests for {@link ComparatorChain}.
 * This class provides understandable, behavior-driven tests for the main features
 * of the ComparatorChain, including comparison logic, modification locking, and exception handling.
 */
public class ComparatorChainTest {

    private final Comparator<Integer> naturalOrder = Comparator.naturalOrder();
    private final Comparator<Integer> reverseOrder = Comparator.reverseOrder();

    // --- Constructor Tests ---

    @Test
    public void constructor_noArgs_shouldCreateEmptyChain() {
        // Arrange & Act
        final ComparatorChain<Object> chain = new ComparatorChain<>();

        // Assert
        assertEquals("An empty chain should have size 0", 0, chain.size());
        assertFalse("A new chain should not be locked", chain.isLocked());
    }

    @Test
    public void constructor_fromList_shouldAddAllComparatorsInForwardOrder() {
        // Arrange
        final List<Comparator<Integer>> comparators = new ArrayList<>();
        comparators.add(naturalOrder);
        comparators.add(reverseOrder);

        // Act
        final ComparatorChain<Integer> chain = new ComparatorChain<>(comparators);

        // Assert
        assertEquals("Chain should contain all comparators from the list", 2, chain.size());
        // The first comparator (natural order) should be used.
        assertTrue("Default sort order should be forward", chain.compare(5, 10) < 0);
    }

    @Test
    public void constructor_fromListAndBitSet_shouldSetCorrectSortOrders() {
        // Arrange
        final List<Comparator<Integer>> comparators = new ArrayList<>();
        comparators.add(naturalOrder); // Index 0: forward
        comparators.add(naturalOrder); // Index 1: reverse

        final BitSet bitSet = new BitSet();
        bitSet.set(1); // Set second comparator (index 1) to reverse sort

        // Act
        final ComparatorChain<Integer> chain = new ComparatorChain<>(comparators, bitSet);

        // Assert
        assertEquals(2, chain.size());
        // First comparator returns 0 for equal-length strings, so the second (reversed) is used.
        // For (1, 2), natural order is < 0, so reversed is > 0.
        assertTrue("Sort order should respect the provided BitSet", chain.compare(1, 2) > 0);
    }

    // --- Comparison Logic Tests ---

    @Test
    public void compare_onEmptyChain_shouldThrowUnsupportedOperationException() {
        // Arrange
        final ComparatorChain<Object> chain = new ComparatorChain<>();

        // Act & Assert
        try {
            chain.compare(new Object(), new Object());
            fail("compare() on an empty chain should have thrown UnsupportedOperationException");
        } catch (final UnsupportedOperationException e) {
            assertEquals("ComparatorChains must contain at least one Comparator", e.getMessage());
        }
    }

    @Test
    public void compare_withSingleForwardComparator_shouldReturnCorrectResult() {
        // Arrange
        final ComparatorChain<Integer> chain = new ComparatorChain<>(naturalOrder);

        // Act & Assert
        assertTrue("Comparing 1 to 2 should be negative", chain.compare(1, 2) < 0);
        assertEquals("Comparing 1 to 1 should be zero", 0, chain.compare(1, 1));
        assertTrue("Comparing 2 to 1 should be positive", chain.compare(2, 1) > 0);
    }

    @Test
    public void compare_withSingleReverseComparator_shouldReturnInvertedResult() {
        // Arrange
        final ComparatorChain<Integer> chain = new ComparatorChain<>(naturalOrder, true); // reverse = true

        // Act & Assert
        assertTrue("Comparing 1 to 2 in reverse should be positive", chain.compare(1, 2) > 0);
        assertEquals("Comparing 1 to 1 should be zero", 0, chain.compare(1, 1));
        assertTrue("Comparing 2 to 1 in reverse should be negative", chain.compare(2, 1) < 0);
    }

    @Test
    public void compare_withMultipleComparators_shouldUseNextComparatorWhenPreviousIsEqual() {
        // Arrange
        final Comparator<String> lengthComparator = Comparator.comparingInt(String::length);
        final Comparator<String> lexicalComparator = Comparator.naturalOrder();

        final ComparatorChain<String> chain = new ComparatorChain<>(lengthComparator);
        chain.addComparator(lexicalComparator);

        // Act & Assert
        // "a" and "b" have the same length, so the second (lexical) comparator is used.
        assertTrue(chain.compare("a", "b") < 0);

        // "apple" and "banana" have different lengths, so the first comparator is used.
        assertTrue(chain.compare("apple", "banana") < 0);
    }

    @Test
    public void compare_withMultipleComparators_shouldStopAtFirstNonZeroResult() {
        // Arrange
        final Comparator<String> firstComparator = (s1, s2) -> 1;  // Always returns a non-zero value
        final Comparator<String> secondComparator = (s1, s2) -> -1; // Should never be called

        final ComparatorChain<String> chain = new ComparatorChain<>(firstComparator);
        chain.addComparator(secondComparator);

        // Act
        final int result = chain.compare("a", "b");

        // Assert
        assertEquals("Should return result from the first non-zero comparator", 1, result);
    }

    // --- Chain Modification and Locking Tests ---

    @Test
    public void isLocked_shouldReturnTrueAfterCompareIsCalled() {
        // Arrange
        final ComparatorChain<Integer> chain = new ComparatorChain<>(naturalOrder);
        assertFalse("A new chain should not be locked", chain.isLocked());

        // Act
        chain.compare(1, 2);

        // Assert
        assertTrue("Chain should be locked after the first comparison", chain.isLocked());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void addComparator_onLockedChain_shouldThrowException() {
        // Arrange
        final ComparatorChain<Integer> chain = new ComparatorChain<>(naturalOrder);
        chain.compare(1, 2); // Lock the chain

        // Act
        chain.addComparator(reverseOrder); // Should throw
    }

    @Test(expected = UnsupportedOperationException.class)
    public void setComparator_onLockedChain_shouldThrowException() {
        // Arrange
        final ComparatorChain<Integer> chain = new ComparatorChain<>(naturalOrder);
        chain.compare(1, 2); // Lock the chain

        // Act
        chain.setComparator(0, reverseOrder); // Should throw
    }

    @Test(expected = UnsupportedOperationException.class)
    public void setReverseSort_onLockedChain_shouldThrowException() {
        // Arrange
        final ComparatorChain<Integer> chain = new ComparatorChain<>(naturalOrder);
        chain.compare(1, 2); // Lock the chain

        // Act
        chain.setReverseSort(0); // Should throw
    }

    @Test
    public void setSortOrder_onUnlockedChain_shouldChangeComparisonResult() {
        // Arrange
        final ComparatorChain<Integer> chain = new ComparatorChain<>(naturalOrder);
        assertTrue("Default sort order should be forward", chain.compare(1, 2) < 0);
        
        // Reset chain to test modification
        final ComparatorChain<Integer> modifiableChain = new ComparatorChain<>(naturalOrder);

        // Act: set to reverse
        modifiableChain.setReverseSort(0);

        // Assert
        assertTrue("After setReverseSort, order should be reversed", modifiableChain.compare(1, 2) > 0);
    }

    // --- Exception Handling Tests ---

    @Test(expected = RuntimeException.class)
    public void compare_whenUnderlyingComparatorThrowsException_shouldPropagate() {
        // Arrange
        final Comparator<Object> faultyComparator = (o1, o2) -> {
            throw new RuntimeException("Comparator error!");
        };
        final ComparatorChain<Object> chain = new ComparatorChain<>(faultyComparator);

        // Act
        chain.compare(new Object(), new Object()); // Should re-throw the RuntimeException
    }

    @Test(expected = NullPointerException.class)
    public void compare_withNullComparatorInChain_shouldThrowNullPointerException() {
        // Arrange: The constructor allows a null comparator to be added.
        final Comparator<Object> nullComparator = null;
        final ComparatorChain<Object> chain = new ComparatorChain<>(nullComparator);

        // Act: The NPE is thrown when the chain attempts to use the null comparator.
        chain.compare(new Object(), new Object());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void setComparator_withInvalidIndex_shouldThrowIndexOutOfBounds() {
        new ComparatorChain<>().setComparator(0, naturalOrder);
    }

    // --- equals() and hashCode() Tests ---

    @Test
    public void equals_shouldReturnTrueForIdenticalChains() {
        // Arrange
        final ComparatorChain<Integer> chain1 = new ComparatorChain<>(naturalOrder);
        chain1.addComparator(reverseOrder, true);

        final ComparatorChain<Integer> chain2 = new ComparatorChain<>(naturalOrder);
        chain2.addComparator(reverseOrder, true);

        // Assert
        assertTrue("Two identical chains should be equal", chain1.equals(chain2));
        assertEquals("Hashcodes of two identical chains should be equal", chain1.hashCode(), chain2.hashCode());
    }

    @Test
    public void equals_shouldReturnFalseForDifferentChains() {
        // Arrange
        final ComparatorChain<Integer> chain1 = new ComparatorChain<>(naturalOrder);
        final ComparatorChain<Integer> chain2 = new ComparatorChain<>(reverseOrder);
        final ComparatorChain<Integer> chain3 = new ComparatorChain<>(naturalOrder, true);

        // Assert
        assertFalse("Chains with different comparators should not be equal", chain1.equals(chain2));
        assertFalse("Chains with different sort orders should not be equal", chain1.equals(chain3));
    }

    @Test
    public void equals_shouldReturnFalseForNullOrDifferentType() {
        // Arrange
        final ComparatorChain<Object> chain = new ComparatorChain<>();

        // Assert
        assertFalse("equals(null) should return false", chain.equals(null));
        assertFalse("equals() with a different object type should return false", chain.equals("a string"));
    }
}