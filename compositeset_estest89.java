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
     * Tests that calling retainAll() with the set itself as an argument
     * results in no modification and correctly returns false.
     *
     * According to the Collection API, retainAll should return true only if
     * the collection was modified as a result of the call. Retaining all
     * elements from itself should never modify the collection.
     */
    @Test
    public void retainAllWithSelfShouldNotModifySetAndReturnFalse() {
        // Arrange: Create an empty CompositeSet.
        // The behavior should be the same whether the set is empty or not,
        // but an empty set is a simple and effective case to test.
        final Set<Integer> componentSet = new LinkedHashSet<>();
        final CompositeSet<Integer> compositeSet = new CompositeSet<>(componentSet);

        // Act: Call retainAll with the set itself.
        final boolean wasModified = compositeSet.retainAll(compositeSet);

        // Assert: Verify that the set was not modified.
        assertFalse("retainAll(this) should always return false as it cannot modify the collection.", wasModified);
    }
}