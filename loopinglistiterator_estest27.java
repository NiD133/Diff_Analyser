package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

/**
 * Contains tests for the {@link LoopingListIterator}.
 */
public class LoopingListIteratorTest {

    /**
     * Tests that hasPrevious() returns true for a new iterator on a non-empty list.
     * A standard ListIterator would return false, but a LoopingListIterator should
     * be able to "loop around" to the end of the list.
     */
    @Test
    public void testHasPreviousReturnsTrueForNonEmptyListAtStart() {
        // Arrange: Create a list with a single element and a looping iterator for it.
        final List<String> list = Collections.singletonList("A");
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);

        // Act & Assert: Verify that hasPrevious() is true from the start.
        assertTrue(
            "A new iterator for a non-empty list should report having a previous element",
            iterator.hasPrevious()
        );
    }
}