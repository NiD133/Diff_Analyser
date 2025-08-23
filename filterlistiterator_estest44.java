package org.apache.commons.collections4.iterators;

import org.junit.Test;

/**
 * This test class verifies the behavior of the FilterListIterator.
 * The original test was automatically generated and has been refactored for clarity.
 */
public class FilterListIterator_ESTestTest44 extends FilterListIterator_ESTest_scaffolding {

    /**
     * Tests that the add() method is unsupported and correctly throws an
     * UnsupportedOperationException. The FilterListIterator is a read-only
     * decorator and is not designed to allow adding elements.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void addShouldThrowUnsupportedOperationException() {
        // Given: A FilterListIterator instance.
        // The internal state (e.g., the wrapped iterator or predicate) is not
        // relevant for testing this unsupported operation, so we use the
        // default constructor.
        final FilterListIterator<Object> filterListIterator = new FilterListIterator<>();

        // When: The add method is called
        // Then: An UnsupportedOperationException is thrown, as declared by the @Test annotation.
        filterListIterator.add("any object");
    }
}