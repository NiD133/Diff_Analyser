package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Tests for the {@link FilterListIterator} class, focusing on its behavior
 * when the underlying iterator is not properly initialized.
 */
public class FilterListIteratorTest {

    /**
     * Verifies that calling previous() on a FilterListIterator initialized with a null
     * underlying iterator throws a NoSuchElementException.
     *
     * This is the expected behavior, as there is no preceding element to return.
     */
    @Test(expected = NoSuchElementException.class)
    public void previousShouldThrowExceptionWhenUnderlyingIteratorIsNull() {
        // Arrange: Create a FilterListIterator with a null delegate iterator.
        // The explicit cast to ListIterator is needed to resolve constructor ambiguity.
        final FilterListIterator<Object> filterListIterator =
                new FilterListIterator<>((ListIterator<Object>) null);

        // Act: Attempt to retrieve the previous element.
        // The @Test(expected=...) annotation asserts that a NoSuchElementException is thrown.
        filterListIterator.previous();
    }
}