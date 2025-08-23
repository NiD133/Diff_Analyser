package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertNull;

import java.util.Collections;
import java.util.List;

/**
 * Contains tests for the LoopingListIterator class, focusing on its looping behavior.
 */
public class LoopingListIteratorTest {

    /**
     * Verifies that calling previous() on a newly created iterator
     * loops around to the end of the list and returns the last element.
     */
    @Test
    public void previousOnNewIteratorShouldLoopAroundToReturnLastElement() {
        // Arrange: Create a list with a single null element. A new iterator
        // is positioned before the first element (at index 0).
        final List<String> listWithSingleItem = Collections.singletonList(null);
        final LoopingListIterator<String> loopingIterator = new LoopingListIterator<>(listWithSingleItem);

        // Act: Call previous(), which should cause the iterator to wrap around.
        final String previousElement = loopingIterator.previous();

        // Assert: The returned element should be the last (and only) element from the list.
        assertNull("previous() should have returned the null element from the list.", previousElement);
    }
}