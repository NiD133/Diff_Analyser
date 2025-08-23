package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * Contains tests for the {@link FilterListIterator} class.
 */
public class FilterListIteratorTest {

    /**
     * Tests that hasPrevious() throws a NullPointerException if the predicate has not been set.
     * <p>
     * When a predicate is not provided, the internal predicate field is null. The hasPrevious()
     * method attempts to call {@code predicate.evaluate()}, which results in an NPE.
     */
    @Test(expected = NullPointerException.class)
    public void testHasPreviousThrowsNullPointerExceptionWhenPredicateNotSet() {
        // Arrange: Create a list with an element so that the underlying iterator's
        // hasPrevious() method returns true.
        final List<String> list = Collections.singletonList("A");
        final ListIterator<String> underlyingIterator = list.listIterator(1); // Position iterator at the end

        // Create a FilterListIterator and set the iterator, but not the predicate.
        final FilterListIterator<String> filterIterator = new FilterListIterator<>();
        filterIterator.setListIterator(underlyingIterator);

        // Act & Assert: Calling hasPrevious() should trigger the NullPointerException
        // because it tries to use the null predicate. The expected exception is
        // declared in the @Test annotation.
        filterIterator.hasPrevious();
    }
}