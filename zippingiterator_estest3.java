package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertSame;

import java.util.Collections;
import java.util.Iterator;

// The original test class name and scaffolding are kept to match the provided context.
// In a real-world scenario, this would likely be renamed to 'ZippingIteratorTest'.
public class ZippingIterator_ESTestTest3 extends ZippingIterator_ESTest_scaffolding {

    /**
     * Tests that the first call to next() on a ZippingIterator returns the
     * element from the first underlying iterator.
     * <p>
     * This test specifically covers the edge case where the same iterator instance
     * is used for both inputs, meaning they share state.
     * </p>
     */
    @Test
    public void testNext_withSharedIteratorInstance_returnsFirstElement() {
        // Arrange: Create a simple iterator with a single element.
        final String element = "A";
        final Iterator<String> sourceIterator = Collections.singletonList(element).iterator();

        // Act: Create a ZippingIterator with the same iterator instance provided twice.
        final ZippingIterator<String> zippingIterator = new ZippingIterator<>(sourceIterator, sourceIterator);
        final String result = zippingIterator.next();

        // Assert: The first element returned should be the one from the underlying iterator.
        assertSame("The first call to next() should return the single element from the source iterator.",
                   element, result);
    }
}