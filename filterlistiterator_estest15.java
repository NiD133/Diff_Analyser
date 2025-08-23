package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.Collections;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertThrows;

/**
 * Contains tests for the {@link FilterListIterator} class.
 * This class focuses on ensuring correct exception handling for boundary conditions.
 */
public class FilterListIteratorTest {

    /**
     * Verifies that calling previous() on an empty FilterListIterator
     * correctly throws a NoSuchElementException.
     * <p>
     * An iterator can be empty either because its underlying iterator is null
     * or because the underlying iterator has no elements. This test covers the latter case.
     */
    @Test
    public void shouldThrowNoSuchElementExceptionWhenCallingPreviousOnEmptyIterator() {
        // Arrange: Create a FilterListIterator with an empty underlying iterator.
        final ListIterator<String> emptyUnderlyingIterator = Collections.<String>emptyList().listIterator();
        final FilterListIterator<String> filterIterator = new FilterListIterator<>(emptyUnderlyingIterator);

        // Act & Assert: Expect a NoSuchElementException when previous() is called.
        assertThrows(NoSuchElementException.class, filterIterator::previous);
    }

    /**
     * Verifies that calling previous() on a FilterListIterator that has not been
     * initialized with an underlying iterator also throws a NoSuchElementException.
     */
    @Test
    public void shouldThrowNoSuchElementExceptionWhenCallingPreviousOnUninitializedIterator() {
        // Arrange: Create a FilterListIterator using the default constructor,
        // which leaves the underlying iterator as null.
        final FilterListIterator<Object> uninitializedIterator = new FilterListIterator<>();

        // Act & Assert: Expect a NoSuchElementException when previous() is called.
        assertThrows(NoSuchElementException.class, uninitializedIterator::previous);
    }
}