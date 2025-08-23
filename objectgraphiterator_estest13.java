package org.apache.commons.collections4.iterators;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link ObjectGraphIterator}.
 */
public class ObjectGraphIteratorTest {

    /**
     * Verifies that the protected method {@code findNextByIterator} can successfully
     * update the state of an exhausted iterator, allowing it to continue iterating
     * over elements from a newly supplied iterator.
     */
    @Test
    public void findNextByIteratorCanSupplyNewElementsToExhaustedIterator() {
        // --- Arrange ---

        // 1. Create a main iterator designed to be easily exhausted.
        // The root is a simple string.
        final String root = "root";
        // The transformer returns null, ensuring the graph traversal stops after the root.
        final Transformer<String, String> transformer = ConstantTransformer.nullTransformer();
        final ObjectGraphIterator<String> graphIterator = new ObjectGraphIterator<>(root, transformer);

        // 2. Exhaust the iterator by consuming its only element.
        assertEquals("The iterator should yield the root element first.", root, graphIterator.next());
        assertFalse("The iterator should be exhausted after consuming the root.", graphIterator.hasNext());

        // 3. Create a new, non-empty iterator to inject into the main one.
        final List<String> nextElements = Arrays.asList("new_element");
        final Iterator<String> sourceIterator = nextElements.iterator();

        // --- Act ---

        // Call the protected method under test to update the iterator's state.
        graphIterator.findNextByIterator(sourceIterator);

        // --- Assert ---

        // The main iterator should now have a next element, provided by the sourceIterator.
        assertTrue("The iterator should now have a next element.", graphIterator.hasNext());
        assertEquals("The next element should come from the injected source iterator.",
                     "new_element", graphIterator.next());
        assertFalse("The iterator should be exhausted again after consuming the new element.",
                    graphIterator.hasNext());
    }
}