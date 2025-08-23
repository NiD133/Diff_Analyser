package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Contains tests for the {@link CompositeSet#iterator()} method.
 */
public class CompositeSetIteratorTest {

    /**
     * Tests that the iterator of a CompositeSet is empty when it is composed of other empty sets.
     * This verifies that the iterator correctly handles compositions of empty sets, including nested CompositeSets.
     */
    @Test
    public void testIteratorForCompositeSetOfEmptySetsIsEmpty() {
        // Arrange: Create a composite set containing different types of empty sets.
        // This setup tests the iterator's behavior with nested, empty structures.
        Set<Integer> emptyHashSet = new HashSet<>();
        CompositeSet<Integer> emptyNestedCompositeSet = new CompositeSet<>();

        // The CompositeSet under test is composed of the two empty sets created above.
        CompositeSet<Integer> compositeSet = new CompositeSet<>(emptyHashSet, emptyNestedCompositeSet);

        // Assert pre-conditions: The composite set should be empty as its components are empty.
        assertTrue("A composite set of empty sets should be empty", compositeSet.isEmpty());
        assertEquals("A composite set of empty sets should have a size of 0", 0, compositeSet.size());

        // Act: Get the iterator from the composite set.
        Iterator<Integer> iterator = compositeSet.iterator();

        // Assert: The resulting iterator should be empty.
        assertNotNull("The iterator should not be null", iterator);
        assertFalse("The iterator for an empty composite set should have no elements", iterator.hasNext());
    }
}