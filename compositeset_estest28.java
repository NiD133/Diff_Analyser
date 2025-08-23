package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * This class contains improved, more understandable tests for the CompositeSet class,
 * refactored from a single, complex, auto-generated test method.
 */
public class CompositeSetImprovedTest {

    /**
     * Tests that creating a CompositeSet from another CompositeSet results in a
     * new composite containing the original one as its single underlying set.
     */
    @Test
    public void constructorWithCompositeSetCreatesAViewWithOneUnderlyingSet() {
        // Arrange
        Set<String> innerSet = new HashSet<>();
        CompositeSet<String> originalComposite = new CompositeSet<>(innerSet);

        // Act
        CompositeSet<String> newComposite = new CompositeSet<>(originalComposite);
        List<Set<String>> underlyingSets = newComposite.getSets();

        // Assert
        assertEquals("The new composite set should have one underlying set.", 1, underlyingSets.size());
        assertSame("The underlying set should be the original composite set.", originalComposite, underlyingSets.get(0));
    }

    /**
     * Tests that calling removeIf on an empty CompositeSet does nothing and returns false.
     */
    @Test
    public void removeIfOnEmptySetReturnsFalse() {
        // Arrange
        CompositeSet<String> emptySet = new CompositeSet<>();
        Predicate<String> alwaysTruePredicate = s -> true;

        // Act
        boolean result = emptySet.removeIf(alwaysTruePredicate);

        // Assert
        assertFalse("removeIf should return false for an empty set.", result);
        assertTrue("The set should remain empty.", emptySet.isEmpty());
    }

    /**
     * Tests that removeIf correctly removes elements that match the predicate
     * from the underlying sets and returns true.
     */
    @Test
    public void removeIfRemovesMatchingElementsAndReturnsTrue() {
        // Arrange
        Set<Integer> set1 = new HashSet<>();
        set1.add(1);
        set1.add(2); // even

        Set<Integer> set2 = new HashSet<>();
        set2.add(3);
        set2.add(4); // even

        CompositeSet<Integer> compositeSet = new CompositeSet<>(set1, set2);
        Predicate<Integer> isEven = n -> n % 2 == 0;

        // Act
        boolean result = compositeSet.removeIf(isEven);

        // Assert
        assertTrue("removeIf should return true as elements were removed.", result);
        assertEquals("CompositeSet size should be reduced to 2.", 2, compositeSet.size());
        assertFalse("Element 2 should have been removed.", compositeSet.contains(2));
        assertFalse("Element 4 should have been removed.", compositeSet.contains(4));
        assertTrue("Element 1 should remain.", compositeSet.contains(1));
        assertTrue("Element 3 should remain.", compositeSet.contains(3));

        // Verify underlying collections were also modified
        assertFalse("Underlying set1 should no longer contain 2.", set1.contains(2));
        assertFalse("Underlying set2 should no longer contain 4.", set2.contains(4));
    }

    /**
     * Tests that getMutator returns null by default when no mutator has been configured.
     */
    @Test
    public void getMutatorReturnsNullByDefault() {
        // Arrange
        CompositeSet<Object> compositeSet = new CompositeSet<>();

        // Act
        CompositeSet.SetMutator<Object> mutator = compositeSet.getMutator();

        // Assert
        assertNull("getMutator should return null when no mutator is set.", mutator);
    }

    /**
     * Tests that calling addComposited with a null array of sets does not throw an
     * exception or modify the composite set.
     */
    @Test
    public void addCompositedWithNullArrayDoesNotAlterSet() {
        // Arrange
        CompositeSet<String> compositeSet = new CompositeSet<>();
        compositeSet.addComposited(new HashSet<>()); // Add one set initially

        // Act: The method signature is addComposited(final Set<E>... sets),
        // so a null array is a valid, though unusual, input.
        compositeSet.addComposited((Set<String>[]) null);

        // Assert
        assertEquals("addComposited with a null array should not change the number of underlying sets.", 1, compositeSet.getSets().size());
    }
}