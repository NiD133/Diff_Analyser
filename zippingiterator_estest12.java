package org.apache.commons.collections4.iterators;

import org.junit.Test;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link ZippingIterator}.
 */
public class ZippingIteratorTest {

    /**
     * Tests that the remove() method correctly removes the element from the
     * underlying collection's iterator.
     *
     * This test creates a ZippingIterator with the same underlying iterator
     * provided three times. It verifies that after calling next() and then remove(),
     * the single element in the source list is successfully removed.
     */
    @Test
    public void removeShouldDelegateToUnderlyingIterator() {
        // Arrange: Create a list with a single element and an iterator for it.
        List<Integer> sourceList = new LinkedList<>();
        sourceList.add(100);
        Iterator<Integer> sourceIterator = sourceList.iterator();

        // The ZippingIterator is created with the same iterator instance multiple times.
        ZippingIterator<Integer> zippingIterator = new ZippingIterator<>(sourceIterator, sourceIterator, sourceIterator);

        // Pre-assertion: The list should not be empty, and the iterator should have a next element.
        assertFalse("Source list should not be empty before the test", sourceList.isEmpty());
        assertTrue("ZippingIterator should have an element to start with", zippingIterator.hasNext());

        // Act: Advance the iterator and remove the element.
        Integer value = zippingIterator.next();
        zippingIterator.remove();

        // Assert: Verify the element was removed from the source list.
        assertEquals("The value returned by next() should be the one from the list", Integer.valueOf(100), value);
        assertTrue("The source list should be empty after remove() is called", sourceList.isEmpty());
        assertFalse("ZippingIterator should have no more elements", zippingIterator.hasNext());
    }
}