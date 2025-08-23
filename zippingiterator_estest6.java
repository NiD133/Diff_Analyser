package org.apache.commons.collections4.iterators;

import org.junit.Test;
import java.util.Iterator;
import java.util.Collections;

/**
 * Unit tests for {@link ZippingIterator}.
 */
public class ZippingIteratorTest {

    /**
     * Tests that the constructor throws a NullPointerException if the array of
     * iterators passed to it contains a null element.
     */
    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNullPointerExceptionWhenGivenNullIterator() {
        // Arrange: Create an array of iterators that includes a null.
        // The constructor's contract states that all provided iterators must be non-null.
        final Iterator<?>[] iteratorsWithNull = {
            Collections.emptyIterator(),
            null
        };

        // Act & Assert: Instantiating ZippingIterator with this array should
        // throw a NullPointerException, which is verified by the @Test annotation.
        new ZippingIterator<>(iteratorsWithNull);
    }
}