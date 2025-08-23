package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link CompositeSet} class.
 */
public class CompositeSetTest {

    /**
     * Tests that calling removeAll() with an empty collection on an empty CompositeSet
     * does not modify the set and correctly returns false.
     */
    @Test
    public void removeAllFromEmptySetWithEmptyCollectionShouldReturnFalse() {
        // Arrange: Create an empty CompositeSet and an empty collection to remove.
        final CompositeSet<String> emptySet = new CompositeSet<>();
        final Set<String> emptyCollectionToRemove = Collections.emptySet();

        // Act: Attempt to remove the elements of the empty collection from the empty set.
        final boolean wasModified = emptySet.removeAll(emptyCollectionToRemove);

        // Assert: Verify that the set was not modified and remains empty.
        assertFalse("removeAll should return false as the set was not modified", wasModified);
        assertTrue("The composite set should remain empty after the operation", emptySet.isEmpty());
    }
}