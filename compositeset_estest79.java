package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link CompositeSet#containsAll(Collection)} method.
 * This class provides a more understandable version of an auto-generated test case.
 */
public class CompositeSet_ESTestTest79 {

    /**
     * Tests that {@link CompositeSet#containsAll(Collection)} returns false
     * when the composite set is empty and the collection to check is not.
     */
    @Test
    public void testContainsAllShouldReturnFalseForNonEmptyCollectionOnEmptySet() {
        // Arrange: Create an empty CompositeSet and a non-empty collection.
        final CompositeSet<String> emptyCompositeSet = new CompositeSet<>();
        final Collection<String> nonEmptyCollection = Collections.singleton("an element");

        // Act: Check if the empty set contains all elements of the non-empty collection.
        final boolean result = emptyCompositeSet.containsAll(nonEmptyCollection);

        // Assert: The result must be false, as an empty set cannot contain any elements.
        assertFalse("An empty CompositeSet should not contain elements from a non-empty collection.", result);
    }
}