package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

import java.util.HashSet;
import java.util.Set;

/**
 * Contains tests for the CompositeSet class.
 * Note: The original test was part of a generated suite (CompositeSet_ESTestTest68).
 * This version is cleaned up for clarity.
 */
public class CompositeSet_ESTestTest68 {

    /**
     * Tests that calling removeIf() on an empty CompositeSet returns false,
     * as no elements can be removed.
     */
    @Test
    public void removeIfShouldReturnFalseWhenSetIsEmpty() {
        // Arrange: Create an empty composite set.
        final Set<Integer> componentSet = new HashSet<>();
        final CompositeSet<Integer> compositeSet = new CompositeSet<>(componentSet);

        // Act: Attempt to remove elements. The predicate is designed to match any element,
        // but since the set is empty, no removal should occur.
        final boolean wasModified = compositeSet.removeIf(element -> true);

        // Assert: The method should return false, indicating the set was not modified.
        assertFalse("removeIf on an empty set should return false.", wasModified);
    }
}