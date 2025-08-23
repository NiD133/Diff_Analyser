package org.apache.commons.collections4.bag;

import org.junit.Test;

/**
 * This test class contains tests for the TreeBag class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class TreeBag_ESTestTest11 extends TreeBag_ESTest_scaffolding {

    /**
     * Verifies that the TreeBag constructor throws a NullPointerException
     * when initialized with a null Iterable. This is the expected behavior
     * as the constructor cannot iterate over a null collection.
     */
    @Test(expected = NullPointerException.class)
    public void constructorWithNullIterableShouldThrowNullPointerException() {
        // When: Attempting to create a TreeBag with a null iterable argument.
        // Then: A NullPointerException is expected, as declared by the @Test annotation.
        new TreeBag<>((Iterable<?>) null);
    }
}