package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.Collections;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertThrows;

/**
 * Improved, more understandable tests for {@link FilterListIterator}.
 */
public class FilterListIteratorImprovedTest {

    /**
     * Verifies that calling next() on a FilterListIterator that wraps an
     * empty iterator throws a NoSuchElementException, as per the
     * Iterator contract.
     */
    @Test
    public void nextShouldThrowNoSuchElementExceptionForEmptyIterator() {
        // Arrange: Create a FilterListIterator with an underlying iterator that has no elements.
        // Using an iterator from an empty list is a clear and standard way to set up this condition.
        final ListIterator<Object> emptyListIterator = Collections.emptyList().listIterator();
        final FilterListIterator<Object> filterIterator = new FilterListIterator<>(emptyListIterator);

        // Act & Assert: Verify that calling next() throws the expected exception.
        // The assertThrows construct clearly defines the action (calling next())
        // and the expected outcome (a NoSuchElementException).
        assertThrows(NoSuchElementException.class, filterIterator::next);
    }
}