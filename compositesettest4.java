package org.apache.commons.collections4.set;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Extension of {@link AbstractSetTest} for the {@link CompositeSet} class.
 * This class focuses on specific behaviors not covered by the standard test suite.
 */
public class CompositeSetTest4<E> extends AbstractSetTest<E> {

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
        // The mutator is needed for the abstract tests that modify the set.
        set.setMutator(new EmptySetMutator<>(contained));
        return set;
    }

    // -----------------------------------------------------------------------

    @Test
    @DisplayName("containsAll(null) should return false")
    @SuppressWarnings("unchecked")
    void containsAll_withNullCollection_shouldReturnFalse() {
        // This test verifies a specific behavior of CompositeSet.containsAll().
        // The java.util.Collection interface contract states that containsAll(null)
        // should throw a NullPointerException. This test asserts that the
        // implementation deviates from the contract by returning false.

        // Arrange
        final CompositeSet<E> compositeSet = new CompositeSet<>(buildOne(), buildTwo());
        final Collection<E> nullCollection = null;

        // Act
        final boolean result = compositeSet.containsAll(nullCollection);

        // Assert
        assertFalse(result, "containsAll(null) was expected to return false.");
    }
}