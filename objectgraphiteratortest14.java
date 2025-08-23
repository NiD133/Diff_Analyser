package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.commons.collections4.IteratorUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ObjectGraphIterator}, focusing on its ability to handle
 * a root iterator that contains other iterators, including empty ones.
 */
public class ObjectGraphIteratorTest {

    @Test
    void objectGraphIteratorShouldSkipEmptyNestedIterators() {
        // This test verifies that ObjectGraphIterator can correctly navigate an
        // iterator of iterators, seamlessly skipping any that are empty.

        // ARRANGE
        // Define the data that will be spread across multiple iterators.
        final List<String> list1 = List.of("One", "Two", "Three");
        final List<String> list2 = List.of("Four");
        final List<String> list3 = List.of("Five", "Six");

        // Create a list of iterators, interspersing the data iterators with empty ones.
        // This setup simulates a common scenario where some branches in a graph are empty.
        final List<Iterator<String>> nestedIterators = new ArrayList<>();
        nestedIterators.add(IteratorUtils.emptyIterator());
        nestedIterators.add(list1.iterator());
        nestedIterators.add(IteratorUtils.emptyIterator());
        nestedIterators.add(list2.iterator());
        nestedIterators.add(IteratorUtils.emptyIterator());
        nestedIterators.add(list3.iterator());
        nestedIterators.add(IteratorUtils.emptyIterator());

        // The ObjectGraphIterator is initialized with an iterator over our list of iterators.
        // It is expected to "flatten" this structure into a single sequence of elements.
        final Iterator<String> graphIterator = new ObjectGraphIterator<>(nestedIterators.iterator());

        // ACT & ASSERT
        // 1. Verify that the iterator correctly yields all elements from the non-empty lists in order.
        final String[] expectedElements = {"One", "Two", "Three", "Four", "Five", "Six"};
        for (final String expectedElement : expectedElements) {
            assertTrue(graphIterator.hasNext(), "Iterator should have another element before getting '" + expectedElement + "'");
            assertEquals(expectedElement, graphIterator.next(), "Iterator should return the correct element");
        }

        // 2. Assert that the iterator is exhausted after all elements have been consumed.
        assertFalse(graphIterator.hasNext(), "Iterator should be exhausted after iterating through all elements");
        assertThrows(NoSuchElementException.class, graphIterator::next,
            "Calling next() on an exhausted iterator should throw an exception");
    }
}