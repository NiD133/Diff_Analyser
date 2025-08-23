package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.NullPredicate;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertFalse;

public class FilterListIterator_ESTestTest25 extends FilterListIterator_ESTest_scaffolding {

    /**
     * Tests that calling previous() throws a NoSuchElementException
     * when there are no preceding elements that match the predicate.
     */
    @Test(expected = NoSuchElementException.class)
    public void previousShouldThrowExceptionWhenNoMatchingPreviousElement() {
        // Arrange
        // A source list containing a single, non-null element.
        final List<Integer> sourceList = Collections.singletonList(5795);

        // A predicate that only accepts null values, so it will not match the element.
        final Predicate<Integer> isNullPredicate = NullPredicate.nullPredicate();

        // A ListIterator positioned at the end of the list to allow backward iteration.
        final ListIterator<Integer> listIterator = sourceList.listIterator(sourceList.size());

        // The FilterListIterator under test.
        final FilterListIterator<Integer> filteredIterator = new FilterListIterator<>(listIterator, isNullPredicate);

        // Sanity check: The iterator should report that it has no previous matching element.
        assertFalse("Precondition failed: hasPrevious() should be false.", filteredIterator.hasPrevious());

        // Act & Assert
        // Calling previous() should throw an exception because no matching element can be found.
        // The expected exception is declared in the @Test annotation.
        filteredIterator.previous();
    }
}