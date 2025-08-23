package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import org.apache.commons.collections4.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for FilterListIterator to ensure it correctly mirrors the behavior of a
 * standard ListIterator when using a predicate that accepts all elements.
 *
 * This suite verifies the iterator's protocol under various traversal patterns.
 */
@DisplayName("FilterListIterator with a 'true' predicate")
public class FilterListIteratorTest {

    private static final int LIST_SIZE = 20;
    private static final int RANDOM_WALK_STEPS = 500;

    private List<Integer> list;
    private Predicate<Integer> truePredicate;
    private final Random random = new Random();

    @BeforeEach
    public void setUp() {
        list = new ArrayList<>();
        for (int i = 0; i < LIST_SIZE; i++) {
            list.add(i);
        }
        truePredicate = obj -> true;
    }

    /**
     * Tests that a full forward traversal of the filtered iterator behaves
     * identically to the underlying iterator.
     */
    @Test
    public void shouldBehaveLikeNormalIteratorOnFullForwardWalk() {
        final ListIterator<Integer> expected = list.listIterator();
        final ListIterator<Integer> filtered = new FilterListIterator<>(list.listIterator(), truePredicate);

        while (expected.hasNext()) {
            assertTrue(filtered.hasNext(), "Filtered iterator should have a next element");
            assertEquals(expected.nextIndex(), filtered.nextIndex(), "Next indices should match");
            assertEquals(expected.previousIndex(), filtered.previousIndex(), "Previous indices should match");
            assertEquals(expected.next(), filtered.next(), "Next elements should match");
        }
        assertFalse(filtered.hasNext(), "Filtered iterator should have no more elements");
    }

    /**
     * Tests that a full backward traversal of the filtered iterator behaves
     * identically to the underlying iterator.
     */
    @Test
    public void shouldBehaveLikeNormalIteratorOnFullBackwardWalk() {
        // Move both iterators to the end of the list
        final ListIterator<Integer> expected = list.listIterator(list.size());
        final ListIterator<Integer> filtered = new FilterListIterator<>(list.listIterator(list.size()), truePredicate);

        while (expected.hasPrevious()) {
            assertTrue(filtered.hasPrevious(), "Filtered iterator should have a previous element");
            assertEquals(expected.nextIndex(), filtered.nextIndex(), "Next indices should match");
            assertEquals(expected.previousIndex(), filtered.previousIndex(), "Previous indices should match");
            assertEquals(expected.previous(), filtered.previous(), "Previous elements should match");
        }
        assertFalse(filtered.hasPrevious(), "Filtered iterator should be at the beginning");
    }

    /**
     * Tests that alternating between next() and previous() calls produces the
     * same results for the filtered and underlying iterators.
     */
    @Test
    public void shouldBehaveLikeNormalIteratorOnAlternatingWalk() {
        final ListIterator<Integer> expected = list.listIterator();
        final ListIterator<Integer> filtered = new FilterListIterator<>(list.listIterator(), truePredicate);

        while (expected.hasNext()) {
            assertEquals(expected.next(), filtered.next(), "First next() should match");
            if (expected.hasPrevious()) {
                assertEquals(expected.previous(), filtered.previous(), "previous() should match");
                assertEquals(expected.next(), filtered.next(), "Second next() should match");
            }
        }
    }

    /**
     * Tests a complex, deterministic walk pattern involving nested forward and
     * backward movements.
     */
    @Test
    public void shouldBehaveLikeNormalIteratorOnComplexWalk() {
        final ListIterator<Integer> expected = list.listIterator();
        final ListIterator<Integer> filtered = new FilterListIterator<>(list.listIterator(), truePredicate);

        for (int i = 0; i < list.size(); i++) {
            // Walk forward i steps
            for (int j = 0; j < i; j++) {
                assertEquals(expected.next(), filtered.next());
            }
            // Walk back i/2 steps
            for (int j = 0; j < i / 2; j++) {
                assertEquals(expected.previous(), filtered.previous());
            }
        }
    }

    /**
     * Tests that the filtered iterator behaves correctly during a long,
     * randomized sequence of next() and previous() calls.
     */
    @Test
    public void shouldBehaveLikeNormalIteratorOnRandomWalk() {
        final ListIterator<Integer> expected = list.listIterator();
        final ListIterator<Integer> filtered = new FilterListIterator<>(list.listIterator(), truePredicate);
        final StringBuilder walkDescription = new StringBuilder(RANDOM_WALK_STEPS);

        for (int i = 0; i < RANDOM_WALK_STEPS; i++) {
            if (random.nextBoolean()) {
                walkDescription.append("
                +");
                if (expected.hasNext()) {
                    assertTrue(filtered.hasNext(), "Filtered iterator should have next at step: " + walkDescription);
                    assertEquals(expected.next(), filtered.next(), "Random walk failed on next() at step: " + walkDescription);
                }
            } else {
                walkDescription.append("-");
                if (expected.hasPrevious()) {
                    assertTrue(filtered.hasPrevious(), "Filtered iterator should have previous at step: " + walkDescription);
                    assertEquals(expected.previous(), filtered.previous(), "Random walk failed on previous() at step: " + walkDescription);
                }
            }
            assertEquals(expected.nextIndex(), filtered.nextIndex(), "Indices diverged at step: " + walkDescription);
        }
    }
}