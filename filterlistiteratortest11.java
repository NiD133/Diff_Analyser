package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A comprehensive test for FilterListIterator that verifies its behavior
 * under various complex traversal patterns.
 */
@DisplayName("FilterListIterator Comprehensive Walk Test")
class FilterListIteratorTest {

    private static final Predicate<Integer> ODD_PREDICATE = number -> number % 2 != 0;

    private List<Integer> sourceList;
    private List<Integer> expectedOddNumbers;
    private final Random random = new Random();

    @BeforeEach
    public void setUp() {
        sourceList = IntStream.range(0, 20).boxed().collect(Collectors.toList());
        expectedOddNumbers = sourceList.stream()
                                       .filter(ODD_PREDICATE)
                                       .collect(Collectors.toList());
    }

    /**
     * Tests that a FilterListIterator behaves identically to a standard ListIterator
     * on a pre-filtered list across a wide range of traversal scenarios.
     */
    @Test
    void testComprehensiveWalkWithOddPredicate() {
        // Arrange: Create the iterator under test and an expected iterator
        final ListIterator<Integer> expectedIterator = expectedOddNumbers.listIterator();
        final FilterListIterator<Integer> filteredIterator =
            new FilterListIterator<>(sourceList.listIterator(), ODD_PREDICATE);

        // Act & Assert: Verify behavior through multiple traversal patterns
        assertFullForwardAndBackwardWalks(expectedIterator, filteredIterator);
        assertAlternatingForwardAndBackwardWalk(expectedIterator, filteredIterator);
        assertComplexBidirectionalWalk(expectedIterator, filteredIterator);
        assertRandomWalk(expectedIterator, filteredIterator);
    }

    /**
     * Verifies the iterator by walking it all the way forward, then all the way back.
     */
    private void assertFullForwardAndBackwardWalks(final ListIterator<?> expected, final ListIterator<?> testing) {
        // Walk all the way forward
        while (expected.hasNext()) {
            assertTrue(testing.hasNext(), "hasNext() should be true during forward walk");
            assertEquals(expected.nextIndex(), testing.nextIndex(), "nextIndex() should match during forward walk");
            assertEquals(expected.previousIndex(), testing.previousIndex(), "previousIndex() should match during forward walk");
            assertEquals(expected.next(), testing.next(), "next() should return the same element during forward walk");
        }

        // Walk all the way back
        while (expected.hasPrevious()) {
            assertTrue(testing.hasPrevious(), "hasPrevious() should be true during backward walk");
            assertEquals(expected.nextIndex(), testing.nextIndex(), "nextIndex() should match during backward walk");
            assertEquals(expected.previousIndex(), testing.previousIndex(), "previousIndex() should match during backward walk");
            assertEquals(expected.previous(), testing.previous(), "previous() should return the same element during backward walk");
        }
    }

    /**
     * Verifies the iterator by repeatedly calling next(), previous(), next().
     */
    private void assertAlternatingForwardAndBackwardWalk(final ListIterator<?> expected, final ListIterator<?> testing) {
        while (expected.hasNext()) {
            assertEquals(expected.next(), testing.next(), "Step 1: next()");
            assertEquals(expected.previous(), testing.previous(), "Step 2: previous()");
            assertEquals(expected.next(), testing.next(), "Step 3: next()");
        }
    }

    /**
     * Verifies the iterator using a complex, nested pattern of forward and backward steps.
     * This stress-tests the iterator's internal state management.
     */
    private void assertComplexBidirectionalWalk(final ListIterator<?> expected, final ListIterator<?> testing) {
        // Reset iterators to the beginning
        while (expected.hasPrevious()) {
            expected.previous();
            testing.previous();
        }

        for (int i = 0; i < expectedOddNumbers.size(); i++) {
            // Walk forward i steps
            for (int j = 0; j < i; j++) {
                assertEquals(expected.next(), testing.next(), "Complex walk: forward step");
            }
            // Walk back i/2 steps
            for (int j = 0; j < i / 2; j++) {
                assertEquals(expected.previous(), testing.previous(), "Complex walk: backward step");
            }
        }
    }

    /**
     * Verifies the iterator by performing a long series of random forward or backward steps.
     */
    private void assertRandomWalk(final ListIterator<?> expected, final ListIterator<?> testing) {
        final StringBuilder walkDescription = new StringBuilder(500);
        for (int i = 0; i < 500; i++) {
            if (random.nextBoolean()) {
                // Step forward
                walkDescription.append(" next() ");
                if (expected.hasNext()) {
                    assertTrue(testing.hasNext(), "hasNext() should be true before random next()");
                    assertEquals(expected.next(), testing.next(), "Random walk failed at: " + walkDescription);
                }
            } else {
                // Step backward
                walkDescription.append(" previous() ");
                if (expected.hasPrevious()) {
                    assertTrue(testing.hasPrevious(), "hasPrevious() should be true before random previous()");
                    assertEquals(expected.previous(), testing.previous(), "Random walk failed at: " + walkDescription);
                }
            }
            assertEquals(expected.nextIndex(), testing.nextIndex(), "nextIndex() mismatch during random walk");
            assertEquals(expected.previousIndex(), testing.previousIndex(), "previousIndex() mismatch during random walk");
        }
    }
}