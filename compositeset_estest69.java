package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.function.Predicate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link CompositeSet}.
 */
public class CompositeSetTest {

    /**
     * Tests that calling removeIf on an empty CompositeSet returns false
     * and does not modify the set.
     */
    @Test
    public void removeIfOnEmptySetShouldReturnFalse() {
        // Arrange
        CompositeSet<Object> emptySet = new CompositeSet<>();
        // A predicate that would remove any element it encounters.
        Predicate<Object> alwaysTruePredicate = element -> true;

        // Act
        boolean wasModified = emptySet.removeIf(alwaysTruePredicate);

        // Assert
        assertFalse("removeIf on an empty set should return false as the collection was not modified", wasModified);
        assertTrue("The set should remain empty after the operation", emptySet.isEmpty());
    }
}