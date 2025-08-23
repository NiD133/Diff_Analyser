package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.Test;

/**
 * Unit tests for {@link LoopingListIterator}.
 */
public class LoopingListIteratorTest {

    /**
     * Verifies that calling next() on an iterator created from an empty list
     * throws a NoSuchElementException, as the iterator has no elements to loop over.
     */
    @Test
    public void nextShouldThrowNoSuchElementExceptionForEmptyList() {
        // Arrange: Create a LoopingListIterator with an empty list.
        final List<Object> emptyList = Collections.emptyList();
        final LoopingListIterator<Object> iterator = new LoopingListIterator<>(emptyList);

        // Act & Assert: Verify that calling next() throws the expected exception.
        final NoSuchElementException thrown = assertThrows(
            NoSuchElementException.class,
            () -> iterator.next() // The action that is expected to throw.
        );

        // Assert on the exception message to ensure it's informative.
        assertEquals("There are no elements for this iterator to loop on", thrown.getMessage());
    }
}