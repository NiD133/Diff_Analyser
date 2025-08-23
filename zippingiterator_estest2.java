package org.apache.commons.collections4.iterators;

import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link ZippingIterator}.
 */
public class ZippingIteratorTest {

    /**
     * Tests that the ZippingIterator correctly returns a null element
     * when one of its underlying iterators provides it.
     */
    @Test
    public void nextShouldReturnNullWhenUnderlyingIteratorProvidesNull() {
        // Arrange: Create two iterators to be zipped. The first contains a null element,
        // and the second contains a non-null element.
        final Iterator<String> iteratorA = Arrays.asList((String) null).iterator();
        final Iterator<String> iteratorB = Arrays.asList("B").iterator();

        final ZippingIterator<String> zippingIterator = new ZippingIterator<>(iteratorA, iteratorB);

        // Assert that the iterator has elements before we start.
        assertTrue("Iterator should have elements before iteration", zippingIterator.hasNext());

        // Act & Assert: First call to next() should return the element from iteratorA.
        final String firstElement = zippingIterator.next();
        assertNull("The first element from iteratorA should be null", firstElement);

        // The iterator should still have the element from iteratorB.
        assertTrue("Iterator should still have an element from iteratorB", zippingIterator.hasNext());

        // Act & Assert: Second call to next() should return the element from iteratorB.
        final String secondElement = zippingIterator.next();
        assertEquals("The second element from iteratorB should be 'B'", "B", secondElement);

        // The iterator should now be exhausted.
        assertFalse("Iterator should be exhausted after iterating all elements", zippingIterator.hasNext());
    }
}