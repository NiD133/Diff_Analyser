package org.apache.commons.collections4.set;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Contains specific tests for CompositeSet behavior, supplementing the
 * general contract tests in AbstractSetTest.
 *
 * @param <E> the type of the elements in the set
 */
public class CompositeSetTestTest2<E> extends AbstractSetTest<E> {

    @SuppressWarnings("unchecked")
    public Set<E> buildOne() {
        final HashSet<E> set = new HashSet<>();
        set.add((E) "1");
        set.add((E) "2");
        return set;
    }

    @SuppressWarnings("unchecked")
    public Set<E> buildTwo() {
        final HashSet<E> set = new HashSet<>();
        set.add((E) "3");
        set.add((E) "4");
        return set;
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    protected int getIterationBehaviour() {
        return UNORDERED;
    }

    @Override
    public CompositeSet<E> makeObject() {
        final HashSet<E> contained = new HashSet<>();
        final CompositeSet<E> set = new CompositeSet<>(contained);
        set.setMutator(new EmptySetMutator<>(contained));
        return set;
    }

    /**
     * Tests that adding sets with overlapping elements to a CompositeSet without a
     * SetMutator throws an UnsupportedOperationException.
     */
    @Test
    @SuppressWarnings("unchecked")
    void addCompositedWithOverlappingSetsAndNoMutatorShouldThrowException() {
        // Arrange: Create a set to be part of the composite.
        final Set<E> initialSet = new HashSet<>();
        initialSet.add((E) "1");
        initialSet.add((E) "2");
        initialSet.add((E) "3");

        // Arrange: Create a CompositeSet containing the initial set.
        // By default, it has no SetMutator, so it cannot resolve collisions.
        final CompositeSet<E> compositeSet = new CompositeSet<>(initialSet);

        // Arrange: Create sets that will cause collisions.
        // collidingSetA overlaps with initialSet on elements "1" and "2".
        final Set<E> collidingSetA = buildOne(); // Contains {"1", "2"}
        // collidingSetB overlaps with initialSet on element "3".
        final Set<E> collidingSetB = buildTwo(); // Contains {"3", "4"}

        // Act & Assert: Attempting to add sets that are already present or have
        // overlapping elements should fail because no collision resolution
        // strategy (SetMutator) has been defined.

        // Scenario 1: Adding the initial set again, plus another colliding set.
        assertThrows(UnsupportedOperationException.class,
            () -> compositeSet.addComposited(initialSet, collidingSetA));

        // Scenario 2: Adding the initial set again, plus two other colliding sets.
        assertThrows(UnsupportedOperationException.class,
            () -> compositeSet.addComposited(initialSet, collidingSetA, collidingSetB));
    }
}