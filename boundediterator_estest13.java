package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;

/**
 * Tests for illegal constructor arguments in {@link BoundedIterator}.
 *
 * Note: This test class retains the original EvoSuite structure to ensure
 * compatibility with its test execution environment, but the test case
 * itself has been rewritten for clarity.
 */
// The original class name and runner are kept for compatibility.
public class BoundedIterator_ESTestTest13 extends BoundedIterator_ESTest_scaffolding {

    /**
     * Verifies that the BoundedIterator constructor throws an IllegalArgumentException
     * when the 'offset' parameter is negative.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowExceptionForNegativeOffset() {
        // Arrange: Use a valid iterator and a valid 'max' value.
        final Iterator<Object> emptyIterator = Collections.emptyIterator();
        final long negativeOffset = -1L;
        final long validMax = 10L;

        // Act & Assert:
        // Attempting to create a BoundedIterator with a negative offset
        // should throw an IllegalArgumentException.
        new BoundedIterator<>(emptyIterator, negativeOffset, validMax);
    }
}