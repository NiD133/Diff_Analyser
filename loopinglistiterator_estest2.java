package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

/**
 * Contains tests for the {@link LoopingListIterator#size()} method.
 */
// The original test class name and hierarchy are preserved.
public class LoopingListIterator_ESTestTest2 extends LoopingListIterator_ESTest_scaffolding {

    /**
     * Tests that the size() method returns the correct number of elements
     * for an iterator based on a non-empty list.
     */
    @Test
    public void testSizeReturnsCorrectSizeForNonEmptyList() {
        // Arrange: Create a list with a single element.
        final List<Integer> list = new LinkedList<>();
        list.add(42); // Use a distinct value to avoid confusion with indices or counts.

        final LoopingListIterator<Integer> iterator = new LoopingListIterator<>(list);

        // Act: Get the size from the iterator.
        final int actualSize = iterator.size();

        // Assert: The iterator's size should match the underlying list's size.
        assertEquals("The iterator's size should be 1 for a list with one element.", 1, actualSize);
    }
}