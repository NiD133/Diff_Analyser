package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

/**
 * Contains tests for the LoopingListIterator class.
 * This refactored test focuses on the scenario of removing elements.
 */
public class LoopingListIteratorTest {

    /**
     * Tests that the iterator correctly handles the removal of all elements
     * one by one while iterating forward. After all elements are removed,
     * the iterator should become empty and behave accordingly.
     */
    @Test
    void whenRemovingAllElementsWhileIterating_thenIteratorBecomesEmpty() {
        // Given a list with three elements and a looping iterator
        final List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);

        // When: We retrieve and remove the first element ("a")
        assertEquals("a", iterator.next());
        iterator.remove();
        // Then: The list should contain the remaining two elements
        assertEquals(2, list.size(), "List size should be 2 after first removal");
        assertEquals(Arrays.asList("b", "c"), list, "List should contain remaining elements");
        assertTrue(iterator.hasNext(), "Iterator should still have elements");

        // When: We retrieve and remove the second element ("b")
        assertEquals("b", iterator.next());
        iterator.remove();
        // Then: The list should contain the final element
        assertEquals(1, list.size(), "List size should be 1 after second removal");
        assertEquals(Collections.singletonList("c"), list, "List should contain the last element");
        assertTrue(iterator.hasNext(), "Iterator should still have the last element");

        // When: We retrieve and remove the final element ("c")
        assertEquals("c", iterator.next());
        iterator.remove();
        // Then: The list should now be empty
        assertEquals(0, list.size(), "List should be empty after final removal");
        assertTrue(list.isEmpty(), "List should be empty");

        // And: The iterator should correctly report that it is empty
        assertFalse(iterator.hasNext(), "Iterator should now be empty");
        assertThrows(NoSuchElementException.class, iterator::next,
                "next() should throw when the iterator is empty");
    }
}