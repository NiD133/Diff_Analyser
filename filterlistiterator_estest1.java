package org.apache.commons.collections4.iterators;

import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.ListIterator;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.junit.Test;

/**
 * Tests for {@link FilterListIterator}.
 * This class contains a test case demonstrating iterator invalidation.
 */
public class FilterListIterator_ESTestTest1 { // Note: A more descriptive name like FilterListIteratorTest would be better.

    /**
     * Tests that the iterator fails fast with a ConcurrentModificationException
     * if the underlying collection is modified after the iterator is created.
     */
    @Test
    public void hasPreviousShouldThrowConcurrentModificationExceptionWhenUnderlyingListIsModified() {
        // Arrange: Set up a list and a FilterListIterator that wraps it.
        final LinkedList<Integer> underlyingList = new LinkedList<>(Arrays.asList(10, 20));
        final ListIterator<Integer> underlyingIterator = underlyingList.listIterator();

        // The specific predicate is not important for this test, so we use one that accepts all elements for clarity.
        final Predicate<Integer> anyPredicate = TruePredicate.truePredicate();
        final FilterListIterator<Integer> filterIterator = new FilterListIterator<>(underlyingIterator, anyPredicate);

        // Act: Modify the underlying list directly, which should invalidate the iterator.
        underlyingList.add(30);

        // Assert: Expect a ConcurrentModificationException when trying to use the iterator.
        try {
            filterIterator.hasPrevious();
            fail("A ConcurrentModificationException should have been thrown due to list modification.");
        } catch (final ConcurrentModificationException e) {
            // This is the expected behavior. The test passes.
        }
    }
}