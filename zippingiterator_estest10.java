package org.apache.commons.collections4.iterators;

import static org.junit.Assert.assertFalse;

import java.util.Collections;
import java.util.Iterator;
import org.junit.Test;

/**
 * Contains tests for the {@link ZippingIterator}.
 */
public class ZippingIteratorTest {

    /**
     * Tests that hasNext() returns false when the ZippingIterator is
     * constructed with only empty iterators.
     */
    @Test
    public void hasNextShouldReturnFalseWhenAllIteratorsAreEmpty() {
        // Arrange: Create three empty iterators.
        final Iterator<Integer> emptyIterator1 = Collections.emptyIterator();
        final Iterator<Integer> emptyIterator2 = Collections.emptyIterator();
        final Iterator<Integer> emptyIterator3 = Collections.emptyIterator();

        // Act: Create a ZippingIterator with the empty iterators.
        final ZippingIterator<Integer> zippingIterator = new ZippingIterator<>(
            emptyIterator1, emptyIterator2, emptyIterator3);

        // Assert: The ZippingIterator should report that it has no elements.
        assertFalse("A ZippingIterator with only empty iterators should not have a next element",
                    zippingIterator.hasNext());
    }
}