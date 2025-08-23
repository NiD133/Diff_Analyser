package org.apache.commons.collections4.set;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * Tests for edge cases in the {@link CompositeSet} class.
 */
public class CompositeSetTest {

    /**
     * Tests that the CompositeSet constructor throws a StackOverflowError when
     * given a set that contains itself.
     * <p>
     * When a set is added to a CompositeSet, the implementation may perform
     * operations (like checking for collisions) that iterate over the set's
     * elements. If the set contains a reference to itself, this can lead to
     * infinite recursion and a StackOverflowError.
     */
    @Test(expected = StackOverflowError.class)
    public void constructorShouldThrowStackOverflowErrorWhenGivenRecursiveSet() {
        // Arrange: Create a set that contains itself, forming a recursive data structure.
        final Set<Object> recursiveSet = new HashSet<>();
        recursiveSet.add(recursiveSet);

        // Act & Assert: Attempting to create a CompositeSet with this recursive set
        // is expected to cause a StackOverflowError.
        new CompositeSet<>(recursiveSet);
    }
}