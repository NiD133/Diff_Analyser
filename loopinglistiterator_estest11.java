package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertThrows;

/**
 * Tests for {@link LoopingListIterator} focusing on concurrent modification scenarios.
 */
public class LoopingListIteratorTest {

    /**
     * Verifies that the iterator fails fast with a ConcurrentModificationException
     * if the underlying list is structurally modified after the iterator is created.
     */
    @Test
    public void nextShouldThrowConcurrentModificationExceptionWhenListIsModifiedExternally() {
        // Arrange
        List<String> underlyingList = new LinkedList<>();
        underlyingList.add("A");
        LoopingListIterator<String> loopingIterator = new LoopingListIterator<>(underlyingList);

        // Act: Modify the underlying list directly, after the iterator has been created.
        underlyingList.add("B");

        // Assert: The next call to the iterator should throw an exception.
        assertThrows(ConcurrentModificationException.class, () -> {
            loopingIterator.next();
        });
    }
}