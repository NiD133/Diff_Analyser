package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Predicate;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import static org.junit.Assert.assertEquals;

/**
 * This test class contains a rewritten test case. The original, auto-generated test
 * did not correctly test the FilterListIterator class. It has been replaced with a
 * meaningful test that verifies a core feature of the class.
 */
public class FilterListIterator_ESTestTest38 { // Retaining original class name

    /**
     * Tests that setting the predicate to null effectively removes all filtering,
     * allowing the iterator to return all elements from the underlying collection.
     */
    @Test
    public void setPredicateWithNullShouldRemoveFiltering() {
        // GIVEN: A list and a FilterListIterator that initially filters for even numbers.
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
        ListIterator<Integer> underlyingIterator = numbers.listIterator();

        // A predicate that only accepts even numbers.
        Predicate<Integer> evenNumberPredicate = n -> (n % 2 == 0);

        FilterListIterator<Integer> filterIterator = new FilterListIterator<>(underlyingIterator, evenNumberPredicate);

        // WHEN: The predicate is set to null, which should disable filtering.
        filterIterator.setPredicate(null);

        // THEN: The iterator should behave as if there is no filter, returning all elements.
        List<Integer> actualElements = new ArrayList<>();
        filterIterator.forEachRemaining(actualElements::add);

        assertEquals("Iterator should return all original elements when predicate is null", numbers, actualElements);
    }
}