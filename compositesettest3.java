package org.apache.commons.collections4.set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

/**
 * Contains specific tests for CompositeSet behavior that are not covered by the
 * AbstractSetTest framework.
 *
 * @param <E> the type of elements in the tested set
 */
// Renamed class for clarity and to remove redundancy.
public class CompositeSetTest<E> extends AbstractSetTest<E> {

    // The following methods are part of the AbstractSetTest framework.
    // They are used to run a standard suite of Set contract tests.

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

    /**
     * Creates a CompositeSet for the tests inherited from AbstractSetTest.
     */
    @Override
    public CompositeSet<E> makeObject() {
        final HashSet<E> contained = new HashSet<>();
        final CompositeSet<E> set = new CompositeSet<>(contained);
        // A mutator is required for the add/remove tests in AbstractSetTest.
        set.setMutator(new EmptySetMutator<>(contained));
        return set;
    }

    // The original test case has been expanded into more specific,
    // descriptive tests for better clarity and maintainability.

    @Test
    @SuppressWarnings("unchecked")
    void contains_shouldReturnTrue_whenElementExistsInAnyCompositedSet() {
        // Arrange
        final Set<E> set1 = buildOne(); // Contains "1", "2"
        final Set<E> set2 = buildTwo(); // Contains "3", "4"
        final CompositeSet<E> compositeSet = new CompositeSet<>(set1, set2);

        // Act & Assert
        assertTrue(compositeSet.contains("1"), "Should find an element from the first set.");
        assertTrue(compositeSet.contains("4"), "Should find an element from the second set.");
    }

    @Test
    @SuppressWarnings("unchecked")
    void contains_shouldReturnFalse_whenElementDoesNotExist() {
        // Arrange
        final Set<E> set1 = buildOne();
        final Set<E> set2 = buildTwo();
        final CompositeSet<E> compositeSet = new CompositeSet<>(set1, set2);

        // Act & Assert
        assertFalse(compositeSet.contains("5"), "Should not find an element that is not in any set.");
    }

    @Test
    void contains_shouldReturnFalse_whenSetIsEmpty() {
        // Arrange
        final CompositeSet<E> emptyCompositeSet = new CompositeSet<>();

        // Act & Assert
        assertFalse(emptyCompositeSet.contains("1"), "Should not find any element in an empty set.");
    }
}