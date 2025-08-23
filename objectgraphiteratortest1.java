package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections4.IteratorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the ObjectGraphIterator's ability to "flatten" an iterator of iterators.
 * <p>
 * This class focuses on the constructor {@link ObjectGraphIterator#ObjectGraphIterator(Iterator)},
 * which is designed to chain multiple iterators together sequentially.
 * </p>
 */
@DisplayName("ObjectGraphIterator (Chaining)")
class ObjectGraphIteratorChainingTest extends AbstractIteratorTest<Object> {

    /** The expected sequence of elements after chaining the iterators. */
    private static final List<String> EXPECTED_ELEMENTS = List.of("One", "Two", "Three", "Four", "Five", "Six");

    /** A list of iterators that will be chained together by the ObjectGraphIterator. */
    private List<Iterator<String>> iteratorsToChain;

    @BeforeEach
    void setUp() {
        final List<String> list1 = List.of("One", "Two", "Three");
        final List<String> list2 = List.of("Four");
        final List<String> list3 = List.of("Five", "Six");

        iteratorsToChain = new ArrayList<>();
        iteratorsToChain.add(list1.iterator());
        iteratorsToChain.add(list2.iterator());
        iteratorsToChain.add(list3.iterator());
    }

    /**
     * Creates an ObjectGraphIterator that chains together multiple iterators.
     * This is the primary subject of the tests in this class.
     */
    @Override
    public ObjectGraphIterator<Object> makeObject() {
        // setUp() is called by the test runner before this method
        return new ObjectGraphIterator<>(iteratorsToChain.iterator());
    }

    /**
     * Creates an empty ObjectGraphIterator.
     */
    @Override
    public ObjectGraphIterator<Object> makeEmptyIterator() {
        return new ObjectGraphIterator<>(IteratorUtils.emptyIterator());
    }

    @Test
    @DisplayName("Should traverse all elements from a sequence of iterators")
    void shouldTraverseAllElementsFromSequenceOfIterators() {
        // Arrange: Create the iterator that chains the iterators prepared in setUp.
        final Iterator<Object> chainedIterator = makeObject();

        // Act: Collect all elements from the chained iterator.
        final List<Object> actualElements = IteratorUtils.toList(chainedIterator);

        // Assert: The collected elements must match the expected sequence.
        assertEquals(EXPECTED_ELEMENTS, actualElements);
    }

    @Test
    @DisplayName("hasNext should return false after full iteration")
    void hasNextShouldReturnFalseAfterFullIteration() {
        // Arrange
        final Iterator<Object> chainedIterator = makeObject();

        // Act: Exhaust the iterator.
        IteratorUtils.drain(chainedIterator);

        // Assert
        assertFalse(chainedIterator.hasNext(), "Iterator should be exhausted after traversing all elements");
    }

    // Note: The original test contained a large number of unused inner classes
    // (Forest, Tree, Branch, Leaf, LeafFinder). These were likely intended for testing
    // the graph traversal capabilities of ObjectGraphIterator with a Transformer,
    // which is a separate use case. They have been removed from this file to reduce
    // clutter and focus solely on the iterator-chaining functionality being tested here.
}