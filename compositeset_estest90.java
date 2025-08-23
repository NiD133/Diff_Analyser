package org.apache.commons.collections4.set;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;

/**
 * This test class contains improved versions of tests for CompositeSet.
 * The original test class name is retained to show the context of the improvement.
 */
public class CompositeSet_ESTestTest90 extends CompositeSet_ESTest_scaffolding {

    /**
     * Tests that a newly created, empty CompositeSet behaves as expected.
     * It verifies that isEmpty() returns true and that containsAll() with an
     * empty collection also returns true.
     */
    @Test
    public void testEmptyCompositeSetBehavesCorrectly() {
        // Arrange: Create an empty CompositeSet and an empty collection to test against.
        final CompositeSet<Integer> emptySet = new CompositeSet<>();
        final Collection<Integer> emptyCollection = Collections.emptySet();

        // Assert: Verify the properties of the empty CompositeSet.
        assertTrue("A newly initialized CompositeSet should be empty.", emptySet.isEmpty());

        assertTrue("An empty set should 'contain' all elements of an empty collection.",
                   emptySet.containsAll(emptyCollection));
    }
}