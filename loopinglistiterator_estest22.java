package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Tests for {@link LoopingListIterator}.
 */
public class LoopingListIteratorTest {

    /**
     * Verifies that calling nextIndex() on an iterator created from an empty list
     * correctly throws a NoSuchElementException, as per the method's contract.
     */
    @Test(expected = NoSuchElementException.class)
    public void nextIndexOnEmptyListShouldThrowNoSuchElementException() {
        // Arrange: Create a LoopingListIterator with an empty list.
        // Using Collections.emptyList() is a standard, clear way to represent this.
        final List<Object> emptyList = Collections.emptyList();
        final LoopingListIterator<Object> iterator = new LoopingListIterator<>(emptyList);

        // Act & Assert: Calling nextIndex() is expected to throw NoSuchElementException.
        // The @Test(expected=...) annotation handles the assertion.
        iterator.nextIndex();
    }
}