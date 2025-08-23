package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for BoundedIterator.
 *
 * @param <E> the type of element in the iterator.
 */
@DisplayName("BoundedIterator Test")
public class BoundedIteratorTest<E> extends AbstractIteratorTest<E> {

    /** The master list of elements for testing. */
    private static final String[] TEST_DATA = { "a", "b", "c", "d", "e", "f", "g" };

    /** A generic list initialized with the test data before each test. */
    private List<E> fullList;

    @SuppressWarnings("unchecked")
    @BeforeEach
    public void setUp() {
        // This unchecked cast is safe because TEST_DATA contains only Strings,
        // and the test framework will instantiate the generic type E as String.
        fullList = Arrays.asList((E[]) TEST_DATA);
    }

    //--- AbstractIteratorTest Methods ---

    @Override
    public Iterator<E> makeEmptyIterator() {
        return new BoundedIterator<>(Collections.<E>emptyList().iterator(), 0, 10);
    }

    /**
     * Creates a BoundedIterator that represents a sub-section of the full list
     * for the abstract test suite.
     * <p>
     * It starts at offset 1 and is limited to the remaining elements.
     * For the test data ["a", ..., "g"], this iterator will yield ["b", ..., "g"].
     */
    @Override
    public Iterator<E> makeObject() {
        final int offset = 1;
        final int max = fullList.size() - 1;
        return new BoundedIterator<>(new ArrayList<>(fullList).iterator(), offset, max);
    }

    //--- Specific BoundedIterator Tests ---

    @Test
    @DisplayName("Should iterate until underlying iterator is exhausted when max is larger than available elements")
    void testIterationStopsAtEndWhenMaxExceedsSize() {
        // Given: A BoundedIterator with an offset and a 'max' count that is greater
        // than the number of remaining elements in the underlying iterator.
        final int offset = 1;
        final int maxCount = 10; // maxCount (10) > remaining elements (6)
        final Iterator<E> boundedIterator = new BoundedIterator<>(fullList.iterator(), offset, maxCount);

        // And: The expected result is the sublist of elements after the offset.
        final List<E> expectedElements = fullList.subList(offset, fullList.size());

        // When: We collect all elements from the iterator.
        final List<E> actualElements = new ArrayList<>();
        boundedIterator.forEachRemaining(actualElements::add);

        // Then: The collected elements should match the expected sublist.
        assertEquals(expectedElements, actualElements,
                "Iterator should return all elements from the offset to the end of the list.");

        // And: The iterator should report that it is exhausted.
        assertFalse(boundedIterator.hasNext(), "hasNext() should return false after full iteration.");
        assertThrows(NoSuchElementException.class, boundedIterator::next,
                "next() should throw NoSuchElementException after iterator is exhausted.");
    }
}