package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

import java.util.HashSet;

/**
 * This test class focuses on the behavior of the CompositeSet.
 * The original test was auto-generated and has been refactored for clarity and maintainability.
 */
public class CompositeSetTest {

    /**
     * Tests that calling contains() on an empty CompositeSet always returns false,
     * regardless of the object being checked.
     */
    @Test
    public void testContainsOnEmptySetReturnsFalse() {
        // Arrange: Create an empty CompositeSet.
        final CompositeSet<Integer> emptyCompositeSet = new CompositeSet<>();
        
        // An arbitrary object to search for. The original test used another Set,
        // which is a valid object to check for containment.
        final Object objectToFind = new HashSet<Integer>();

        // Act: Call the contains() method on the empty set.
        final boolean isFound = emptyCompositeSet.contains(objectToFind);

        // Assert: The method should return false because an empty set contains nothing.
        assertFalse("contains() on an empty CompositeSet should always return false.", isFound);
    }
}