package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.ListIterator;

import static org.junit.Assert.assertThrows;

/**
 * This test class contains a test case from a larger, auto-generated suite.
 * The original class name was FilterListIterator_ESTestTest29.
 */
public class FilterListIteratorConcurrencyTest {

    /**
     * Tests that the iterator exhibits fail-fast behavior.
     *
     * A ConcurrentModificationException should be thrown if the underlying list is
     * modified after the iterator is created and before an operation like hasNext() is called.
     * This confirms that the decorator correctly propagates the exception from the wrapped iterator.
     */
    @Test
    public void hasNextShouldThrowConcurrentModificationExceptionWhenUnderlyingListIsModified() {
        // Arrange: Create a list and a FilterListIterator wrapping its iterator.
        final LinkedList<String> sourceList = new LinkedList<>();
        final ListIterator<String> originalIterator = sourceList.listIterator();
        final FilterListIterator<String> filterIterator = new FilterListIterator<>(originalIterator);

        // Act: Modify the underlying list *after* the iterator has been created.
        // This invalidates the state of the original iterator.
        sourceList.add("new element");

        // Assert: Expect a ConcurrentModificationException when trying to use the filter iterator.
        assertThrows(ConcurrentModificationException.class, filterIterator::hasNext);
    }
}