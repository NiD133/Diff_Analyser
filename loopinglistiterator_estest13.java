package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertThrows;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;

/**
 * Tests for {@link LoopingListIterator} to ensure it correctly handles
 * concurrent modifications of its underlying list.
 */
public class LoopingListIteratorTest {

    /**
     * Verifies that a {@link ConcurrentModificationException} is thrown
     * when the iterator's {@code add} method is called after the
     * underlying list has been modified externally (i.e., not through the iterator).
     * This is the expected "fail-fast" behavior of Java collections.
     */
    @Test
    public void addShouldThrowConcurrentModificationExceptionWhenListIsModifiedExternally() {
        // Arrange: Create a list with an element and an iterator for it.
        final List<String> list = new LinkedList<>();
        list.add("A");
        final LoopingListIterator<String> iterator = new LoopingListIterator<>(list);

        // Act: Modify the list directly, bypassing the iterator.
        // This action invalidates the internal state of the iterator.
        list.add("B");

        // Assert: Verify that the next attempt to use the iterator's add method
        // results in a ConcurrentModificationException.
        assertThrows(ConcurrentModificationException.class, () -> {
            iterator.add("C");
        });
    }
}