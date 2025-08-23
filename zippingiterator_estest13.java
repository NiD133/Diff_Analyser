package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Unit tests for {@link ZippingIterator}.
 */
public class ZippingIteratorTest {

    /**
     * Tests that calling next() on a ZippingIterator created with only empty
     * iterators throws a NoSuchElementException.
     */
    @Test(expected = NoSuchElementException.class)
    public void testNextShouldThrowExceptionWhenAllIteratorsAreEmpty() {
        // Arrange: Create a ZippingIterator from three empty iterators.
        // Using Collections.emptyIterator() is a standard and clear way to get an empty iterator.
        final ZippingIterator<Object> zippingIterator = new ZippingIterator<>(
                Collections.emptyIterator(),
                Collections.emptyIterator(),
                Collections.emptyIterator());

        // Act & Assert: Calling next() should throw the expected exception.
        // The assertion is handled by the @Test(expected=...) annotation.
        zippingIterator.next();
    }
}