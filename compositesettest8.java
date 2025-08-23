package org.apache.commons.collections4.set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Tests for CompositeSet.
 * <p>
 * This class focuses on specific behaviors of CompositeSet not covered by the
 * abstract test suite.
 * </p>
 */
public class CompositeSetTest<E> extends AbstractSetTest<E> {

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
        // The EmptySetMutator is used by tests in the AbstractSetTest superclass.
        set.setMutator(new EmptySetMutator<>(contained));
        return set;
    }

    /**
     * Tests that removing an element directly from an underlying set is reflected
     * in the composite set's view.
     */
    @Test
    @SuppressWarnings("unchecked")
    void shouldReflectRemovalsFromUnderlyingSets() {
        // Arrange: Create two sets and a composite set that views them both.
        final Set<E> set1 = buildOne(); // Contains "1", "2"
        final Set<E> set2 = buildTwo(); // Contains "3", "4"
        final CompositeSet<E> compositeSet = new CompositeSet<>(set1, set2);

        // Sanity check to ensure elements are present initially.
        assertTrue(compositeSet.contains("1"), "Composite set should contain '1' before removal");
        assertTrue(compositeSet.contains("3"), "Composite set should contain '3' before removal");

        // Act: Remove an element directly from the first underlying set.
        set1.remove("1");

        // Assert: The composite set should no longer contain the removed element.
        assertFalse(compositeSet.contains("1"), "Composite set should not contain '1' after it was removed from set1");

        // Act: Remove an element directly from the second underlying set.
        set2.remove("3");

        // Assert: The composite set should also reflect this change.
        assertFalse(compositeSet.contains("3"), "Composite set should not contain '3' after it was removed from set2");
    }
}