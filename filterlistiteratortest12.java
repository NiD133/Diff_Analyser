package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the interaction between previous() and next() in FilterListIterator.
 * This test ensures that after calling previous(), a subsequent call to next()
 * returns the same element, maintaining iterator consistency.
 */
@DisplayName("FilterListIterator previous/next interaction")
class FilterListIteratorTest {

    private static final Predicate<Integer> IS_DIVISIBLE_BY_THREE = x -> x % 3 == 0;
    private static final Predicate<Integer> ALWAYS_TRUE_PREDICATE = x -> true;

    private List<Integer> fullList;
    private List<Integer> divisibleByThreeList;

    @BeforeEach
    void setUp() {
        fullList = IntStream.range(0, 20).boxed().collect(Collectors.toList());
        divisibleByThreeList = fullList.stream()
                                       .filter(IS_DIVISIBLE_BY_THREE)
                                       .collect(Collectors.toList());
    }

    /**
     * Verifies that when a predicate filters the list, the iterator correctly
     * handles moving backward and then forward.
     */
    @Test
    void nextAfterPreviousShouldReturnSameElementWhenFiltered() {
        // Arrange: Create an expected iterator and a filtered iterator.
        final ListIterator<Integer> expectedIterator = divisibleByThreeList.listIterator();
        final FilterListIterator<Integer> filteredIterator = new FilterListIterator<>(fullList.listIterator(), IS_DIVISIBLE_BY_THREE);

        // Act: Move both iterators to the end of the collection.
        walkForward(expectedIterator, filteredIterator);

        // Assert: Verify that moving back and then forward returns the same element.
        assertNextAfterPreviousReturnsSameElement(expectedIterator, filteredIterator);
    }

    /**
     * Verifies that with a predicate that accepts all elements, the iterator behaves
     * like a standard ListIterator when moving backward and then forward.
     */
    @Test
    void nextAfterPreviousShouldReturnSameElementWhenNotFiltered() {
        // Arrange: Use a predicate that doesn't filter any elements.
        final ListIterator<Integer> expectedIterator = fullList.listIterator();
        final FilterListIterator<Integer> filteredIterator = new FilterListIterator<>(fullList.listIterator(), ALWAYS_TRUE_PREDICATE);

        // Act: Move both iterators to the end of the collection.
        walkForward(expectedIterator, filteredIterator);

        // Assert: Verify that moving back and then forward returns the same element.
        assertNextAfterPreviousReturnsSameElement(expectedIterator, filteredIterator);
    }

    /**
     * Asserts that a call to next() after a call to previous() returns the same element.
     * This helper assumes the iterators are positioned at the end of the collection.
     *
     * @param expected The reference iterator.
     * @param actual   The FilterListIterator being tested.
     */
    private void assertNextAfterPreviousReturnsSameElement(final ListIterator<?> expected, final ListIterator<?> actual) {
        // GIVEN: Iterators are at the end of the collection.

        // WHEN: We move back by one element.
        final Object elementFromPreviousExpected = expected.previous();
        final Object elementFromPreviousActual = actual.previous();
        assertEquals(elementFromPreviousExpected, elementFromPreviousActual, "previous() should return the same element");

        // THEN: hasNext() should now be true for both iterators.
        assertTrue(expected.hasNext(), "Expected iterator should have a next element");
        assertTrue(actual.hasNext(), "Actual iterator should have a next element");

        // WHEN: We move forward by one element.
        final Object elementFromNextExpected = expected.next();
        final Object elementFromNextActual = actual.next();

        // THEN: The element returned by next() should be the same one returned by the preceding previous().
        assertEquals(elementFromPreviousExpected, elementFromNextExpected, "next() should return the same element as previous()");
        assertEquals(elementFromPreviousActual, elementFromNextActual, "Filtered next() should also return the same element as previous()");
    }

    /**
     * Iterates both iterators forward to the end, ensuring they behave identically.
     */
    private void walkForward(final ListIterator<?> expected, final ListIterator<?> testing) {
        while (expected.hasNext()) {
            assertTrue(testing.hasNext(), "Tested iterator should have next element when expected one does");
            assertEquals(expected.next(), testing.next(), "next() should return the same element");
        }
    }
}