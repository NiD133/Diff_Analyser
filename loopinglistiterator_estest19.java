package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Contains tests for the {@link LoopingListIterator}.
 */
public class LoopingListIteratorTest {

    /**
     * Tests that calling previous() on an iterator created with an empty list
     * throws a NoSuchElementException, as there is no element to retrieve.
     */
    @Test(expected = NoSuchElementException.class)
    public void previousOnEmptyListShouldThrowNoSuchElementException() {
        // Arrange: Create a LoopingListIterator with an empty list.
        final List<Object> emptyList = Collections.emptyList();
        final LoopingListIterator<Object> iterator = new LoopingListIterator<>(emptyList);

        // Act: Attempt to get the previous element, which should fail.
        iterator.previous();

        // Assert: The expected exception is verified by the @Test(expected=...) annotation.
    }
}