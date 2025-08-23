package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BoundedIterator} that verify specific behaviors and edge cases,
 * particularly those related to modification operations.
 * <p>
 * This class extends {@link AbstractIteratorTest} to ensure the iterator
 * fully complies with the Iterator contract.
 */
public class BoundedIteratorTest extends AbstractIteratorTest<String> {

    private static final String[] TEST_ELEMENTS = { "a", "b", "c", "d", "e", "f", "g" };

    private List<String> list;

    @BeforeEach
    public void setUp() {
        list = new ArrayList<>(Arrays.asList(TEST_ELEMENTS));
    }

    // --- AbstractIteratorTest Implementation ---

    @Override
    public Iterator<String> makeEmptyIterator() {
        return new BoundedIterator<>(Collections.<String>emptyList().iterator(), 0, 10);
    }

    /**
     * Creates a BoundedIterator that covers a sub-section of the test list,
     * starting at index 1 ("b") and including up to 6 elements.
     * Expected elements: "b", "c", "d", "e", "f", "g".
     */
    @Override
    public Iterator<String> makeObject() {
        // Use a fresh, mutable copy for the abstract test suite.
        final List<String> freshList = new ArrayList<>(list);
        final int offset = 1;
        final int max = list.size() - 1; // 6 elements
        return new BoundedIterator<>(freshList.iterator(), offset, max);
    }

    // --- Custom Test Cases ---

    /**
     * Verifies that calling remove() twice in a row, without an intermediate
     * call to next(), throws an IllegalStateException as per the Iterator contract.
     */
    @Test
    void remove_calledTwiceWithoutNext_throwsIllegalStateException() {
        // Arrange
        final List<String> underlyingList = new ArrayList<>(list);
        final int offset = 1;       // Start iteration from index 1 ("b")
        final int maxItems = 5;     // Iterate over at most 5 elements: "b", "c", "d", "e", "f"
        final Iterator<String> iterator = new BoundedIterator<>(underlyingList.iterator(), offset, maxItems);

        assertTrue(iterator.hasNext(), "Iterator should have elements before starting.");
        iterator.next(); // Consume the first element ("b")

        // Act: The first remove() call after next() is valid.
        iterator.remove();

        // Assert: The second remove() call is invalid and should throw.
        assertThrows(IllegalStateException.class, iterator::remove,
                "Calling remove() again without an intervening next() call should throw.");

        // Verify the state of the underlying list after the valid remove operation.
        final List<String> expectedList = new ArrayList<>(Arrays.asList(TEST_ELEMENTS));
        expectedList.remove("b");
        assertEquals(expectedList, underlyingList, "The element 'b' should have been removed from the list.");
    }
}