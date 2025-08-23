package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Test cases for ZippingIterator, focusing on the behavior of the hasNext() method.
 */
public class ZippingIteratorTest {

    /**
     * Tests that hasNext() is idempotent and returns true when the underlying iterator has elements.
     * <p>
     * This test covers a scenario where a ZippingIterator is constructed with the same
     * iterator instance multiple times. It verifies that repeatedly calling hasNext()
     * correctly reports the presence of elements without advancing the iterator.
     * </p>
     */
    @Test
    public void hasNextShouldBeIdempotentAndReturnTrueWhenElementsExist() {
        // Arrange: Create a list with a single element and get its iterator.
        final List<String> list = new ArrayList<>();
        list.add("A");
        final Iterator<String> singleIterator = list.iterator();

        // Act: Create a ZippingIterator using the same iterator instance twice.
        final ZippingIterator<String> zippingIterator = new ZippingIterator<>(singleIterator, singleIterator);

        // Assert: Verify that hasNext() returns true and is idempotent.
        // The first call should return true because the underlying iterator has an element.
        assertTrue("First call to hasNext() should return true.", zippingIterator.hasNext());

        // The second call should also return true, as hasNext() should not alter the iterator's state.
        assertTrue("Second call to hasNext() should also return true, demonstrating idempotency.", zippingIterator.hasNext());
    }
}