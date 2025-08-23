package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;

/**
 * Contains tests for the {@link ZippingIterator} class.
 */
public class ZippingIteratorTest {

    /**
     * Verifies that calling remove() before a call to next() throws an
     * IllegalStateException, as specified by the Iterator contract.
     */
    @Test(expected = IllegalStateException.class)
    public void removeBeforeNextShouldThrowIllegalStateException() {
        // Arrange: Create a ZippingIterator. The content of the underlying
        // iterators is irrelevant for this test case; what matters is that
        // next() has not yet been called on the ZippingIterator.
        final Iterator<Object> emptyIterator = Collections.emptyIterator();
        final ZippingIterator<Object> zippingIterator = new ZippingIterator<>(emptyIterator);

        // Act & Assert: Calling remove() immediately should throw.
        zippingIterator.remove();
    }
}