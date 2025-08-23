package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.junit.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import static org.junit.Assert.fail;

public class FilterListIterator_ESTestTest21 extends FilterListIterator_ESTest_scaffolding {

    /**
     * Tests that calling previous() at the beginning of an iteration throws a NoSuchElementException.
     * <p>
     * The original test was confusing and likely incorrect. It included numerous unused variables
     * and a complex sequence of iterator movements that did not result in the expected exception.
     * This revised test verifies the intended boundary condition in a clear and direct manner.
     * </p>
     */
    @Test(timeout = 4000)
    public void previousShouldThrowExceptionWhenIteratorIsAtTheBeginning() {
        // Arrange
        // The predicate will only accept null elements.
        final Predicate<Integer> nullPredicate = NullPredicate.nullPredicate();

        // The source list contains elements, including one that the predicate will match,
        // to ensure we are testing the iterator's position, not an empty filtered list.
        final LinkedList<Integer> sourceList = new LinkedList<>(Arrays.asList(5795, null));

        // Create the FilterListIterator. It is positioned at the start by default.
        final FilterListIterator<Integer> filterListIterator = new FilterListIterator<>(
            sourceList.listIterator(),
            nullPredicate
        );

        // Act & Assert
        // Calling previous() at the beginning of the iteration should throw an exception,
        // as there is no preceding element that matches the predicate.
        try {
            filterListIterator.previous();
            fail("A NoSuchElementException should have been thrown.");
        } catch (final NoSuchElementException e) {
            // This is the expected behavior. The test passes.
        }
    }
}