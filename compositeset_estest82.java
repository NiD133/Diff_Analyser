package org.apache.commons.collections4.set;

import org.junit.Test;
import java.util.LinkedHashSet;
import java.util.Set;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link CompositeSet} class.
 */
public class CompositeSetTest {

    /**
     * Tests that calling retainAll() on an empty CompositeSet with itself as an argument
     * correctly reports that the set was not modified.
     */
    @Test
    public void retainAllWithSelfOnEmptySetShouldReturnFalse() {
        // Arrange
        // Create a composite set containing a single, empty component set.
        final Set<Integer> componentSet = new LinkedHashSet<>();
        final CompositeSet<Integer> compositeSet = new CompositeSet<>(componentSet);

        // Act
        // Attempt to retain all elements from itself. Since the set is empty,
        // no modification should occur.
        final boolean wasModified = compositeSet.retainAll(compositeSet);

        // Assert
        // The method should return false, indicating the set was not modified.
        assertFalse("retainAll on an empty set should return false", wasModified);
    }
}