package org.apache.commons.collections4.iterators;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

/**
 * Unit tests for {@link CartesianProductIterator}.
 */
public class CartesianProductIteratorTest {

    /**
     * Tests that the iterator correctly cycles through all combinations of elements
     * from the input iterables in the expected order.
     */
    @Test
    public void nextShouldReturnAllTuplesInCorrectOrder() {
        // Arrange: Create two simple lists to form a Cartesian product.
        // The expected product is: [A, 1], [A, 2], [B, 1], [B, 2].
        final List<String> list1 = Arrays.asList("A", "B");
        final List<Integer> list2 = Arrays.asList(1, 2);

        final CartesianProductIterator<Object> productIterator =
                new CartesianProductIterator<>(list1, list2);

        // Act & Assert: Verify each tuple is returned in the correct order and
        // that hasNext() behaves as expected throughout the iteration.

        // First element: [A, 1]
        assertTrue("Iterator should have a first element", productIterator.hasNext());
        assertEquals(Arrays.asList("A", 1), productIterator.next());

        // Second element: [A, 2]
        assertTrue("Iterator should have a second element", productIterator.hasNext());
        assertEquals(Arrays.asList("A", 2), productIterator.next());

        // Third element: [B, 1]
        assertTrue("Iterator should have a third element", productIterator.hasNext());
        assertEquals(Arrays.asList("B", 1), productIterator.next());

        // Fourth element: [B, 2]
        assertTrue("Iterator should have a fourth element", productIterator.hasNext());
        assertEquals(Arrays.asList("B", 2), productIterator.next());

        // End of iteration
        assertFalse("Iterator should have no more elements", productIterator.hasNext());
    }
}