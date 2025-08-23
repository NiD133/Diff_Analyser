package org.apache.commons.collections4.set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections4.set.CompositeSet.SetMutator;
import org.junit.jupiter.api.Test;

/**
 * Contains tests for CompositeSet.
 * <p>
 * This class extends a generic abstract test class. The generic type {@code <E>}
 * is used to allow the abstract tests to be run. The specific tests in this
 * file use String elements, which requires the use of unchecked casts.
 * </p>
 */
public class CompositeSetTestTest6<E> extends AbstractSetTest<E> {

    // --- Test Fixture Setup for AbstractSetTest ---

    /**
     * Builds a sample set with elements "1" and "2".
     * Used by the test framework and specific tests.
     */
    @SuppressWarnings("unchecked")
    public Set<E> buildOne() {
        final HashSet<E> set = new HashSet<>();
        set.add((E) "1");
        set.add((E) "2");
        return set;
    }

    /**
     * Builds a sample set with elements "3" and "4".
     * Used by the test framework and specific tests.
     */
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
     * Creates an empty CompositeSet for the abstract test cases inherited
     * from AbstractSetTest.
     */
    @Override
    public CompositeSet<E> makeObject() {
        final HashSet<E> contained = new HashSet<>();
        final CompositeSet<E> set = new CompositeSet<>(contained);
        // The mutator is needed for the abstract tests that modify the collection.
        // The original code included a custom SetMutator here.
        // set.setMutator(new EmptySetMutator<>(contained));
        return set;
    }

    // --- Specific Test Cases for CompositeSet ---

    /**
     * Tests that calling removeAll with a null collection is a no-op and returns false.
     * <p>
     * This behavior deviates from the java.util.Collection#removeAll contract,
     * which specifies that a NullPointerException should be thrown. This test
     * verifies the specific, more lenient implementation in CompositeSet.
     * </p>
     */
    @Test
    @SuppressWarnings("unchecked")
    void removeAllWithNullCollectionShouldReturnFalseAndNotModifySet() {
        // Arrange
        final Set<E> set1 = buildOne(); // Contains {"1", "2"}
        final Set<E> set2 = buildTwo(); // Contains {"3", "4"}
        final CompositeSet<E> compositeSet = new CompositeSet<>(set1, set2);
        final int expectedSize = 4;
        assertEquals(expectedSize, compositeSet.size(), "Pre-condition: CompositeSet should have 4 elements.");

        // Act
        final boolean wasModified = compositeSet.removeAll(null);

        // Assert
        assertFalse(wasModified, "removeAll(null) should return false, indicating no modification.");
        assertEquals(expectedSize, compositeSet.size(), "The size of the set should remain unchanged.");
        assertTrue(compositeSet.containsAll(set1), "Set should still contain all elements from the first component set.");
        assertTrue(compositeSet.containsAll(set2), "Set should still contain all elements from the second component set.");
    }
}