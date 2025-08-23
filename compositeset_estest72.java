package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the {@link CompositeSet} class.
 * This is a refactored version of a single, auto-generated test case.
 */
public class CompositeSetTest {

    /**
     * Tests that calling {@code retainAll(null)} on an empty CompositeSet
     * returns false, indicating that the set was not modified.
     *
     * <p>Note: The general contract of {@link java.util.Collection#retainAll(java.util.Collection)}
     * is to throw a NullPointerException if the provided collection is null.
     * This test verifies that CompositeSet deviates from that contract by returning
     * false instead of throwing an exception.</p>
     */
    @Test
    public void retainAllWithNullCollectionOnEmptySetShouldReturnFalse() {
        // Arrange: Create an empty CompositeSet.
        final CompositeSet<Object> compositeSet = new CompositeSet<>();

        // Act: Call retainAll with a null collection.
        final boolean wasModified = compositeSet.retainAll(null);

        // Assert: The set should not have been modified, so the method should return false.
        assertFalse("retainAll(null) on an empty set should return false.", wasModified);
    }
}