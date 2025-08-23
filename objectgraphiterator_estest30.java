package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.Collections;
import java.util.Iterator;

/**
 * Unit tests for {@link ObjectGraphIterator}.
 */
public class ObjectGraphIteratorTest {

    /**
     * Tests that calling remove() before a call to next() throws an IllegalStateException.
     * This behavior is required by the java.util.Iterator contract.
     */
    @Test(expected = IllegalStateException.class)
    public void removeShouldThrowIllegalStateExceptionWhenCalledBeforeNext() {
        // Arrange: Create an ObjectGraphIterator. An empty underlying iterator is
        // sufficient, as we are testing the state before any iteration begins.
        final Iterator<Object> emptyRootIterator = Collections.emptyIterator();
        final ObjectGraphIterator<Object> graphIterator = new ObjectGraphIterator<>(emptyRootIterator);

        // Act & Assert: Calling remove() immediately should throw an IllegalStateException.
        // The @Test(expected) annotation handles the assertion.
        graphIterator.remove();
    }
}