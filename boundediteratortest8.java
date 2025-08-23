package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for BoundedIterator.
 * This class focuses on specific behaviors not covered by the abstract test suite.
 *
 * @param <E> the type of element being tested.
 */
@DisplayName("BoundedIterator")
public class BoundedIteratorTest<E> extends AbstractIteratorTest<E> {

    private final String[] testArray = { "a", "b", "c", "d", "e", "f", "g" };
    private List<E> testList;

    @Override
    public Iterator<E> makeEmptyIterator() {
        return new BoundedIterator<>(Collections.<E>emptyList().iterator(), 0, 10);
    }

    @Override
    public Iterator<E> makeObject() {
        // BoundedIterator with offset=1 and max=(size-1) will iterate from the 2nd element to the end.
        return new BoundedIterator<>(new ArrayList<>(testList).iterator(), 1, testList.size() - 1);
    }

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() {
        testList = Arrays.asList((E[]) testArray);
    }

    /**
     * Verifies that calling remove() on the BoundedIterator correctly removes the element
     * from the underlying collection, and that the iterator continues to function correctly afterward.
     */
    @Test
    @DisplayName("remove() should modify the underlying collection")
    void removeShouldModifyUnderlyingCollection() {
        // --- Arrange ---
        // Create a mutable copy of the list, as Arrays.asList() is fixed-size.
        final List<E> underlyingList = new ArrayList<>(testList);

        final long offset = 1; // Skips "a"
        final long maxElements = 5; // Will iterate over "b", "c", "d", "e", "f"
        final BoundedIterator<E> boundedIterator = new BoundedIterator<>(underlyingList.iterator(), offset, maxElements);

        final E elementToRemove = testList.get(1); // "b"
        assertEquals("b", elementToRemove, "The first element to be iterated should be 'b'");

        // --- Act ---
        // Advance to the first element and remove it.
        assertTrue(boundedIterator.hasNext(), "Iterator should have elements before removal.");
        assertEquals(elementToRemove, boundedIterator.next(), "Iterator should return the correct first element.");
        boundedIterator.remove();

        // --- Assert ---
        // 1. Verify the element was removed from the underlying collection.
        assertFalse(underlyingList.contains(elementToRemove), "Element 'b' should have been removed from the underlying list.");
        assertEquals(testArray.length - 1, underlyingList.size(), "Underlying list size should decrease by one.");

        // 2. Verify the iterator continues with the correct remaining elements.
        @SuppressWarnings("unchecked")
        final List<E> expectedRemainingElements = Arrays.asList((E[]) new String[]{"c", "d", "e", "f"});
        final List<E> actualRemainingElements = new ArrayList<>();
        boundedIterator.forEachRemaining(actualRemainingElements::add);

        assertEquals(expectedRemainingElements, actualRemainingElements, "Iterator should yield the correct sequence of elements after removal.");

        // 3. Verify the iterator is exhausted.
        assertFalse(boundedIterator.hasNext(), "Iterator should be exhausted after consuming all elements.");
        assertThrows(NoSuchElementException.class, boundedIterator::next, "Calling next() on an exhausted iterator should throw.");
    }
}